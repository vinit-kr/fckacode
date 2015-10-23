package com.ssinfo.corider.app.exceptions;

import com.ssinfo.corider.app.constants.Constants;

/**
 * Class for handling Registration exception.
 * @author VINIT
 *
 */
public class RegistrationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage = Constants.UNKNOWN; 
	public RegistrationException(String errorMessage){
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public String getMessage(){
		return errorMessage;
	}
	
}
