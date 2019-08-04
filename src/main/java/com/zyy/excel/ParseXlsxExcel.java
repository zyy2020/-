package com.zyy.excel;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class ParseXlsxExcel extends ExcelAbstract{

	/**
	 * 提取列名称的正则表达式
	 */
	private static final String DISTILL_COLUMN_REG="^([A-Z]{1,})";
	
	/**
	 * 读取excel的每一行记录，map的key是列号（A,B,C...）,
	 * value是单元格的值。如果单元格是空的，则没有值
	 */
	private List<Map<String,String>>dataList=new ArrayList<>();
	
	@Override
	public void optRow(int curRow, Map<String, String> rowValueMap) {
		Map<String, String>dataMap=new HashedMap<>();
		rowValueMap.forEach((k,v)->dataMap.put(removeNum(k), v));
		dataList.add(dataMap);
	}
	
	/**
	 * 将字符串转换成日期
	 * @param dateNum
	 * @return
	 */
	public static String dateNum2Str(String dateNum) {
		Date date=HSSFDateUtil.getJavaDate(Double.parseDouble(dateNum));
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    return formatter.format(date);
	}
	
	/**
	 * 删除单元格名称中数字，只保留列号
	 * @param cellName 单元格名称,如：A1
	 * @return 列号,如：A
	 */
	private String removeNum(String cellName) {
		Pattern pattern = Pattern.compile(DISTILL_COLUMN_REG);
		Matcher m = pattern.matcher(cellName);
		if(m.find()) {
			return m.group(1);
		}
		return "";
	}

	public List<Map<String, String>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, String>> dataList) {
		this.dataList = dataList;
	}
	
	  public static void main(String[] args) { 
	    	try { 
		    	ParseXlsxExcel excel = new ParseXlsxExcel(); 
		    	 try {
		    		 excel.readOneSheet("D:\\\\BrowserFile\\\\test.xlsx","sheet1"); 
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	 System.out.println(excel.getDataList().isEmpty());
		    	System.out.println(excel.getDataList().get(42).get("D")); 
		    	System.out.println(excel.getDataList().get(0));
		    	 System.out.println(excel.getDataList().get(excel.getDataList().size()-42)); 
		    	} catch (Exception e) { 
		    	e.printStackTrace(); 
		    	} 
	  }
}
