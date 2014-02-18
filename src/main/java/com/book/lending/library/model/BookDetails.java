package com.book.lending.library.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="bookdetails")
public class BookDetails {
	
	

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
	private List<UserDetails> userDetails;

	public BookDetails(){ }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<UserDetails> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(List<UserDetails> userDetails) {
		this.userDetails = userDetails;
	}

	
	public Book getBook(BookDetails bookDetails){
		Book book =new Book();
		if(null!=bookDetails){
			book.setAuthor(this.getAuthor());
			book.setBookName(bookDetails.getBookName());
			book.setTitle(bookDetails.getTitle());
			book.setSerialNumber(bookDetails.getSerialNumber());
			book.setCategory(bookDetails.getCategory());
		}
		return book;
	}


	public List<User> getUser(BookDetails bookDetails){
		List<User> userList = new ArrayList<User>();
		if(null!=bookDetails){
			List<UserDetails> userDetailsList = bookDetails.getUserDetails();
			userList = new ArrayList<User>(); 
			for(UserDetails userDetails : userDetailsList){
				User user = new User();	
				user.setDob(userDetails.getDob());
				user.setFirstName(userDetails.getFirstName());
				user.setLastName(userDetails.getLastName());
				user.setUserContact(userDetails.getUserContact());
				userList.add(user);
			}
		}
		return userList;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((bookName == null) ? 0 : bookName.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		result = prime * result
				+ ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((userDetails == null) ? 0 : userDetails.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookDetails other = (BookDetails) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (bookName == null) {
			if (other.bookName != null)
				return false;
		} else if (!bookName.equals(other.bookName))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userDetails == null) {
			if (other.userDetails != null)
				return false;
		} else if (!userDetails.equals(other.userDetails))
			return false;
		return true;
	}

	
}
