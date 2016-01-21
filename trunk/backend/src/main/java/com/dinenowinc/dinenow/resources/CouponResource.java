package com.dinenowinc.dinenow.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.dropwizard.auth.Auth;

import javax.persistence.RollbackException;
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
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.Coupon;
import com.dinenowinc.dinenow.model.CouponType;
import com.dinenowinc.dinenow.model.NetworkStatus;
import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
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
	protected HashMap<String, Object> fromEntity(Coupon entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("couponCode", entity.getCoupon_code());
		dto.put("couponType", entity.getCoupon_type());		
		dto.put("discountValue", entity.getDiscount_value());
		dto.put("startDate", entity.getStart_date());
		dto.put("endDate", entity.getEnd_date());
		dto.put("maximumValue", entity.getMaximum_value());
		dto.put("manimumValue", entity.getMinimum_value());
		dto.put("networkStatus", entity.getNetworkStatus());
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Coupon fromAddDto(HashMap<String, Object> dto) {
		Coupon entity = super.fromAddDto(dto);

		entity.setCoupon_code(Long.parseLong(dto.get("couponCode").toString()));
		entity.setCoupon_type(CouponType.valueOf(dto.get("couponType").toString()));
		entity.setNetworkStatus(NetworkStatus.valueOf(dto.get("networkStatus").toString()));
		entity.setDiscount_value(Integer.parseInt(dto.get("discountValue").toString()));
		entity.setMaximum_value(Integer.parseInt(dto.get("maximumValue").toString()));
		entity.setMinimum_value(Integer.parseInt(dto.get("manimumValue").toString()));
		entity.setStart_date(new Date(Long.parseLong(dto.get("startDate").toString())));
		entity.setEnd_date(new Date(Long.parseLong(dto.get("endDate").toString())));
		if (dto.containsKey("restaurants")) {
			ArrayList<String> listKeyRestaurants = (ArrayList<String>) dto.get("restaurants");
			for (String key : listKeyRestaurants) {
				Restaurant restaurant = restaurantDao.findOne(key);
				entity.addRestaurants(restaurant);
			}
		}
		
		return entity;
	}



	@SuppressWarnings("unchecked")
	@Override
	protected Coupon fromUpdateDto(Coupon t, HashMap<String, Object> dto) {
		Coupon entity = super.fromUpdateDto(t, dto);
		
		entity.setCoupon_code(Long.parseLong(dto.get("couponCode").toString()));
		entity.setCoupon_type(CouponType.valueOf(dto.get("couponType").toString()));
		entity.setNetworkStatus(NetworkStatus.valueOf(dto.get("networkStatus").toString()));
		entity.setDiscount_value(Integer.parseInt(dto.get("discountValue").toString()));
		entity.setMaximum_value(Integer.parseInt(dto.get("maximumValue").toString()));
		entity.setMinimum_value(Integer.parseInt(dto.get("manimumValue").toString()));
		entity.setStart_date(new Date(Long.parseLong(dto.get("startDate").toString())));
		entity.setEnd_date(new Date(Long.parseLong(dto.get("endDate").toString())));
		if (dto.containsKey("restaurants")) {
			ArrayList<String> listKeyRestaurants = (ArrayList<String>) dto.get("restaurants");
			for (String key : listKeyRestaurants) {
				Restaurant restaurant = restaurantDao.findOne(key);
				entity.addRestaurants(restaurant);
			}
		}
		return entity;
	}



	//==============================ACTION====================================//


	@Override
	protected HashMap<String, Object> onGet(Coupon entity) {

		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto = super.onGet(entity);
		return dto;
	}




	@Override
	protected Response onAdd(AccessToken access, Coupon entity, Restaurant restaurant) {
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));
	}

	@Override
	protected Response onUpdate(AccessToken access, Coupon entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));	
	}

	@Override
	protected Response onDelete(AccessToken access,Coupon entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED, new ServiceErrorMessage("has relationship"));
		}
	}



	//============================METHOD=====================================//

	@GET
	@ApiOperation("api get all coupon for ADMIN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
	})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
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
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
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
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.add(access, dto);
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
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN) {
			Coupon coupon = couponDao.findOne(id);
			if (coupon != null) {
				return super.update(access, id, dto);
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
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN ) {
			Coupon coupon = couponDao.findOne(id);
			if (coupon != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

}