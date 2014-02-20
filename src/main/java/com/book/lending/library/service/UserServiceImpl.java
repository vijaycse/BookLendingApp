package com.book.lending.library.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.model.UserDetails;
import com.book.lending.library.web.BookServiceController;

@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserSevice{

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(BookServiceController.class);

	public static final String COLLECTION_NAME = "userdetails";
	
	
	/****
	 * 
	 */
	public void addPerson(UserDetails person) throws BookLendingException {
		if(null!=person){
			try{
				logger.debug("adding a new user" + person.getFirstName());
				if (!mongoTemplate.collectionExists(UserDetails.class)) {
					mongoTemplate.createCollection(UserDetails.class);
				}
				person.setUserId(UUID.randomUUID().toString());
				logger.debug("adding a new user" + person.getFirstName() + "with id " +  person.getId());
				mongoTemplate.insert(person, COLLECTION_NAME);
			}
			catch(Exception ex){
				throw new BookLendingException("1103 ", "Adding the user is unsucessful " + ex.getMessage());
			}
		}
		else{
			throw new BookLendingException("1100 ", "User is invalid ");
		}
	}

	public List<UserDetails> listPerson() throws BookLendingException {
		try{
			return mongoTemplate.findAll(UserDetails.class, COLLECTION_NAME);
		}
		catch(Exception ex){
			throw new BookLendingException("1107 ", "Fetching  user is unsucessful " + ex.getMessage());
		}
	}

	public UserDetails findUser(UserDetails user) throws BookLendingException{
		Criteria criteria = Criteria.where("userId").is(user.getUserId());
		try{
			return mongoTemplate.findOne(new Query(criteria), UserDetails.class);
		}
		catch(Exception ex){
			throw new BookLendingException("1105 ", "Fetching user is unsucessful " + ex.getMessage());
		}
	}
	
	
	/****
	 * 
	 */
	public void deletePerson(UserDetails person) throws BookLendingException {
		if(null!=person){
			try{
				mongoTemplate.remove(person, COLLECTION_NAME);
			}catch(Exception ex){
				throw new BookLendingException("1101 ", "Deleting user is unsucessful " + ex.getMessage());
			}
		}
		else{
			throw new BookLendingException("1100 ", "User is invalid ");
		}
	}
	
	
	/**
	 * 
	 */
	public void updatePerson(UserDetails person) throws BookLendingException {
		if(null!=person){
			try {
				logger.debug("updating a new user" + person.getFirstName() + "with id " +  person.getId());
				mongoTemplate.save(person, COLLECTION_NAME);
			}
			catch(Exception ex){
				throw new BookLendingException("1102 ", "Updateing user is unsucessful " + ex.getMessage());
			}
		}
		else{
			throw new BookLendingException("1100 ", "User is invalid ");
		}
	}

}
