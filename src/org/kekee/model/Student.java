package org.kekee.model;

import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class Student {
	private int studentId;
	private String stuNumber;
	private String userName;
	private String password;
	private int dormBuildId = 0;
	private String dormBuildName;
	private String dormName;
	private String name;
	private String sex;
	private String tel;
	
	public Student() {
	}
	
	public Student(String userName, String password) {
		this.stuNumber = userName;
		this.userName = userName;
		this.password = password;
	}

	public Student(String stuNumber, String password, int dormBuildId,
			String dormName, String name, String sex, String tel) {
		this.stuNumber = stuNumber;
		this.userName = stuNumber;
		this.password = password;
		this.dormBuildId = dormBuildId;
		this.dormName = dormName;
		this.name = name;
		this.sex = sex;
		this.tel = tel;
	}
}
