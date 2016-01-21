package com.dinenowinc.dinenow.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;

public class AddressBookValidator {

	private HashMap<String, Object> dto;
	
	public AddressBookValidator(HashMap<String, Object> dto) {
		this.dto = dto;
	}

	public List<ServiceErrorMessage> validateForAdd(){
		List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
		if (!dto.containsKey("name")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'name'"));
		}else if(dto.get("name").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Name cannot be empty"));
		}
		if (!dto.containsKey("address_1")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'address_1'"));
		}else if(dto.get("address_1").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Address_1 cannot be empty"));
		}
		if (!dto.containsKey("address_1")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'address_1'"));
		}else if(dto.get("address_1").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Address_1 cannot be empty"));
		}
		if (!dto.containsKey("address_2")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'address_2'"));
		}else if(dto.get("address_2").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Address_2 cannot be empty"));
		}
		if (!dto.containsKey("city")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'city'"));
		}else if(dto.get("city").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("City cannot be empty"));
		}
		if (!dto.containsKey("province")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'province'"));
		}else if(dto.get("province").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Province cannot be empty"));
		}
		if (!dto.containsKey("country")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'country'"));
		}else if(dto.get("country").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Country cannot be empty"));
		}
		if (!dto.containsKey("postal_code")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'postal_code'"));
		}else if(dto.get("postal_code").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Postal_code cannot be empty"));
		}
		if (!dto.containsKey("delivery_instructions")){
			errorMessages.add(new ServiceErrorMessage("Missing field 'delivery_instructions'"));
		}else if(dto.get("delivery_instructions").toString().length()==0) {
			errorMessages.add(new ServiceErrorMessage("Delivery_instructions cannot be empty"));
		}
		return errorMessages;
	}
}
