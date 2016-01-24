package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.ReviewDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/comments")
@Api("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource extends AbstractResource<Review>{

	@Inject
    ReviewDao  reviewDao;
	
	@Inject
	RestaurantDao  rDao;
	
	@Override
	protected HashMap<String, Object> fromEntity(Review entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}
	
	@Override
	protected Review fromAddDto(HashMap<String, Object> dto) {
		Review review = super.fromAddDto(dto);
		review.setRating(Integer.parseInt(dto.get("rating").toString()));
		review.setReviews(dto.get("reviews").toString());
		return review;
	}
	
	@Override
	protected HashMap<String, Object> onGet(Review entity) {
		return super.onGet(entity);
	}

	
	@Override
	protected Review fromUpdateDto(Review t, HashMap<String, Object> dto) {
		Review review = super.fromUpdateDto(t, dto);
		review.setRating(Integer.parseInt(dto.get("rating").toString()));
		review.setReviews(dto.get("reviews").toString());
		return review;
	}	
	
	//=================================ACTION==================================//	
	
		@Override
		protected Response onAdd(User access, Review review, Restaurant restaurant) {
			if (restaurant == null) {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
			}
			review.setCustomerName(access.getName());
			restaurant.addgetReviews(review); //SELECT r.tax FROM Restaurant r inner join r.tax WHERE r.id = :value
			dao.save(review);
			double l = (double)dao.getEntityManager().createQuery("select avg(rv.rating) from Restaurant r inner join r.reviews rv where r.id= :value",Double.class)
					.setParameter("value", restaurant.getId()).
					getSingleResult();
			System.out.println(l);
			restaurant.setRating(l);
			rDao.update(restaurant);
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(review));
		}
		
		@Override
		protected Response onUpdate(User access,  Review review, Restaurant restaurant) {
			dao.update(review);
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(review));	
		}
	
	@GET
	@ApiOperation(value = "api get all reviews", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
			return super.getAll(access);
	}
		
	@GET
	@Path("/{id}")
	@ApiOperation(value = "api get detail user role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
			return super.get(access, id);

	}
	
	
	
	
	@POST
	@ApiOperation(value = "api add new user role", notes="{"
			+ "<br/>  \"name\": \"Waiter\","
			+ "<br/>}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		System.out.println(":::::::::::"+access.getName());
		if (access.getRole() == UserRole.CUSTOMER) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("Only for Customer"));
	}
	
	
/*	@PUT
	@ApiOperation(value = "api update payment type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.CUSTOMER) {
				return super.update(access, id, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}*/
	
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "api delete payment type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.CUSTOMER) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}	
}
