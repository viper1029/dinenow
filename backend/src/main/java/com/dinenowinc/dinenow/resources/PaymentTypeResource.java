package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.PaymentTypeDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AddressBook;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.validation.PaymentTypeValidator;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/payment_type")
@Api("/payment_type")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentTypeResource extends AbstractResource<PaymentType>{

	@Inject
    PaymentTypeDao  paymentTypeDao;
	
	@Inject
	private RestaurantDao restaurantDao;
	
	@Override
	protected HashMap<String, Object> fromEntity(PaymentType entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}
	
	@Override
	protected PaymentType fromAddDto(HashMap<String, Object> dto) {
		PaymentType paymentType = super.fromAddDto(dto);
			paymentType.setName(dto.get("name").toString());
		return paymentType;
	}
	

	
	@Override
	protected PaymentType fromUpdateDto(PaymentType t, HashMap<String, Object> dto) {
		PaymentType paymentType = super.fromUpdateDto(t, dto);
		paymentType.setName(dto.get("name").toString());
		return paymentType;
	}	
	
	@GET
	@ApiOperation(value = "api get all payment type", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
		
	@GET
	@Path("/{id}")
	@ApiOperation(value = "api get detail payment type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	
	
	@POST
	@ApiOperation(value = "api add new payment type", notes="{"
			+ "<br/>  \"name\": \"51 cash\","
			+ "<br/>}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			PaymentTypeValidator typeValidator = new PaymentTypeValidator(paymentTypeDao, dto);
			List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
			if (mListError.size() == 0) {
				return super.add(access, dto);
			}
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("access denied for user"));
	}
	
	
	@PUT
	@ApiOperation(value = "api update payment type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
				PaymentTypeValidator typeValidator = new PaymentTypeValidator(paymentTypeDao, dto);
				List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
				if (mListError.size() == 0) {
					return super.update(access, id, dto);
				}
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "api delete payment type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}	
}
