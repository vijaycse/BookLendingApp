package com.book.lending.library.model;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="book")
public class Book {
	@Id
	private String id;


	@Indexed
	@NotNull
	private String bookName;
	@Indexed(unique=true)
	@NotNull
	private String serialNumber;
	private String author;
	private String title;
	private String category;

	private List<String> keyword;

	public Book(){ }

	public Book(String name, String serialNumber, String author,String title){
		this.bookName=name;
		this.serialNumber=serialNumber;
		this.title=title;
		this.author=author;
	}


	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<String> getKeyword() {
		return keyword;
	}
	public void setKeyword(List<String> keyword) {
		this.keyword = keyword;
	}


	@Override
	public String toString() {
		return "Book [bookName=" + bookName + ", serialNumber=" + serialNumber
				+ ", author=" + author + ", title=" + title + ", category="
				+ category + ", keyword=" + keyword + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

 


}
