package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.helpers.ModelHelpers;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Path("/categories")
@Api("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource extends AbstractResource<Category> {

  @Inject
  private CategoryDao categoryDao;

  @Inject
  private RestaurantDao restaurantDao;

  @GET
  @ApiOperation(value = "get all categories for admin and owner")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "restaurant not found"),
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      List<Category> entities = this.dao.getAll();
      LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
      returnMap.put("categories", getMapListFromEntities(entities));
      return ResourceUtils.asSuccessResponse(Status.OK, returnMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @Path("/restaurant/{restaurant_id}")
  @ApiOperation("Get All Categories By Restaurant")
  @GET
  public Response getCategoriesByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      if (restaurantDao.get(restaurant_id) != null) {
        List<Category> entities = categoryDao.getCategoriesByRestaurantId(restaurant_id);
        List<HashMap<String, Object>> returnMap = ModelHelpers.fromEntities(entities);
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("categories", returnMap);
        return ResourceUtils.asSuccessResponse(Status.OK, dto);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @GET
  @Path("/{id}")
  @ApiOperation(value = "get detail of categories")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @POST
  @ApiOperation(value = "api add new category")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "restaurant not found"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @Override
  protected Response onCreate(User access, Category entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addCategory(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @Override
  protected Category getEntityForInsertion(HashMap<String, Object> inputMap) {
    Category entity = super.getEntityForInsertion(inputMap);
    entity.setCategoryName(inputMap.get("name").toString());
    entity.setCategoryDescription(inputMap.get("description").toString());
    return entity;
  }

  @PUT
  @ApiOperation(value = "update category")
  @Path("/{id}")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "category not found"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Category category = categoryDao.get(id);
      if (category != null) {
        return super.update(access, id, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Category not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Category getEntityForUpdate(Category category, HashMap<String, Object> inputMap) {
    category.setCategoryName(inputMap.get("name").toString());
    category.setCategoryDescription(inputMap.get("description").toString());
    return category;
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "delete a category")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 404, message = "category not found"),
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
        return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }
}