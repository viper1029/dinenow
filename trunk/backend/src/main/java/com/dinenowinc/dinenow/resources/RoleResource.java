package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.HashMap;

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

import com.dinenowinc.dinenow.dao.RoleDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Role;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/roles")
@Api("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource extends AbstractResource<Role>{

	@Inject
    RoleDao  roleDao;
	
	
	@Override
	protected HashMap<String, Object> getMapFromEntity(Role entity) {
		return entity.toDto();
	}
	
	@Override
	protected Role getEntityForInsertion(HashMap<String, Object> inputMap) {
		Role role = super.getEntityForInsertion(inputMap);
		role.setName(inputMap.get("name").toString());
		return role;
	}

	
	@Override
	protected Role getEntityForUpdate(Role role, HashMap<String, Object> inputMap) {
		role.setName(inputMap.get("name").toString());
		return role;
	}	
	
	@GET
	@ApiOperation(value = "api get all User Role", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
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
		if (access.getRole() == UserRole.ADMIN) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	
	
	@POST
	@ApiOperation(value = "api add new user role", notes="{"
			+ "<br/>  \"name\": \"Waiter\","
			+ "<br/>}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data") 
			})
	@Override
	public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
		if (access.getRole() == UserRole.ADMIN ||access.getRole() == UserRole.OWNER) {
			return super.create(access, inputMap);
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
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
				return super.update(access, id, inputMap);
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
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}	
}
