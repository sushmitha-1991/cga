package com.dsp.cga.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExcelExportUtil {

	@Autowired
	private Utils utils;
	
	public byte[] generateExcel(List<Map<String, Object>> data,String title) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		System.out.println("----in generate excel util-----");
		// === Styles ===
	    CellStyle headerStyle = workbook.createCellStyle();
	    Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerStyle.setFont(headerFont);
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setAlignment(HorizontalAlignment.LEFT);
	    headerStyle.setBorderBottom(BorderStyle.THIN);
	    headerStyle.setBorderTop(BorderStyle.THIN);
	    headerStyle.setBorderLeft(BorderStyle.THIN);
	    headerStyle.setBorderRight(BorderStyle.THIN);
	    
	 // Data Style (with borders)
	    CellStyle dataStyle = workbook.createCellStyle();
	    dataStyle.setAlignment(HorizontalAlignment.LEFT);
	    dataStyle.setBorderBottom(BorderStyle.THIN);
	    dataStyle.setBorderTop(BorderStyle.THIN);
	    dataStyle.setBorderLeft(BorderStyle.THIN);
	    dataStyle.setBorderRight(BorderStyle.THIN);
	    
	 // Title Row
	    Row titleRow = sheet.createRow(0);
	    Cell titleCell = titleRow.createCell(0);
	    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	    titleCell.setCellValue(title+" as on " + today);

	    CellStyle titleStyle = workbook.createCellStyle();
	    Font titleFont = workbook.createFont();
	    titleFont.setBold(true);
	    titleFont.setFontHeightInPoints((short) 12);
	    titleStyle.setFont(titleFont);
	    titleStyle.setAlignment(HorizontalAlignment.CENTER);
	    titleCell.setCellStyle(titleStyle);

	    System.out.println("-------1------------------");
	    // Merge title row across columns
	    int columnSize = !data.isEmpty() ? data.get(0).size()+1 : 1; //added +1 for serial number
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSize-1));

		if (!data.isEmpty()) {
			Map<String, Object> firstRow = data.get(0);
			Row headerRow = sheet.createRow(2);
			int colIndex = 0;

			 // Add "S. No." column manually
		    Cell snoHeader = headerRow.createCell(colIndex++);
		    snoHeader.setCellValue("S. No.");
		    snoHeader.setCellStyle(headerStyle);
		    
			// Create header
			for (String key : firstRow.keySet()) {
				Cell cell = headerRow.createCell(colIndex++);
				cell.setCellValue(utils.sanitizeForExcel(key)); // Sanitize header (optional)
				cell.setCellStyle(headerStyle);
			}
			
			System.out.println("----------2-----------------");

			// Fill data rows
			int rowIndex = 3;
			int sNo=1;
			
			for (Map<String, Object> rowData : data) {
				Row row = sheet.createRow(rowIndex++);
				colIndex = 0;
				
				// First column = Serial No.
		        Cell snoCell = row.createCell(colIndex++);
		        snoCell.setCellValue(sNo++);
		        snoCell.setCellStyle(dataStyle);
		        
				for (Object value : rowData.values()) {
					Cell cell = row.createCell(colIndex++);
					String sanitizedValue = utils.sanitizeForExcel(value != null ? value.toString() : "");
					cell.setCellValue(sanitizedValue);
					cell.setCellStyle(dataStyle);
				}
			}
			
			System.out.println("-------------3------------");
//			// === Auto-size all columns with minimum width ===
//			int totalColumns = !data.isEmpty() ? data.get(0).size()+1 : 0; //added +1 for serial number
//			for (int i = 0; i < totalColumns; i++) {
//			    sheet.autoSizeColumn(i);
//			    System.out.println("---inside auto sizing column----");
//			    // minimum width (e.g., 4000 = ~15 characters)
//			    int currentWidth = sheet.getColumnWidth(i);
//			    int minWidth = 4000; 
//			    if (currentWidth < minWidth) {
//			        sheet.setColumnWidth(i, minWidth);
//			    }
//			}
			System.out.println("file write");
		}
//		
		
		System.out.println("-------------writing out------------");
		workbook.write(out);
		workbook.close();
		System.out.println("----before returning excel bytes----");
		byte[] excelBytes = out.toByteArray();
		System.out.println("----returning excel bytes----");
		return excelBytes;
		

//		return ResponseEntity.ok().headers(headers).body(excelBytes);
	}
}
