package com.dinenowinc.dinenow.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;

public final class ResourceUtils {
	private static final List<Object> list = new ArrayList<Object>();
	public static Response asSuccessResponse(Status statusCode, Object data) {
		HashMap<String, Object> entity = new HashMap<String, Object>();
		entity.put("data", data);
		entity.put("errors", list);
	//	entity.put("errorMessage", null);
		return Response.status(statusCode).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}
	
//	public static Response asFailedResponse(Status statusCode, String errorMessage) {
//		HashMap<String, Object> entity = new HashMap<String, Object>();
//		entity.put("errorMessage", errorMessage);
//		entity.put("data", null);		
//		return Response.status(statusCode).entity(entity).type(MediaType.APPLICATION_JSON).build();
//	}
	
	public static Response asFailedResponse(Status statusCode, Object errorMessage) {	
		if(errorMessage instanceof ServiceErrorMessage){
			List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
			errorMessages.add((ServiceErrorMessage)errorMessage);
			System.out.println(errorMessages.size());
			return Response.status(statusCode).entity(errorMessages).type(MediaType.APPLICATION_JSON).build();
		}
		return Response.status(statusCode).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
	}
	
	public static Response asFailedResponse(Status statusCode, List<ServiceError> errorMessage) {
		HashMap<String, Object> entity = new HashMap<String, Object>();
		String errorlist = "";
		for (int i = 0; i < errorMessage.size(); i++) {
			errorlist += errorMessage.get(i).getMessageName().toString() + (i!=errorMessage.size()-1?", ":"");
		}
		entity.put("errorMessage", errorlist);
		entity.put("data", null);		
		return Response.status(statusCode).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}	
}
