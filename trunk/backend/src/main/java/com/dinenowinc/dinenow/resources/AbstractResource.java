package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.lang.reflect.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.AdminDao;
import com.dinenowinc.dinenow.dao.BaseEntityDAOImpl;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Admin;
import com.dinenowinc.dinenow.model.BaseEntity;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.ModelHelpers;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;

public abstract class AbstractResource<T extends BaseEntity> implements IAbstractResource{

	@Inject
	protected BaseEntityDAOImpl<T, String> dao;
	
	@Inject
	private RestaurantDao restaurantDao;
	
	@Inject
	private RestaurantUserDao restaurantUserDao;
	
	@Inject
	private CustomerDao customerDao;
	
	@Inject
	private AdminDao adminDao;
	
	
	protected List<HashMap<String, Object>> fromEntities(List<T> entities)	{
//		List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
//		
//		for (T entity : entities) {
//			dtos.add(fromEntity(entity));			
//		}
//		
//		return dtos;
		
		return ModelHelpers.fromEntities(entities);
	}
	protected abstract HashMap<String, Object> fromEntity(T entity);
	
	
	private Class<?> mClass;
	
	public AbstractResource() {
		ParameterizedType parameterizedType  = (ParameterizedType) (this.getClass().getGenericSuperclass());
		mClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
	}	

	public Class<?> getClassT(){		
		return (Class<?>) mClass;
	}
	
	@SuppressWarnings("unchecked")
	protected T fromDto(HashMap<String, Object> dto) {
		try {
			T entity = (T) getClassT().newInstance();
			if(dto.containsKey("id")) {
				entity.setId(dto.get("id").toString());			
			}
			return entity;
		}
		catch(Exception ex) {
			return null;			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T fromFullDto(HashMap<String, Object> dto) {
		try {
			T entity = (T) getClassT().newInstance();
			if(dto.containsKey("id")) {
				entity.setId(dto.get("id").toString());			
			}
			return entity;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T fromAddDto(HashMap<String, Object> dto) {
		try {
			T entity = (T) getClassT().newInstance();
			return entity;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	protected T fromUpdateDto(T t, HashMap<String, Object> dto) {
		try {
			return t;
		}
		catch(Exception ex) {
			return null;			
		}
	}
	
	protected Response onAdd(User access, T entity, Restaurant restaurant) {
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	protected Response onAdd(T entity, Restaurant restaurant) {
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	
	protected Response onUpdate(User access, T entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	
	protected Response onDelete(User access,T entity) {
		System.out.println(entity);
		dao.delete(entity);
		
		return ResourceUtils.asSuccessResponse(Status.OK, null);
	}
	
	protected Response onChangeStatus(User access,T entity) {
		System.out.println(entity);
		dao.changeStatus(entity);
		
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	@Override
	public Response getAll(@Auth User access) {
		LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		List<T> entities = this.dao.findAll();		
		List<HashMap<String, Object>> dtos = fromEntities(entities);
	//	System.out.println(entity.getClass().getSimpleName().toLowerCase()+":::::::::::::");
		dto.put(getClassT().getSimpleName().toLowerCase()+'s', dtos);
		return ResourceUtils.asSuccessResponse(Status.OK, dto);
	}
	
	
	@Override
	public Response get(@Auth User access,@PathParam("id") String id) {
		T entity = dao.findOne(id);
    	if (entity != null) {
    		HashMap<String, Object> dto = onGet(entity);
    		    		
    		return ResourceUtils.asSuccessResponse(Status.OK, dto);
    	} else {
    		return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Cannot found entity"));
    	}		
	}
	
	
	protected HashMap<String, Object> onGet(T entity) {
		HashMap<String, Object> dto = fromEntity(entity);
		return dto;		
	}	
	
	@Override
	public Response add(@Auth User access, HashMap<String, Object> dto) {
		T entity = fromAddDto(dto);
		Admin adminUser;
		RestaurantUser restaurantUser;
		if(access.getRole() == UserRole.ADMIN){
			adminUser = adminDao.getAdminById(access.getId().toString());
			entity.setCreatedBy(adminUser.getLastName());
		} else if(access.getRole() == UserRole.OWNER){
			restaurantUser=restaurantUserDao.getRestaurantUserById(access.getId().toString());
			entity.setCreatedBy(restaurantUser.getLastName());
		} else if(access.getRole() == UserRole.CUSTOMER){
			Customer customer= customerDao.getCustomerByID(access.getId().toString());
			entity.setCreatedBy(customer.getLastName());
		}
		
		Date date=new Date();
		entity.setCreatedDate(date);
		Response response = null;
		try {
			if (dto.containsKey("restaurantId")) {
				response = onAdd(access, entity, restaurantDao.findOne(dto.get("restaurantId").toString()));
				
			}else {
				response = onAdd(access, entity, null);
			}
		}
		catch (javax.persistence.RollbackException e) { 
			e.printStackTrace();
			response = ResourceUtils.asFailedResponse(Status.NOT_ACCEPTABLE, new ServiceErrorMessage(String.format("Duplicate Entity, This's Already Exist")));
		}
		catch(Exception ex) {
			ex.printStackTrace();
			response = ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot add entity. Error message: %s", ex.getMessage())));			
		}		
		return response;
	}
	
	public Response add(HashMap<String, Object> dto) {
		T entity = fromAddDto(dto);
		Date date=new Date();
		entity.setCreatedDate(date);
		entity.setCreatedBy("self");
		Response response = null;
		try {
			response = onAdd(entity, null);
		}
		catch (javax.persistence.RollbackException e) { 
			e.printStackTrace();
			response = ResourceUtils.asFailedResponse(Status.NOT_ACCEPTABLE, new ServiceErrorMessage(String.format("Duplicate Entity, This's Already Exist")));
		}
		catch(Exception ex) {
			response = ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot add entity. Error message: %s", ex.getMessage())));			
		}		
		return response;
		
	}
	
	@Override
	public Response update(@Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		T entity = dao.findOne(id);		
		entity = fromUpdateDto(entity, dto);
		Response response = null;
		try {
			if (dto.containsKey("restaurantId")) {
				response = onUpdate(access, entity, restaurantDao.findOne(dto.get("restaurantId").toString()));
			}else {
				response = onUpdate(access, entity, null);
			}
		}
		catch (javax.persistence.RollbackException e) { 
			e.printStackTrace();
			response = ResourceUtils.asFailedResponse(Status.NOT_ACCEPTABLE, new ServiceErrorMessage(String.format("Duplicate Entity, This's Already Exist")));
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot update entity. Error message: %s", ex.getMessage())));			
		}	
		return response;
	}

	@Override
	public Response delete(@Auth User access, @PathParam("id") String id) {
		System.out.println(id);
		T entity = dao.findOne(id);
		System.out.println(entity);
		Response response = null;
		
    	if (entity != null) {
    		System.out.println(entity.toString());
    		try {
    			onChangeStatus(access,entity);
    			response = onDelete(access,entity);
    		}
    		catch(Exception ex) {
    			ex.printStackTrace();
    			response = ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot delete entity, Only Update this entity. Error message: %s", ex.getMessage())));			
    		}
    		
    		return response;
    	} else {    		
    		return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Cannot found entity"));
    	}
	}
}

