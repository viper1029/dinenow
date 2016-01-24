package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.HashMap;

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

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/categories")
@Api("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource extends AbstractResource<Category> {	
	@Inject
	private CategoryDao categoryDao;
	@Inject
	private RestaurantDao restaurantDao;
	

	
	@Override
	protected HashMap<String, Object> fromEntity(Category entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

	
	@Override
	protected Category fromAddDto(HashMap<String, Object> dto) {
		Category entity = super.fromAddDto(dto);
		entity.setCategoryName(dto.get("name").toString());
		entity.setCategoryDescription(dto.get("description").toString());		
		return entity;
	}
	
	@Override
	protected Category fromUpdateDto(Category t, HashMap<String, Object> dto) {
		Category entity = super.fromUpdateDto(t, dto);
		entity.setCategoryName(dto.get("name").toString());
		entity.setCategoryDescription(dto.get("description").toString());		
		return entity;
	}
	
	

//=================================ACTION==================================//	
	
	@Override
	protected Response onAdd(User access, Category entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		entity.setCompositeId(restaurant.getId());
		restaurant.addCategory(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	@Override
	protected Response onUpdate(User access, Category entity, Restaurant restaurant) {
		restaurant = restaurantDao.findByCategoryId(entity.getId());
		entity.setCompositeId(restaurant.getId());
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));	
	}
	
	
	@Override
	protected Response onDelete(User access,Category entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK,fromEntity(entity));
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED,new ServiceErrorMessage("has relationship"));
		}
	}
	
	
	
	
	//==========================METHOD==============================//
	
	@GET
	@ApiOperation(value="api get all list categories for admin and owner")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "restaurant not found"), 
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
//		if (access.getRole() == UserRole.OWNER) {
//				List<Category> entities = this.categoryDao.getListByUser(access);	
//				List<HashMap<String, Object>> dtos = fromEntities(entities);
//				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
				return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation(value="api get detail of categories")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@POST
	@ApiOperation(value="api add new category", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\":\"18c8fed0-a7bc-4887-9951-990e829285db\","
			+ "<br/>  \"categoryName\": \"category name\","
			+ "<br/>  \"categoryDescription\": \"categorydescription\""
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "restaurant not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	@PUT
	@ApiOperation(value="api update category", notes="<pre><code>{"
			+ "<br/>  \"categoryName\": \"category name update\","
			+ "<br/>  \"categoryDescription\": \"categorydescription update\""
			+ "<br/>}</code></pre>")
	@Path("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "category not found"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Category category = categoryDao.findOne(id);
			if (category != null) {
				return super.update(access, id, dto);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("category not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value="api delete a category")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "category not found"),
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Category category = categoryDao.findOne(id);
			if (category != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("category not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
}