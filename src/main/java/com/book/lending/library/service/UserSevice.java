package com.book.lending.library.service;

import java.util.List;

import com.book.lending.library.model.UserDetails;

public interface UserSevice {
	public void addPerson(UserDetails person);
	public List<UserDetails> listPerson();
	public void deletePerson(UserDetails person);
	public void updatePerson(UserDetails user);
	public UserDetails findUser(UserDetails user);
}
