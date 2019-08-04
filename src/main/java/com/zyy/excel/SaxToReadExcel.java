package com.zyy.excel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SaxToReadExcel {
	
	//TODO 其他静态变量请自定义
    private static final String RID = "r:id";
    /**
     * 根据sheetname获取rid信息
     * @param filename
     * @param pamMap
     * @throws Exception
     */
    public void getSheetName(String filename, Map<String, Object> pamMap) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
 
        XMLReader parser = fetchSheetParser(sst, pamMap);
 
        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        // getWorkbookData()获取的workbook数据
        InputStream workbookData = r.getWorkbookData();
        InputSource workbookSource = new InputSource(workbookData);
        parser.parse(workbookSource);
        workbookData.close();
    }
 
    /**
     * 指定rid获取sheet内的内容信息
     * @param filename
     * @param pamMap
     * @throws Exception
     */
    public void processOneSheet(String filename, Map<String, Object> pamMap) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
 
        XMLReader parser = fetchSheetParser(sst, pamMap);
        //one sheet
        InputStream sheet2 = r.getSheet(pamMap.get(RID).toString());
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }
 
    /**
     * 执行所有的sheets数据
     * @param filename
     * @throws Exception
     */
    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
 
        XMLReader parser = fetchSheetParser(sst, null);
 
        Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
    }
 
    public XMLReader fetchSheetParser(SharedStringsTable sst, Map<String, Object> pamMap) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        ContentHandler handler = new SheetHandler(sst, pamMap);
        parser.setContentHandler(handler);
        return parser;
    }
    
    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private Map<String, Object> pamMap;
 
        private SheetHandler(SharedStringsTable sst, Map<String, Object> pamMap) {
            this.pamMap = pamMap;
            this.sst = sst;
        }
 
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            //  TODO 想看格式打开此处
            //  System.out.print("<" + name + ">");
            if (name.equals("c")) {
                // Print the cell reference
                //cellRef = A10
                String cellRef = attributes.getValue("r");
                CellReference cellReference = new CellReference(cellRef);
                //col
                short col = cellReference.getCol();
                int row = cellReference.getRow();
                //TODO 所有的数据信息都可以个那就此处来进行识别处理。处理方式如输出
                //TODO 最好的处理方式是执行一行即可处理数据，所有的业务请根据此处的
                //TODO cellRef = A10来识别行列处理
                System.out.println("cellRef = " + cellRef + "; col = " + col
                        + "; row = " + row + "; convertNumToColString = " + CellReference.convertNumToColString(col));
 
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            if(name.equals("sheet")){
                if(pamMap != null){
                    String sheetName = (String) pamMap.get("sheetName");
                    if(!StringUtils.isEmpty(sheetName) && attributes.getValue("name").equals(sheetName)){
                        pamMap.put(RID, attributes.getValue(RID));
                    }
                }
 
                System.out.print(" name=" + attributes.getValue("name"));
                System.out.print("; r:id=" + attributes.getValue("r:id"));
            }
            // Clear contents cache
            lastContents = "";
        }
 
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }
 
 
            // v => contents of a cell
            // Output after we've seen the string contents
            if (name.equals("v")) {
                System.out.println(lastContents);
            }
            //  TODO 想看格式打开此处
            // System.out.print("</" + name + ">");
        }
 
        /**
         * 补充最后的数据处理，此步很重要
         */
        public void endDocument(){
            //TODO 此处为最后的文件输出地方
            //TODO 如果此处不处理，可能会丢失最后的一行数据，如果是自己写逻辑按照行处理的话
            //TODO 最后一行一定要处理
 
        }
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }
    public static void main(String[] args) throws Exception {
        String filePath = "D:\\BrowserFile\\test.xlsx";
        SaxToReadExcel example = new SaxToReadExcel();
        Map<String, Object> pamMap = new HashMap<String, Object>();
        pamMap.put("r:id", "sheet1");
        //根据sheetName 获取指定的sheetid 信息
        example.getSheetName(filePath, pamMap);
 
        //String rid = (String) pamMap.get(RID);
        //Map<String, Object> pamMap2 = new HashMap<String, Object>();
        //pamMap2.put(RID, rid);
        //执行指定的sheet
        example.processOneSheet(filePath, pamMap);
        //执行所有的sheets
//        example.processAllSheets(filePath);
    }
}
