package com.dinenowinc.dinenow.service;

import com.dinenowinc.dinenow.dao.AdminDao;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Admin;
import com.google.inject.Inject;

public class AdminService {
	@Inject
	private AdminDao adminDao;
	@Inject
	private AdminService adminService;
//	@Inject
//	private RestaurantUserService restaurantUserService;
	

	public ServiceResult<Admin> createAdmin(Admin admin){
		Admin res = null;
		res= admin;
		System.out.println(res);
		adminDao.save(res);
		return null;
	}
	
//	public ServiceResult<Customer> createNewCustomer(Admin resData) {
//		CustomerValidator validator = buildTaskValidator(resData);
//		ServiceErrorValidationMessage errorMessages = validator.validateForCreation();
//		Admin res = null;
//
//		if (errorMessages.getErrors().size() == 0) {
//			res = resData;
//			adminDao.save(res);
//			String id_customer_stripe = DineNowApplication.stripe.createCustomer(res);
//        	res.setCustomerStripe(id_customer_stripe);
//        	adminDao.update(res);
//		}
//		return new ServiceResult<Customer>(resData, errorMessages);
//	}
	
//	public ServiceResult<Customer> createNewCustomerFOrSocical(Customer resData, String social) {
//		CustomerValidator validator = buildTaskValidator(resData);
//		ServiceErrorValidationMessage errorMessages = validator.validateForCreationSocial();
//		Customer res = null;
//		if (errorMessages.getErrors().size() == 0) {
//			Customer customerGet = getUserBySocial(social);
//			if (customerGet == null) {
//	        	res = resData;
//	        	customerDao.save(res);
//	        	String id_customer_stripe = DineNowApplication.stripe.createCustomer(res);
//	        	res.setCustomerStripe(id_customer_stripe);
//	        	customerDao.update(res);
//			}else {
//				resData = customerGet;
//			}
//		}
//		return new ServiceResult<Customer>(resData, errorMessages);
//	}
//	
	

//	public ServiceResult<Customer> updateCustomer(Customer resData) {
//		CustomerValidator validator = buildTaskValidator(resData);
//
//		ServiceErrorValidationMessage errorMessages = validator.validateForUpdate();
//		Customer res = null;
//		if (errorMessages.getErrors().size() == 0) {
//			res = resData;
//			customerDao.update(resData);
//		}
//		return new ServiceResult<Customer>(resData, errorMessages);
//	}

//	private CustomerValidator buildTaskValidator(Customer c) {
//		return new CustomerValidator(this.customerService,this.restaurantUserService, c);
//	}


	public Admin checkCredentials(String email, String unencryptedPassword) {		
		return adminDao.getAdminByEmailAndPassword(email, unencryptedPassword);
	}
	
	
	public Admin getUserByEmail(String name){
		return adminDao.getAdminByEmail(name);
	}
	
}
