package com.ssinfo.corider.app.exceptions;

public class SignUpException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3233756758366015524L;
	String errorMessage = "Error";
	public SignUpException(String message){
		super(message);
		this.errorMessage = message;
		
	}

	public String getMessage(){
		return errorMessage;
	}
}
