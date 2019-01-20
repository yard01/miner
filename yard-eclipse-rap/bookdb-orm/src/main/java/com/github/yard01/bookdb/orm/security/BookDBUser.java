package com.github.yard01.bookdb.orm.security;

public class BookDBUser {
	Integer ID;
	String name;
	BookDBRole role;
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
	public BookDBRole getRole() {
		return role;
	}
	public void setRole(BookDBRole role) {
		this.role = role;
	}
}
