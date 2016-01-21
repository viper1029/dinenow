package com.dinenowinc.dinenow.error;


import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class that represents errors that occur during service layer calls. 
 * The messageName is a internal string used for translations.
 * The context defines in which field the error occurred (e.g. the web form field name).
 * 
 */
@EqualsAndHashCode()
@ToString
public class ServiceErrorValidationdelete {

    private ServiceErrorValidationMessage messageName;
    

	public ServiceErrorValidationdelete() {
	}
	
	
    public ServiceErrorValidationdelete(ServiceErrorValidationMessage messageName) {
        this.messageName = messageName;
    }    
    
	
    public ServiceErrorValidationMessage getMessageName() {
		return messageName;
	}
    
    

}
