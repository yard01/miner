package com.github.yard01.bookdb.orm.security;

public class BookDBRole {
	Integer ID;
	String name;	
	String PerspectiveID;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPerspectiveID() {
		return PerspectiveID;
	}
	public void setPerspectiveID(String perspectiveID) {
		PerspectiveID = perspectiveID;
	}
}
