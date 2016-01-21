package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.RollbackException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.AddOnDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.dao.TaxDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.ModelHelpers;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SizeInfo;
import com.dinenowinc.dinenow.model.Tax;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Path("/taxes")
@Api("/taxes")
public class TaxResource extends AbstractResource<Tax>{

	@Inject
	private TaxDao taxeDao;
	@Inject 
	private RestaurantDao restaurantDao;
	
	
	
	@Override
	protected HashMap<String, Object> fromEntity(Tax entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
/*		dto.put("id", entity.getId());
		dto.put("taxeName", entity.getTaxeName());
//		dto.put("taxeDescription", entity.getTaxeDescription());		
		dto.put("taxeValue", entity.getTaxeValue());*/
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	protected Tax fromAddDto(HashMap<String, Object> dto) {
		Tax entity = super.fromAddDto(dto);
		entity.setTaxeName(dto.get("name").toString());
		entity.setTaxeDescription(dto.get("description").toString());
		entity.setTaxeValue(Double.parseDouble(dto.get("value").toString()));
		return entity;
	}
	
	
	
	@Override
	protected Tax fromUpdateDto(Tax t, HashMap<String, Object> dto) {
		Tax entity = super.fromUpdateDto(t, dto);
		entity.setTaxeName(dto.get("name").toString());
		entity.setTaxeDescription(dto.get("description").toString());
		entity.setTaxeValue(Double.parseDouble(dto.get("value").toString()));
		return entity;
	}
	
	
	
	//==============================ACTION====================================//
	
	
	@Override
	protected HashMap<String, Object> onGet(Tax entity) {
		return super.onGet(entity);
	}
	

	
	
	@Override
	protected Response onAdd(AccessToken access, Tax entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		restaurant.addTaxe(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));
	}
	
	@Override
	protected Response onUpdate(AccessToken access, Tax entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));	
	}
	
	@Override
	protected Response onDelete(AccessToken access, Tax entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED, new ServiceErrorMessage("has relationship"));
		}
	}
	
	
	
	//============================METHOD=====================================//
	
	@GET
	@ApiOperation("api get all taxe of restaurant for ADMIN and OWNER")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
//		if (access.getRole() == UserRole.OWNER) {
//			List<AddOn> entities = addOnDao.getListByUser(access);
//			List<HashMap<String, Object>> dtos = fromEntities(entities);
//			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of taxe")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "Access denied for user") 
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user")); 
	}
	
	
	
	@POST
	@ApiOperation(value="api add new add on for restaurant", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"6e441b10-180c-4275-b3d4-19dbbdd7f139\","
			+ "<br/>  \"taxeValue\": 5,"
			+ "<br/>  \"taxeDescription\": \"Taxe GTGT XXX\","
			+ "<br/>  \"taxeName\": \"Taxe\""
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	
	@PUT
	@ApiOperation(value="update add on", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"6e441b10-180c-4275-b3d4-19dbbdd7f139\","
			+ "<br/>  \"taxeValue\": 5,"
			+ "<br/>  \"taxeDescription\": \"Taxe GTGT XXX\","
			+ "<br/>  \"taxeName\": \"Taxe\""
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
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Tax taxe = taxeDao.findOne(id);
			if (taxe != null) {
				return super.update(access, id, dto);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Taxe not found"));
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
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Tax taxe = taxeDao.findOne(id);
			if (taxe != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Taxe not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
}
