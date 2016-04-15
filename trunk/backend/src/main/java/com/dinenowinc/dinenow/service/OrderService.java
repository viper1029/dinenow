package com.dinenowinc.dinenow.service;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.validation.CustomerValidator;
import com.google.inject.Inject;

public class OrderService {
	
	@Inject
	private OrderDao orderDao;
	
	public ServiceResult<Order> addOrder(Order resData) {
		
			Order res = resData;
			orderDao.save(res);
		return new ServiceResult<>(resData);
	}

}
