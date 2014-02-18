package com.book.lending.library.exception;

public class ErrorInfo {
	
	private String errorId;
	private String errorMessage;
	
	public ErrorInfo (String errorId, String message){
		this.errorId=errorId;
		this.errorMessage=message;
	}
	
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	

}
