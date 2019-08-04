package com.zyy.excel;

import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes.Name;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public abstract class ExcelAbstract extends DefaultHandler{
	
	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;
	private int curRow=0;
	private String curCellName="";
   /**
    * 读取当前行的数据。key是单元格名称如A1，value是单元格中的值。如果单元格空，则没有数据。	
    */
	private Map<String,String>rowValueMap=new HashedMap<>();
	
	/**
	 * 处理单行数据的回调方法
	 * @param curRow 当前行
	 * @param rowValueMap 当前行的值
	 */
	public abstract void optRow(int curRow,Map<String,String>rowValueMap);
	
	/**
	 * 读取excel指定sheet页的数据
	 * @param filePath   文件路径
	 * @param sheetNum  sheet页编号，从1开始
	 * @throws Exception
	 */
	public void readOneSheet(String filePath,int sheetNum) throws Exception {
		if(filePath.endsWith(".xlsx")) {//处理excel文件
			OPCPackage pkg = OPCPackage.open(filePath);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			XMLReader parser=getSheetParser(sst);
			//根据rid或者rsheet查找sheet
			InputStream sheet2 = r.getSheet("rid"+sheetNum );
		    InputSource sheetSource = new InputSource(sheet2);
		    parser.parse(sheetSource);
		    sheet2.close();
		}else {
			throw new Exception("文件格式错误,文件扩展名只能是.xlsx");
		}
	}
	
	/**
     * 读取Excel指定sheet页的数据。
     * 
     * @param filePath 文件路径
     * @param sheetName sheet页名称。
     * @throws Exception
     */
    public void readOneSheet(String filePath, String sheetName) throws Exception {
    	
    	if (filePath.endsWith(".xlsx")) {//处理excel2007文件
        	OPCPackage pkg = OPCPackage.open(filePath);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
 
            XMLReader parser = getSheetParser(sst);
            
            // 根据名称查找sheet
            int sheetNum = 0;
            SheetIterator sheets = (SheetIterator)r.getSheetsData();
            while (sheets.hasNext()) {
            	curRow = 0;
            	sheetNum++;
            	InputStream sheet = sheets.next();
            	if(sheets.getSheetName().equals(sheetName)){
            		sheet.close();
            		break;
            	}
            	sheet.close();
            }
            // 根据 rId# 或 rSheet# 查找sheet
            InputStream sheet2 = r.getSheet("rId" + sheetNum);
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
            sheet2.close();
        } else {
            throw new Exception("文件格式错误，文件扩展名只能是xlsx。");
        }
    	
        
    }
	
	

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
       
		//根据sst索引值得到单元格真正要存储的字符串
		//这时characters方法可能会被调用多次
		if(nextIsString) {
			try {
				int idx = Integer.parseInt(lastContents);
				lastContents=new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			} catch (Exception e) {
			}
		}
		//v=>单位格的值，如果单元格是字符串则v标签的值为该字符串在sst中的索引
		//将单元格内容加入rowlist中，在这之前先去掉字段串前后的空白符
		if(name.equals("v")) {
			String value = lastContents.trim();
		   value=	value.equals("") ? "": value;
		   rowValueMap.put(curCellName, value);
		}else {
			//如果标签名称为row，这说明已到行尾，调用optRows（）方法
			if(name.equals("row")) {
				optRow(curRow, rowValueMap);
				rowValueMap.clear();
				curRow++;
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		 //c=>单元格
		if(name.equals("c")) {
			//如果下一个元素是sst的索引，则将nextIsString标记为true
			String cellType=attributes.getValue("t");
			if(cellType!=null&&cellType.equals("s")) {
				nextIsString=true;
			}else {
				nextIsString=false;
			}
		}
		//置空
		lastContents="";
		//记录当前读取单元格的名称
		String cellName = attributes.getValue("r");
		if(cellName!=null&&!cellName.isEmpty()) {
			curCellName=cellName;
		}
	}

	/**
	 * 获取单个sheet页的xml解析器
	 * @param sst
	 * @return
	 * @throws SAXException 
	 */
	private XMLReader getSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser=XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst=sst;
		parser.setContentHandler(this);
		return parser;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		lastContents+=new String(ch,start,length);
	}
	
	
	

}
