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

import com.book.lending.library.model.UserDetails;
import com.book.lending.library.web.BookServiceController;

@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserSevice{

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceController.class);

	public static final String COLLECTION_NAME = "userdetails";

	public void addPerson(UserDetails person) {
		logger.debug("adding a new user" + person.getFirstName());
		if (!mongoTemplate.collectionExists(UserDetails.class)) {
			mongoTemplate.createCollection(UserDetails.class);
		}
		person.setUserId(UUID.randomUUID().toString());
		logger.debug("adding a new user" + person.getFirstName() + "with id " +  person.getId());
		mongoTemplate.insert(person, COLLECTION_NAME);
	}

	public List<UserDetails> listPerson() {
		return mongoTemplate.findAll(UserDetails.class, COLLECTION_NAME);
	}
	
	public UserDetails findUser(UserDetails user){
		Criteria criteria = Criteria.where("userId").is(user.getUserId());
		return mongoTemplate.findOne(new Query(criteria), UserDetails.class);
	}

	public void deletePerson(UserDetails person) {
		mongoTemplate.remove(person, COLLECTION_NAME);
	}

	public void updatePerson(UserDetails person) {
		logger.debug("updating a new user" + person.getFirstName() + "with id " +  person.getId());
		mongoTemplate.save(person, COLLECTION_NAME);
	}

}
