package com.dinenowinc.dinenow.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dinenowinc.dinenow.dao.PaymentTypeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;

public class PaymentTypeValidator {

	private final PaymentTypeDao paymentTypeDao;
	
	private HashMap<String, Object> dto;

	public PaymentTypeValidator(PaymentTypeDao paymentTypeDao,
			HashMap<String, Object> dto) {
		this.paymentTypeDao = paymentTypeDao;
		this.dto = dto;
	}
	
	public List<ServiceErrorMessage> validateForAdd(){
		List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
		if (!dto.containsKey("name")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'name'"));
		}else if(dto.get("name").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Name cannot be empty"));
		}
		return errorMessages;
	}
}
