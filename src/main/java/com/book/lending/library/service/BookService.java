package com.book.lending.library.service;

import java.util.List;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.model.Book;
import com.book.lending.library.model.BookDetails;

public interface BookService {
	public List<Book> listBookDoNotUse();
	public List<BookDetails> listBooks();
	public BookDetails getBookById(String serialNumber) throws BookLendingException;
	public List<BookDetails> getBook(String author,
			 String title,  String keywords) throws BookLendingException;;
	public List<BookDetails> getBookWithUser(
			 String author,  String title,  String keywords) throws BookLendingException;;
	public boolean addBookWithBookDetails(BookDetails bookDetails) throws BookLendingException;
	public boolean updateBookDetails(BookDetails book) throws BookLendingException;
	public boolean deleteBookDetails(BookDetails book) throws BookLendingException;
	
}
