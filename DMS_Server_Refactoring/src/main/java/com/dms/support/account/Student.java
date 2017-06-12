package com.dms.support.account;

public class Student {
	private String uid;
	private String number;
	private String name;
	
	public Student(String uid, String number, String name) {
		this.setUid(uid);
		this.setNumber(number);
		this.setName(name);
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
