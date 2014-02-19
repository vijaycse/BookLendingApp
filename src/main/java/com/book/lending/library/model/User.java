package com.book.lending.library.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class User {
	
	@Id
	//private org.bson.types.ObjectId  ObjectId;
	// moongo DB id auto generated
	private String id;
	// randon number for our reference
	@Indexed(unique=true)
	private String userId;
    private String firstName;
    @Indexed
    private String lastName;
    @Indexed
    private String dob;
    private UserContactInfo userContact;
    
    //@Field("heldBooks")
    //@DBRef(db="user")
    //@DBRef
	private List<Book> heldBooks;
    
    
	public User() {}

    public User(String firstName, String lastName,String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob=dob;
    }
 
    
    public List<Book> getHeldBooks() {
		return heldBooks;
	}

	public void setHeldBooks(List<Book> heldBooks) {
		this.heldBooks = heldBooks;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public UserContactInfo getUserContact() {
		return userContact;
	}

	public void setUserContact(UserContactInfo userContact) {
		this.userContact = userContact;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", dob=" + dob + ", userContact=" + userContact
				+ ", heldBooks=" + heldBooks + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



 

}
