package com.dinenowinc.dinenow.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

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

import com.dinenowinc.dinenow.dao.CouponDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Coupon;
import com.dinenowinc.dinenow.model.helpers.CouponType;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/coupons")
@Api("/coupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CouponResource extends AbstractResource<Coupon> {

	@Inject 
	private RestaurantDao restaurantDao;

	@Inject 
	private CouponDao couponDao;

	@Override
	protected HashMap<String, Object> getMapFromEntity(Coupon entity) {
		HashMap<String, Object> dto = new HashMap<>();
		dto.put("id", entity.getId());
		dto.put("couponType", entity.getCouponType());
		dto.put("discountValue", entity.getDiscountValue());
		dto.put("startDate", entity.getStartDate());
		dto.put("endDate", entity.getEndDate());
		dto.put("maximumValue", entity.getMaximumValue());
		dto.put("manimumValue", entity.getMinimumValue());
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Coupon getEntityForInsertion(HashMap<String, Object> inputMap) {
		Coupon entity = super.getEntityForInsertion(inputMap);

		entity.setCouponType(CouponType.valueOf(inputMap.get("couponType").toString()));
		entity.setDiscountValue(Integer.parseInt(inputMap.get("discountValue").toString()));
		entity.setMaximumValue(Integer.parseInt(inputMap.get("maximumValue").toString()));
		entity.setMinimumValue(Integer.parseInt(inputMap.get("manimumValue").toString()));
		entity.setStartDate(new Date(Long.parseLong(inputMap.get("startDate").toString())));
		entity.setEndDate(new Date(Long.parseLong(inputMap.get("endDate").toString())));
		if (inputMap.containsKey("restaurants")) {
			ArrayList<String> listKeyRestaurants = (ArrayList<String>) inputMap.get("restaurants");
			for (String key : listKeyRestaurants) {
				Restaurant restaurant = restaurantDao.get(key);
			}
		}
		
		return entity;
	}



	@SuppressWarnings("unchecked")
	@Override
	protected Coupon getEntityForUpdate(Coupon coupon, HashMap<String, Object> inputMap) {
		coupon.setCouponType(CouponType.valueOf(inputMap.get("couponType").toString()));
		coupon.setDiscountValue(Integer.parseInt(inputMap.get("discountValue").toString()));
		coupon.setMaximumValue(Integer.parseInt(inputMap.get("maximumValue").toString()));
		coupon.setMinimumValue(Integer.parseInt(inputMap.get("manimumValue").toString()));
		coupon.setStartDate(new Date(Long.parseLong(inputMap.get("startDate").toString())));
		coupon.setEndDate(new Date(Long.parseLong(inputMap.get("endDate").toString())));
		if (inputMap.containsKey("restaurants")) {
			ArrayList<String> listKeyRestaurants = (ArrayList<String>) inputMap.get("restaurants");
			for (String key : listKeyRestaurants) {
				Restaurant restaurant = restaurantDao.get(key);
			}
		}
		return coupon;
	}



	//==============================ACTION====================================//




	@Override
	protected Response onCreate(User access, Coupon entity, Restaurant restaurant) {
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity, access));
	}

	//============================METHOD=====================================//

	@GET
	@ApiOperation("api get all coupon for ADMIN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
	})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}


	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of coupon")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "Access denied for user") 
	})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user")); 
	}



	@POST
	@ApiOperation(value="api add new add on for restaurant", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\":\"18c8fed0-a7bc-4887-9951-990e829285db\","
			+ "<br/>  \"couponCode\": \"couponCode\","
			+ "<br/>  \"couponType\": \"percent or doller\","
			+ "<br/>  \"networkStatus\": \"ONLINE\","
			+ "<br/>  \"discountValue\": \"discountValue\","
			+ "<br/>}"
			+ "<br/>"
			+ "</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
	})
	@Override
	public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.create(access, inputMap);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}



	@PUT
	@ApiOperation(value="update add on", notes="<pre><code>{"
			+ "<br/>  \"couponCode\": \"couponCode\","
			+ "<br/>  \"couponType\": \"percent or doller\","
			+ "<br/>  \"networkStatus\": \"ONLINE\","
			+ "<br/>  \"discountValue\": \"discountValue\","
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 404, message = "Add on not found"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
	})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
		if (access.getRole() == UserRole.ADMIN) {
			Coupon coupon = couponDao.get(id);
			if (coupon != null) {
				return super.update(access, id, inputMap);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation("delete add on by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "add on not found"),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "Access denied for user")
	})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN ) {
			Coupon coupon = couponDao.get(id);
			if (coupon != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

}