package com.book.lending.library.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserContactInfo {
	


	/*	@Id
	private String id;*/
	@Id
	private String  id;
	
	@Indexed(unique = true)
	@NotNull
	private String userContactEmailID;
	@Indexed(unique = true)
	private String userContactPhone;
	
	public UserContactInfo(){
		}
	
	public UserContactInfo(String userContactEmailID, String userContactPhone){
		this.userContactEmailID=userContactEmailID;
		this.userContactPhone=userContactPhone;
	}
	
	
	public String getUserContactEmailID() {
		return userContactEmailID;
	}

	public void setUserContactEmailID(String userContactEmailID) {
		this.userContactEmailID = userContactEmailID;
	}

	public String getUserContactPhone() {
		return userContactPhone;
	}

	public void setUserContactPhone(String userContactPhone) {
		this.userContactPhone = userContactPhone;
	}

	@Override
	public String toString() {
		return "UserContactInfo [userContactEmailID=" + userContactEmailID
				+ ", userContactPhone=" + userContactPhone + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((userContactEmailID == null) ? 0 : userContactEmailID
						.hashCode());
		result = prime
				* result
				+ ((userContactPhone == null) ? 0 : userContactPhone.hashCode());
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
		UserContactInfo other = (UserContactInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userContactEmailID == null) {
			if (other.userContactEmailID != null)
				return false;
		} else if (!userContactEmailID.equals(other.userContactEmailID))
			return false;
		if (userContactPhone == null) {
			if (other.userContactPhone != null)
				return false;
		} else if (!userContactPhone.equals(other.userContactPhone))
			return false;
		return true;
	}
 
	
	public String validateUserContact(){
		if(!StringUtils.isNumeric(this.userContactPhone)){
			return  "phone number is invalid";
		}
		if(!EmailValidator.validate(this.userContactEmailID)){
			return "email address is not valid";
		}
		return "";
	}
	
	
	
	public static class EmailValidator {
		 
		private static Pattern pattern;
		private static Matcher matcher;
	 
		private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		public EmailValidator() {
			pattern = Pattern.compile(EMAIL_PATTERN);
		}
	 
		/**
		 * Validate hex with regular expression
		 * @param hex
		 *            hex for validation
		 * @return true valid hex, false invalid hex
		 */
		public static boolean validate(final String hex) {
	 		matcher = pattern.matcher(hex);
			return matcher.matches();
	 
		}
	}
	
	
	
	
}
