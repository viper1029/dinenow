package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceError;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SubMenu;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.validation.ItemValidator;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Path("/sizes")
@Api("/sizes")
public class SizeResource extends AbstractResource<Size>{

	@Inject
	private SizeDao sizeDao;
	
	@Inject 
	private RestaurantDao restautantDao;
	
	
	
	@Override
	protected HashMap<String, Object> fromEntity(Size entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
/*		dto.put("id", entity.getId());
		dto.put("name", entity.getSizeName());
		dto.put("description", entity.getSizeDescription());*/
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

	
	@Override
	protected Size fromFullDto(HashMap<String, Object> dto) {
		Size entity = super.fromFullDto(dto);
		System.out.println("ssssssssssssssssssss");
		entity.setSizeName(dto.get("name").toString());
		entity.setSizeDescription(dto.get("description").toString());
		return entity;
	}
	
	@Override
	protected Size fromAddDto(HashMap<String, Object> dto) {
		Size entity = super.fromAddDto(dto);
		entity.setSizeName(dto.get("name").toString());
		entity.setSizeDescription(dto.get("description").toString());
		return entity;
	}
	
	@Override
	protected Size fromUpdateDto(Size t, HashMap<String, Object> dto) {
		Size entity = super.fromUpdateDto(t, dto);
		entity.setSizeName(dto.get("name").toString());
		entity.setSizeDescription(dto.get("description").toString());
		return entity;
	}
	
	
	
	
	//==============================ACTION====================================//
	
	@Override
	protected HashMap<String, Object> onGet(Size entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto = super.onGet(entity);
		return dto;
	}
	
	
	@Override
	protected Response onAdd(AccessToken access, Size entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		entity.setCompositeId(restaurant.getId());
		restaurant.addSizes(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	@Override
	protected Response onUpdate(AccessToken access, Size entity, Restaurant restaurant) {
		restaurant = restautantDao.findBySizeId(entity.getId());
		entity.setCompositeId(restaurant.getId());
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));	
	}
	
	@Override
	protected Response onDelete(AccessToken access,Size entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED, new ServiceErrorMessage("has relationship"));
		}
	}
	//============================METHOD=====================================//
	
	@GET
	@ApiOperation("api get all Size of restaurant for ADMIN and OWNER")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
//		if (access.getRole() == UserRole.OWNER) {
//			List<Size> entities = sizeDao.getListByUser(access);
//			List<HashMap<String, Object>> dtos = fromEntities(entities);
//			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of Size")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "Access denied for user")
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		System.out.println("===");
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	@POST
	@ApiOperation(value="api add new Size for restaurant", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"aa27652f-864c-4a2a-b44d-a1ccc3be8b36\","
			+ "<br/>  \"name\": \"name size\","
			+ "<br/>  \"description\": \"description size\""
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 406, message = "Duplicate Entity, This's Already Exist"), 
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override  
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		System.out.println("add data");
		System.out.println(dto);
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	@PUT
	@ApiOperation(value="update size", notes="<pre><code>{"
			+ "<br/>  \"sizeName\": \"name size\","
			+ "<br/>  \"sizeDescription\": \"description size\""
			+ "<br/>}</code></pre>")
	@Path("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Size not found"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Override
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Size size = sizeDao.findOne(id);
			if (size != null) {
				return super.update(access, id, dto);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Size not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation("delete size by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "Size not found"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Size size = sizeDao.findOne(id);
			if (size != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Size not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	
	
	
}
