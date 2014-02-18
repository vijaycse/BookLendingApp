package com.book.lending.library.exception;

public class BookLendingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	private String exceptionId;
	
	public BookLendingException(String message){
		this.exceptionMessage=message;
	}
	
	public BookLendingException(String id,String message){
		this.exceptionMessage=message;
		this.exceptionId=id;
		this.setExceptionMessage(message);
	}
	
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	public String getExceptionId() {
		return exceptionId;
	}
	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}
	
	

}
