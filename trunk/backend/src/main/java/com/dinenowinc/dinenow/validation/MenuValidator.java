package com.dinenowinc.dinenow.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dinenowinc.dinenow.dao.MenuDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Hour;

public class MenuValidator {

	private HashMap<String, Object> dto;
	
	private final MenuDao menuDao;
	
	public MenuValidator(HashMap<String, Object> dto , MenuDao menuDao ) {
		this.dto = dto;
		this.menuDao = menuDao;
	}

	public List<ServiceErrorMessage> validateForAdd(){
		List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
		if (!dto.containsKey("name")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'name'"));
		}else if(dto.get("name").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Name cannot be empty"));
		}
		if (!dto.containsKey("description")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'description'"));
		}else if(dto.get("description").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Description cannot be empty"));
		}
		return errorMessages;
	}
}
