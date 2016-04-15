package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Cuisine;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.User;
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
import java.util.HashMap;


@Path("/cuisines")
@Api("/cuisines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuisineResource extends AbstractResource<Cuisine> {

  @GET
  @ApiOperation("get list of all cuisines for ADMIN and OWNER")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @GET
  @Path("/{id}")
  @ApiOperation("api get detail of cuisine")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "Access denied for user")
  })
  @Override
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @POST
  @ApiOperation(value = "api add new cuisine")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 406, message = "Duplicate Entity, This's Already Exist"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Cuisine entity, Restaurant restaurant) {
    if (restaurant != null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Incorrect request."));
    }
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @Override
  protected Cuisine getEntityForInsertion(HashMap<String, Object> inputMap) {
    Cuisine entity = super.getEntityForInsertion(inputMap);
    entity.setCuisine(inputMap.get("cuisine").toString());
    return entity;
  }

  @PUT
  @ApiOperation(value = "update cuisine")
  @Path("/{id}")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "cuisine not found"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Not supported"));
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation("delete cuisine by id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 404, message = "cuisine not found"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "Cannot found entity")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN) {
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }
}