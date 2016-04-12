package com.dinenowinc.dinenow.error;

import lombok.ToString;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;

/**
 * 
 * @param <T>
 */
@ToString
public class ServiceResult<T extends BaseEntity> {

    private T result;

    private ServiceErrorValidationMessage errors;

    public ServiceResult() {
        this(null);
    }

    public ServiceResult(T result) {
        this(result, new ServiceErrorValidationMessage());
    }

    public ServiceResult(T result, ServiceErrorValidationMessage errors) {
        this.result = result;
        this.errors = errors;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ServiceErrorValidationMessage getErrors() {
        return errors;
    }

    public void setErrors(ServiceErrorValidationMessage errors) {
        this.errors = errors;
    }

    public boolean wasSuccessful() {
        return errors != null && errors.getErrors().size() == 0;
    }

    public boolean hasErrors() {
        return !wasSuccessful();
    }

    public void addMessage(ServiceErrorContext context, ServiceErrorValidationMessage message) {
        errors.addError(message.getMessage());
    }
    
    public boolean containsError(ServiceErrorMessage searchError) {
    	for (ServiceErrorMessage error : errors.getErrors()) {
			if (error.equals(searchError)) {
				return true;
			}
		}
    	return false;
    }    

}
