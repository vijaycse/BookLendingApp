package com.book.lending.library.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.model.UserDetails;
import com.book.lending.library.service.UserSevice;

@Controller  
@RequestMapping("/users/*")
public class UserController {  

	@Autowired
	private UserSevice personService;

	@RequestMapping(value = "/", method = RequestMethod.GET)  
	public @ResponseBody List<UserDetails> getPersonList() throws BookLendingException {  
		return personService.listPerson();
	}  

	@RequestMapping(value = "/user", method = RequestMethod.POST)  
	public ResponseEntity<String> createPerson(@RequestBody UserDetails person) throws BookLendingException {
		if(StringUtils.hasText(person.getId())) {
			personService.updatePerson(person);
		} else {
			personService.addPerson(person);
		}
		return new ResponseEntity<String>("User is created",
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user", method = RequestMethod.DELETE)  
	public ResponseEntity<String> deletePerson(@RequestBody UserDetails person) throws BookLendingException {  
		personService.deletePerson(person);  
		return new ResponseEntity<String>("User is deleted",
				HttpStatus.GONE); 
	}    
}
