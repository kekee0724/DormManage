package org.kekee.model;


import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class Record {
	
	private int recordId;
	private String studentNumber;
	private String studentName;
	private String date;
	private String detail;
	private int dormBuildId;
	private String dormBuildName;
	private String dormName;
	private String startDate;
	private String endDate;
	
	public Record() {
	}
	
	public Record(String studentNumber, String date, String detail) {
		this.studentNumber = studentNumber;
		this.date = date;
		this.detail = detail;
	}
}
