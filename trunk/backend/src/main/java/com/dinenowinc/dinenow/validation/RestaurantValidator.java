package com.dinenowinc.dinenow.validation;

import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.Restaurant;
import java.util.ArrayList;
import java.util.List;

public class RestaurantValidator {

    private final RestaurantDao restaurantDao;

    private final Restaurant restaurant;



    public RestaurantValidator(RestaurantDao dao, Restaurant u) {
        this.restaurantDao = dao;
        this.restaurant = u;
    }

    public ServiceErrorValidationMessage validateForCreation() {
    	ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
        errors.setMessage("Validation Failed");
        
        return errors;
    }

    public List<ServiceErrorMessage> validateRequiredFields() {
    	List<ServiceErrorMessage> errorMessages = new ArrayList<>();
        return errorMessages;
    }

	public ServiceErrorValidationMessage validateForUpdate() {
		
		ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
        errors.setMessage("Validation Failed");

        errors.addErrors(validateRequiredFields());
        return errors;
	}
}
