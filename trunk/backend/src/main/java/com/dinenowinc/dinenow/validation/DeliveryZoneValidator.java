package com.dinenowinc.dinenow.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.error.FieldNames;
import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorContext;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.vividsolutions.jts.geom.Coordinate;

public class DeliveryZoneValidator {

	private final DeliveryZoneDao deliveryZone;
	
	private HashMap<String, Object> dto;
	
	public DeliveryZoneValidator(DeliveryZoneDao deliveryZoneDao, HashMap<String, Object> dto) {
		this.deliveryZone = deliveryZoneDao;
		this.dto = dto;
	}
	
	/*
	 * {
{
  "deliveryZoneName": "Zone delivery",
  "deliveryZoneDescription": "description",
  "orderMinimun": 34,
  "deliveryFee": 67,
  "deliveryZoneType": "CUSTOM",
  "deliveryZoneCoords": [
    {
      "lat": 10.796507,
      "lng": 106.64822
    },
    {
      "lat": 10.782511,
      "lng": 106.644572
    },
    {
      "lat": 10.803926,
      "lng": 106.673926
    },
    {
      "lat": 10.796507,
      "lng": 106.64822
    }
  ]
}
}
	 */
	
	@SuppressWarnings("unchecked")
	public List<ServiceErrorMessage> validateForAdd(){
		List<ServiceErrorMessage> errorMessages = new ArrayList<>();
        
		if (!dto.containsKey("name")){
			errorMessages.add(new ServiceErrorMessage("Miss field 'name'"));
		}
		if (!dto.containsKey("description")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'description'"));
		}
		if (!dto.containsKey("minimum")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'minimum'"));
		}
		if (!dto.containsKey("fee")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'fee'"));
		}
		if (!dto.containsKey("type")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'type'"));
		}
		if (!dto.containsKey("coordinates")) {
			errorMessages.add(new ServiceErrorMessage("Miss field 'coordinates'"));
		}else {
			List<LinkedHashMap<String,Double>> polygon = (List<LinkedHashMap<String,Double>>)dto.get("coordinates");
			if (polygon.size() < 3) {
				errorMessages.add(new ServiceErrorMessage("Polygon error"));
			}else {
				LinkedHashMap<String,Double> start = polygon.get(0);
				LinkedHashMap<String,Double> end = polygon.get(polygon.size()-1);
				if (!start.get("lat").equals(end.get("lat")) || !start.get("lng").equals(end.get("lng"))) {
					errorMessages.add(new ServiceErrorMessage("Polygon error"));
				}
			}
		}
		return errorMessages;
	}
	
	
	
	public ServiceErrorValidationMessage validateForUpdate() {
		ServiceErrorValidationMessage errors = new ServiceErrorValidationMessage();
        errors.setMessage("Validation Failed");
        errors.addErrors(validateForAdd());
		return errors;
	}
	
	
	
	
}
