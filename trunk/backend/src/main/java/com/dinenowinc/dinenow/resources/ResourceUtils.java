package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ResourceUtils {
  private static final List<Object> list = new ArrayList<>();

  public static Response asSuccessResponse(Status statusCode, Object data) {
    HashMap<String, Object> entity = new HashMap<>();
    entity.put("data", data);
    entity.put("errors", list);
    return Response.status(statusCode).entity(entity).type(MediaType.APPLICATION_JSON).build();
  }

  public static Response asFailedResponse(Status statusCode, Object errorMessage) {
    if (errorMessage instanceof ServiceErrorMessage) {
      List<ServiceErrorMessage> errorMessages = new ArrayList<>();
      errorMessages.add((ServiceErrorMessage) errorMessage);
      return Response.status(statusCode).entity(errorMessages).type(MediaType.APPLICATION_JSON).build();
    }
    return Response.status(statusCode).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
  }

  public static Response asFailedResponse(Status statusCode, List<ServiceError> errorMessage) {
    HashMap<String, Object> entity = new HashMap<>();
    String errorList = "";
    for (int i = 0; i < errorMessage.size(); i++) {
      errorList += errorMessage.get(i).getMessageName().toString() + (i != errorMessage.size() - 1 ? ", " : "");
    }
    entity.put("errorMessage", errorList);
    entity.put("data", null);
    return Response.status(statusCode).entity(entity).type(MediaType.APPLICATION_JSON).build();
  }
}
