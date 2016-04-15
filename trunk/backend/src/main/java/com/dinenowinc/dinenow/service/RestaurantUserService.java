package com.dinenowinc.dinenow.service;

import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.validation.RestaurantUserValidator;
import com.google.inject.Inject;

public class RestaurantUserService {

	@Inject
	private RestaurantUserDao userDao;
	
	

	public ServiceResult<RestaurantUser> createNewUser(RestaurantUser userData) {
		RestaurantUserValidator validator = buildTaskValidator(userData);
		ServiceErrorValidationMessage errorMessages = validator.validateForCreation();
		return new ServiceResult<>(userData, errorMessages);
	}

	public ServiceResult<RestaurantUser> updateUser(RestaurantUser userData) {
		RestaurantUserValidator validator = buildTaskValidator(userData);

		ServiceErrorValidationMessage errorMessages = validator.validateForUpdate();
		RestaurantUser user = null;
		if (errorMessages.getErrors().size() == 0) {
			user = userData;
			userDao.update(userData);
		}
		return new ServiceResult<>(userData, errorMessages);
	}

	private RestaurantUserValidator buildTaskValidator(RestaurantUser newUser) {
		return new RestaurantUserValidator(this.userDao, newUser);
	}

	
	
	//Security
	//private static Map<String, User> userRepository = null;

	private static RestaurantUserService instance = null;

	public static RestaurantUserService getInstance() {
		synchronized (RestaurantUserService.class) {
			if (instance == null) {
				instance = new RestaurantUserService();
				//instance.init();
			}
			return instance;
		}
	}


	/**
	 * Returns user, if a user with the given password was found
	 * 
	 * @param username
	 * @param unencryptedPassword
	 * @return 
	 */
	public RestaurantUser checkCredentials(String email, String unencryptedPassword) {		
		System.out.println("checking credential");
		return userDao.getRestaurantUserByEmailAndPassword(email, unencryptedPassword);
	}
	
	public RestaurantUser getUserByEmail(String email) {
		return userDao.getRestaurantUserByEmail(email);
	}
	
}
