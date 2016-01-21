package com.dinenowinc.dinenow.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorContext;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;

public class ItemValidator {

	private final ItemDao itemDao;
	
	private HashMap<String, Object> dto;
	
	public ItemValidator(ItemDao dao, HashMap<String, Object> dto) {
		this.itemDao = dao;
		this.dto = dto;
	}
	
	/*
	 * {
  "itemName": "item 2",
  "itemDescription": "item",
  "availabilityStatus": "AVAILABLE",
  "isVegeterian": false,
  "spiceLevel": 3,
  "notes": "notes",
  "keywords": "test keyword",
  "linkImage": "link image test",
  "price": 34
}
	 */
	
	public List<ServiceErrorMessage> validateForAdd(){
		List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
		if (!dto.containsKey("name")){
			errorMessages.add(new ServiceErrorMessage("Miss field 'name'"));
		}
		if (!dto.containsKey("description")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'description'"));
		}
		if (!dto.containsKey("availabilityStatus")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'availabilityStatus'"));
		}
		if (!dto.containsKey("isVegeterian")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'isVegeterian'"));
		}
		if (!dto.containsKey("spiceLevel")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'spiceLevel'"));
		}
		if (!dto.containsKey("notes")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'notes'"));
		}
		if (!dto.containsKey("linkImage")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'linkImage'"));
		}
		return errorMessages;
	}
	
	
	
	
}
