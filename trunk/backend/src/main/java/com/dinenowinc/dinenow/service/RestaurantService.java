package com.dinenowinc.dinenow.service;

import java.util.List;

import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.validation.RestaurantValidator;
import com.google.inject.Inject;

public class RestaurantService {

	@Inject
	public RestaurantDao restaurantDao;
	
	

	public ServiceResult<Restaurant> createNewRestaurant(Restaurant resData) {
		RestaurantValidator validator = buildTaskValidator(resData);
		ServiceErrorValidationMessage errorMessages = validator.validateForCreation();
		Restaurant res = null;

		if (errorMessages.getErrors().size() == 0) {
			res = resData;
			restaurantDao.save(res);
		}
		return new ServiceResult<>(resData, errorMessages);
	}

	public ServiceResult<Restaurant> updateRestaurant(Restaurant resData) {
		RestaurantValidator validator = buildTaskValidator(resData);

		ServiceErrorValidationMessage errorMessages = validator.validateForUpdate();
		Restaurant res = null;
		if (errorMessages.getErrors().size() == 0) {
			res = resData;
			restaurantDao.update(resData);
		}
		return new ServiceResult<>(resData, errorMessages);
	}

	private RestaurantValidator buildTaskValidator(Restaurant res) {
		return new RestaurantValidator(this.restaurantDao, res);
	}

	


	private static RestaurantService instance = null;

	public static RestaurantService getInstance() {
		synchronized (RestaurantService.class) {
			if (instance == null) {
				instance = new RestaurantService();
				//instance.init();
			}
			return instance;
		}
	}

	
	public Restaurant getUserByEmail(String name){
		return restaurantDao.findByExactUserName(name);
	}

}
