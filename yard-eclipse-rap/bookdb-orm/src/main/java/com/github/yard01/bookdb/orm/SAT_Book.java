package com.github.yard01.bookdb.orm;

public class SAT_Book {
	Integer ID;
	String 	author;
	String 	name;
	String 	series;
	String 	genre;
	String 	publisher;
	Integer pbYear;
	String 	note;
	HUB_Item hub_Item;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getPbYear() {
		return pbYear;
	}

	public void setPbYear(Integer year) {
		this.pbYear = year;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public HUB_Item getHub_Item() {
		return hub_Item;
	}

	public void setHub_Item(HUB_Item hub_Item) {
		this.hub_Item = hub_Item;
	}

}
