package com.dinenowinc.dinenow.error;

public class ServiceErrorMessage {
	
    private String message;
    
    public ServiceErrorMessage() {
	}
    
    public ServiceErrorMessage(String msg) {
    	this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}
