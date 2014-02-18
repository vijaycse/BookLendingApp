package com.book.lending.library.model;

import java.util.List;

public class BookUserDetails {
	
	
	private BookDetails bookDetails;
	
	private List<UserDetails> userDetails;

	public BookDetails getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}

	public List<UserDetails> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(List<UserDetails> userDetails) {
		this.userDetails = userDetails;
	}
	
	
 
	
}
