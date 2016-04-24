package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.Addon;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.helpers.ModelHelpers;
import io.dropwizard.auth.Auth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.ReviewDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/reviews")
@Api("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource extends AbstractResource<Review> {

  @Inject
  ReviewDao reviewDao;

  @Inject
  RestaurantDao restaurantDao;

  @GET
  @ApiOperation(value = "api get all reviews", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data")
  })
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Path("/{restaurant_id}/comments")
  @ApiOperation("Get All Reviews By Restaurant")
  @GET
  public Response getReviewsByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
    if (restaurantDao.get(restaurant_id) != null) {
      List<Review> entities = reviewDao.getReviewsByRestaurantId(restaurant_id);
      List<HashMap<String, Object>> returnMap = ModelHelpers.fromEntities(entities);
      LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
      dto.put("reviews", returnMap);
      return ResourceUtils.asSuccessResponse(Status.OK, dto);
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found."));
    }
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
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @POST
  @ApiOperation(value = "api add new user role")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.CUSTOMER) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Review review, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    review.setCustomerName(access.getName());
    restaurant.addReview(review);
    dao.save(review);
    double rating = (double) dao.getEntityManager().createQuery(
        "select avg(rv.rating) from Restaurant r inner join r.reviews rv where r.id= :value", Double.class)
        .setParameter("value", restaurant.getId())
        .getSingleResult();
    restaurant.setRating(rating);
    restaurantDao.update(restaurant);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(review));
  }

  @Override
  protected Review getEntityForInsertion(HashMap<String, Object> inputMap) {
    Review review = super.getEntityForInsertion(inputMap);
    review.setRating(Integer.parseInt(inputMap.get("rating").toString()));
    review.setComment(inputMap.get("comment").toString());
    return review;
  }

  @PUT
  @ApiOperation(value = "update review")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 404, message = "Add on not found"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.CUSTOMER) {
      Review review = reviewDao.get(id);
      if (review != null) {
        return super.update(access, id, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Review not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Review getEntityForUpdate(Review review, HashMap<String, Object> inputMap) {
    review.setRating(Integer.parseInt(inputMap.get("rating").toString()));
    review.setComment(inputMap.get("reviews").toString());
    return review;
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "api delete payment type")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN) {
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }
}
