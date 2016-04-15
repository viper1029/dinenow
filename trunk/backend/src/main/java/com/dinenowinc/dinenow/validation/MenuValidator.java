package com.dinenowinc.dinenow.validation;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuValidator {

  private HashMap<String, Object> inputMap;

  public MenuValidator(HashMap<String, Object> inputMap) {
    this.inputMap = inputMap;
  }

  public List<ServiceErrorMessage> validateForCreation() {
    List<ServiceErrorMessage> errorMessages = new ArrayList<>();
    if (!inputMap.containsKey("name")) {
      errorMessages.add(new ServiceErrorMessage("Missing field 'name'"));
    }
    else if (inputMap.get("name").toString().length() == 0) {
      errorMessages.add(new ServiceErrorMessage("Name cannot be empty"));
    }
    return errorMessages;
  }
}
