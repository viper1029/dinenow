package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.service.RestaurantUserService;
import com.dinenowinc.dinenow.utils.MD5Hash;
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
import java.util.Date;
import java.util.HashMap;

@Path("/user")
@Api("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantUserResource extends AbstractResource<RestaurantUser> {

  @Inject
  private RestaurantUserService userService;

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "api get all restaurant user", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }


  @GET
  @Path("/{id}")
  @ApiOperation(value = "api get detail restaurant user", notes = "id for restaurant user")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity")
  })
  @Override
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    return super.get(access, id);
  }


  @POST
  @ApiOperation(value = "api add new restaurant user")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    // Create Restaurant Owner by admin.(Give details of Restaurant for both)
    if (access.getRole() == UserRole.ADMIN && inputMap.get("role").toString().equals("OWNER")) {
      RestaurantUser user = getEntityForInsertion(inputMap);
      ServiceResult<RestaurantUser> result = userService.createNewUser(user);

      if (!result.hasErrors()) {
        return super.create(access, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, result.getErrors());
      }
    }
    else if (access.getRole() == UserRole.OWNER) {
      RestaurantUser user = getEntityForInsertion(inputMap);
      ServiceResult<RestaurantUser> result = userService.createNewUser(user);

      if (!result.hasErrors()) {
        return super.create(access, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, result.getErrors());
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

  @Override
  protected Response onCreate(User access, RestaurantUser entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addUser(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @Override
  protected RestaurantUser getEntityForInsertion(HashMap<String, Object> inputMap) {
    System.out.println("@Override@Override@Override @Override@Override@Override " + inputMap);
    RestaurantUser entity = super.getEntityForInsertion(inputMap);
    entity.setEmail(inputMap.get("email").toString());
    entity.setPassword(MD5Hash.md5Spring(inputMap.get("password").toString()));
    entity.setRole(UserRole.valueOf(inputMap.get("role").toString()));
    entity.setName(inputMap.get("fullName").toString());
    entity.setPhone(inputMap.get("phone").toString());
    entity.setRegisteredDate(new Date());
    return entity;
  }

  @PUT
  @ApiOperation(value = "api update restaurant user")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN && access.getRole() == UserRole.OWNER)
      return super.update(access, id, inputMap);
    else
      return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied"));
  }

  @Override
  protected RestaurantUser getEntityForUpdate(RestaurantUser restaurantUser, HashMap<String, Object> inputMap) {
    restaurantUser.setEmail(inputMap.containsKey("email") ? inputMap.get("email").toString() : restaurantUser.getEmail());
    restaurantUser.setPassword(inputMap.containsKey("password") ? MD5Hash.md5Spring(inputMap.get("password").toString()) : restaurantUser.getPassword());
    restaurantUser.setRole(inputMap.containsKey("role") ? UserRole.valueOf(inputMap.get("role").toString()) : restaurantUser.getRole());
    restaurantUser.setName(inputMap.get("fullName").toString());
    restaurantUser.setPhone(inputMap.get("phone").toString());
    return restaurantUser;
  }


  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "api delete restaurant user", notes = "Delete Restaurant User")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "Cannot found entity")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN) {
      RestaurantUser entity = dao.get(id);
      Response response = ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage(String.format("deleted succesfully", "deleted succesfully")));
      if (entity != null) {
        try {
          //onChangeStatus(access,entity);
        }
        catch (Exception ex) {
          ex.printStackTrace();

        }
        return response;
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Cannot found entity"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }
}
