package com.dinenowinc.dinenow.service;


import java.util.Date;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.validation.CustomerValidator;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerService {

  @Inject
  private CustomerDao customerDao;

  @Inject
  private CustomerService customerService;

  @Inject
  private RestaurantUserService restaurantUserService;

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);


  public ServiceResult<Customer> validateCustomer(Customer customer) {
    CustomerValidator validator = buildTaskValidator(customer);
    ServiceErrorValidationMessage errorMessages = validator.validateForCreation();
    LOGGER.debug(customer.toString());

    return new ServiceResult<>(customer, errorMessages);
  }

  public ServiceResult<Customer> updateCustomer(Customer resData) {
    CustomerValidator validator = buildTaskValidator(resData);

    ServiceErrorValidationMessage errorMessages = validator.validateForUpdate();
    @SuppressWarnings("unused")
    Customer res = null;
    if (errorMessages.getErrors().size() == 0) {
      res = resData;
      customerDao.update(resData);
    }
    return new ServiceResult<>(resData, errorMessages);
  }

  private CustomerValidator buildTaskValidator(Customer c) {
    return new CustomerValidator(this.customerService, this.restaurantUserService, c);
  }


  public Customer checkCredentials(String email, String unencryptedPassword) {
    return customerDao.getCustomerByEmailAndPassword(email, unencryptedPassword);
  }

  public Customer getUserByPassword(String unencryptedPassword) {
    return customerDao.checkByPassword(unencryptedPassword);
  }


  public Customer getUserByEmail(String name) {
    return customerDao.getCustomerByEmail(name);
  }


  public Customer getUserBySocial(String social) {
    return customerDao.findBySocial(social);
  }

  public boolean doesPhoneNumberExist(String phone) {
    return customerDao.getCustomerByPhoneNumber(phone) == null ? false : true;
  }

}
