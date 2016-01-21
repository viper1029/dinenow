package com.dinenowinc.dinenow.error;

import java.util.ArrayList;
import java.util.List;

public class ServiceErrorValidationMessage {

	private List<ServiceErrorMessage> errors;

	private String message;
	
	public ServiceErrorValidationMessage() {
	}
	
	public ServiceErrorValidationMessage(String msg, List<ServiceErrorMessage> errors) {
		this.message = msg;
		this.errors = errors;
	}
	
	

	public String getMessage() {
		return message;
	}

	public ServiceErrorValidationMessage setMessage(String message) {
		this.message = message;
		return this;
	}
	
    public List<ServiceErrorMessage> getErrors() {
    	if (errors == null) {
    		errors = new ArrayList<ServiceErrorMessage>();
		}
		return errors;
	}

	public ServiceErrorValidationMessage setErrors(List<ServiceErrorMessage> errors) {
		this.errors = errors;
		return this;
	}
	
	public ServiceErrorValidationMessage addError(String ierror) {
		ServiceErrorMessage error = new ServiceErrorMessage(ierror);
		getErrors().add(error);
		return this;
	}
	
	public ServiceErrorValidationMessage addErrors(List<ServiceErrorMessage> errors) {
		getErrors().addAll(errors);
		return this;
	}
	
}
