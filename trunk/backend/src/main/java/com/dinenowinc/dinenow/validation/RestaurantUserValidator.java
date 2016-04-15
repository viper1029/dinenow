package com.dinenowinc.dinenow.validation;

import org.apache.commons.lang.StringUtils;











import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.RestaurantUser;

import java.util.ArrayList;
import java.util.List;

public class RestaurantUserValidator {

    private final RestaurantUserDao userDao;

    private final RestaurantUser user;

    private final int usernameMinLenght = 3;
    private final int passwordMinLenght = 6;

    public RestaurantUserValidator(RestaurantUserDao dao, RestaurantUser u) {
        if (dao == null) {
            throw new RuntimeException("com.dinenowinc.dinenow.dao.UserDao null");
        }
        if (u == null) {
            throw new RuntimeException("com.dinenowinc.dinenow.model.User null");
        }

        this.userDao = dao;
        this.user = u;
    }

    public ServiceErrorValidationMessage validateForCreation() {
    	
    	ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
        errors.setMessage("Validation Failed");

        errors.addErrors(validateRequiredFields());
        if (userDao.getRestaurantUserByEmail(user.getEmail()) != null) {
        	errors.addError("Username exist");
		}
        return errors;
    }

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public List<ServiceErrorMessage> validateRequiredFields() {
    	List<ServiceErrorMessage> errorMessages = new ArrayList<>();
        if (StringUtils.isEmpty(user.getEmail())) {
        	 errorMessages.add(new ServiceErrorMessage("Email is empty"));
        }
        else if (!user.getEmail().matches(EMAIL_PATTERN)) {
        	errorMessages.add(new ServiceErrorMessage("Email format incorrect"));
		}

        if (user.getPassword() == null || user.getPassword().length() == 0) {
        	errorMessages.add(new ServiceErrorMessage("Password is empty"));
        }
        else if (user.getPassword().length() < passwordMinLenght) {
        	errorMessages.add(new ServiceErrorMessage("Password is too short"));
        }
        return errorMessages;
    }

	public ServiceErrorValidationMessage validateForUpdate() {
		ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
        errors.setMessage("Validation Failed");

        errors.addErrors(validateRequiredFields());
        return errors;
	}
}
