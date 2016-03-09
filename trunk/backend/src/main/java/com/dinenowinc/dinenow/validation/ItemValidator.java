package com.dinenowinc.dinenow.validation;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemValidator {

  private HashMap<String, Object> inputMap;

  public ItemValidator(HashMap<String, Object> inputMap) {
    this.inputMap = inputMap;
  }

  public List<ServiceErrorMessage> validateForCreation() {
    List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
    if (!inputMap.containsKey("name")) {
      errorMessages.add(new ServiceErrorMessage("Missing field 'name'"));
    }
    return errorMessages;
  }
}
