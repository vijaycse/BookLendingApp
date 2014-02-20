package com.book.lending.library.service;

import java.util.List;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.model.UserDetails;

public interface UserSevice {
	public void addPerson(UserDetails person) throws BookLendingException;
	public List<UserDetails> listPerson() throws BookLendingException;
	public void deletePerson(UserDetails person) throws BookLendingException;
	public void updatePerson(UserDetails user) throws BookLendingException;
	public UserDetails findUser(UserDetails user) throws BookLendingException;
}
