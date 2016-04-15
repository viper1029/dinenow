package com.dinenowinc.dinenow.validation;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.service.CustomerService;
import com.dinenowinc.dinenow.service.RestaurantUserService;

import java.util.ArrayList;
import java.util.List;


public class CustomerValidator {

  private final CustomerService customService;
  private final RestaurantUserService restaurantUserService;
  private final Customer customer;


  public CustomerValidator(CustomerService customerService, RestaurantUserService restaurantUserService, Customer c) {
    this.customService = customerService;
    this.restaurantUserService = restaurantUserService;
    this.customer = c;
  }

  public ServiceErrorValidationMessage validateForCreation() {
    ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
    errors.setMessage("Validation Failed.");

    if (customService.getUserByEmail(customer.getEmail()) != null || restaurantUserService.getUserByEmail(customer.getEmail()) != null) {
      errors.addError("This email is already in use.");
    }
    else if (customService.doesPhoneNumberExist(customer.getPhoneNumber())) {
      errors.addError("This phone number already exists.");
    }
    errors.addErrors(validateRequiredFields());
    return errors;
  }

  public ServiceErrorValidationMessage validateForCreationSocial() {
    ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
    errors.setMessage("Validation Failed");
//        if (customer.getSocialAccounts().get(0).startsWith("facebook:") || customer.getSocialAccounts().get(0).startsWith("google:")) {
//        	
//		}else {
//			errors.create(new ServiceError(new ServiceErrorContext(FieldNames.SOCIAL_FORMAT), ServiceErrorMessage.SOCIAL_FORMAT_ERROR));
//		}
    return errors;
  }


  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  public List<ServiceErrorMessage> validateRequiredFields() {
    List<ServiceErrorMessage> errorMessages = new ArrayList<>();
    if (customer.getEmail() == null || customer.getEmail().length() == 0) {
      errorMessages.add(new ServiceErrorMessage("Email is empty"));
    }
    if (!customer.getEmail().matches(EMAIL_PATTERN)) {
      errorMessages.add(new ServiceErrorMessage("Email format incorrect"));
    }
    if (customer.getPassword() == null || customer.getPassword().length() == 0) {
      errorMessages.add(new ServiceErrorMessage("Password is empty"));
    }
    else if (customer.getPassword().length() < 6) {
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
