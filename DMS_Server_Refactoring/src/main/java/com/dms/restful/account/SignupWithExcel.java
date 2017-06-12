package com.dms.restful.account;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dms.support.account.Student;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.Log;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;
import com.google.common.net.HttpHeaders;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "신입생 정보 입력(엑셀)")
@REST(requestBody = "", successCode = 201, failureCode = 204)
@Route(uri = "/signup/excel", method = HttpMethod.POST)
public class SignupWithExcel implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		List<Student> students = new ArrayList<Student>();
		
		Set<FileUpload> files = ctx.fileUploads();
		for(FileUpload file : files) {
			File excel = new File(file.uploadedFileName());
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(excel);
				XSSFSheet sheet = workbook.getSheetAt(0);
				for(Row row : sheet) {
					Cell numberCell = row.getCell(0);
					Cell nameCell = row.getCell(1);

					String uid = createUid();
					String number = Double.toString(numberCell.getNumericCellValue()).replace(".0", "");
					String name = nameCell.getStringCellValue();
					
					MySQL.executeUpdate("INSERT INTO account(uid, permission) VALUES(?, ?)", SHA256.encrypt(uid), false);
					MySQL.executeUpdate("INSERT INTO student_data(uid, number, name) VALUES(?, ?, ?)", SHA256.encrypt(uid), AES256.encrypt(number), AES256.encrypt(name));
					
					students.add(new Student(uid, number, name));
				}
				
				workbook.close();
				excel.delete();
				
				String uidFilePath = createExcelOfStudentData(students);
				
				ctx.response().putHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=" + new String("UID.xlsx".getBytes("UTF-8"), "ISO-8859-1"));
				ctx.response().sendFile(uidFilePath);
				ctx.response().close();
				new File(uidFilePath).delete();
			} catch (InvalidFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String createUid() {
		while(true) {
			String uuid = UUID.randomUUID().toString().substring(0, 6);
			ResultSet rs = MySQL.executeQuery("SELECT * FROM account WHERE uid=?", SHA256.encrypt(uuid));
			try {
				if(!rs.next()) {
					return uuid;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String createExcelOfStudentData(List<Student> students) {
		File excel = new File("temp_uid.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		int rowCount = 0;
		for(Student student : students) {
			XSSFRow row = sheet.createRow(rowCount++);
			XSSFCell numberCell = row.createCell(0);
			XSSFCell nameCell = row.createCell(1);
			XSSFCell uidCell = row.createCell(2);
			
			numberCell.setCellValue(student.getNumber());
			nameCell.setCellValue(student.getName());
			uidCell.setCellValue(student.getUid());
			Log.I("Registered : " + student.getNumber() + " " + student.getName());
		}
		
		try {
			workbook.write(new FileOutputStream(excel));
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return excel.getPath();
	}
}
