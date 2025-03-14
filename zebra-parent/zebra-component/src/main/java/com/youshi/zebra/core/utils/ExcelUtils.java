package com.youshi.zebra.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author wangsch
 * @date 2017年6月15日
 */
public class ExcelUtils {
	public static final String INVALID_STR_VALUE = "INVALID_STR";
	
	public static final String INVALID_INTEGER_VALUE = "INVALID_NUM";
	
	public static boolean isNotBlank(Cell cell) {
		if (cell== null) {
			return false;
		}
		if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			return false;
		}
		return true;
	}
	
	public static final String EMPTY_STR = "N";
	
	public static String getCellStr(Cell cell) {
		try {
			String result = null;
			int type = cell.getCellType();
			switch(type) {
				case HSSFCell.CELL_TYPE_NUMERIC:
					result = String.valueOf(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getStringCellValue();
					break;
			}
			result = result.trim();
			
			return !EMPTY_STR.equals(result) ? result : "";
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public static Integer getInteger(Cell cell) {
		try{
			Integer result = null;
			int type = cell.getCellType();
			switch(type) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				result = (int)cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_STRING:
				result = Integer.parseInt(cell.getStringCellValue());
				break;
			}
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static Workbook createWorkbook(byte[] buf, String suffix)
			throws IOException {
		Workbook workbook = null;
		if ("xls".equals(suffix)) {
			workbook = new HSSFWorkbook(new ByteArrayInputStream(buf));
		} else if ("xlsx".equals(suffix)) {
			workbook = new XSSFWorkbook(new ByteArrayInputStream(buf));
		}
		return workbook;
	}
	
	public static void shiftEmptyRows(Sheet sheet) {
		for(int i = 0; i < sheet.getLastRowNum(); i++){
	        if(sheet.getRow(i)==null){
	            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
	            i--;
	            continue;
	        }
	        boolean isRowEmpty = true;
	        for(int j =0; j<sheet.getRow(i).getLastCellNum();j++){
	            Cell cell = sheet.getRow(i).getCell(j);
				if(cell == null || cell.toString().trim().equals("")){
					isRowEmpty=true;
	            }else {
	                isRowEmpty=false;
	                break;
	            }
	        }
	        if(isRowEmpty==true){
	            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
	            i--;
	        }
	    }
	}
	
}
