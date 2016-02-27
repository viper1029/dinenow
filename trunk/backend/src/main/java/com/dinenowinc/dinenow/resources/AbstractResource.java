package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.RollbackException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResource<T extends BaseEntity> implements IAbstractResource {

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

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResource.class);

  private Class<?> mClass;

  public AbstractResource() {
    ParameterizedType parameterizedType = (ParameterizedType) (this.getClass().getGenericSuperclass());
    mClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
  }

  @Override
  public Response getAll(@Auth User access) {
    List<T> entities = this.dao.getAll();
    LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
    returnMap.put(getClassT().getSimpleName().toLowerCase() + 's', getMapListFromEntities(entities));
    return ResourceUtils.asSuccessResponse(Status.OK, returnMap);
  }

  @Override
  public Response get(@Auth User access, @PathParam("id") String id) {
    T entity = dao.get(id);
    if (entity != null) {
      return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity, access));
    }
    else {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Entity not found."));
    }
  }

  protected HashMap<String, Object> onGet(T entity, User access) {
    return getMapFromEntity(entity);
  }

  @Override
  public Response create(@Auth User access, HashMap<String, Object> inputMap) {
    Admin adminUser;
    RestaurantUser restaurantUser;
    Response response = null;
    T entity = getEntityForInsertion(inputMap);

    if (access.getRole() == UserRole.ADMIN) {
      adminUser = adminDao.getAdminById(access.getId().toString());
      entity.setCreatedBy(adminUser.getEmail());
    }
    else if (access.getRole() == UserRole.OWNER) {
      restaurantUser = restaurantUserDao.getRestaurantUserById(access.getId().toString());
      entity.setCreatedBy(restaurantUser.getEmail());
    }
    else if (access.getRole() == UserRole.CUSTOMER) {
      Customer customer = customerDao.getCustomerByID(access.getId().toString());
      entity.setCreatedBy(customer.getEmail());
    }
    entity.setCreatedDate(new Date());
    try {
      if (inputMap.containsKey("restaurantId")) {
        response = onCreate(access, entity, restaurantDao.get(inputMap.get("restaurantId").toString()));
      }
      else {
        response = onCreate(access, entity, null);
      }
    }
    catch (RollbackException e) {
      LOGGER.debug("Error creating entity.", e);
      response = ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(String.format("This entity is already exists.")));
    }
    catch (Exception e) {
      LOGGER.debug("Error creating entity.", e);
      response = ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Error creating entity: %s", e.getMessage())));
    }
    return response;
  }

  protected Response onCreate(User access, T entity, Restaurant restaurant) {
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @Override
  public Response update(@Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    T entity = dao.get(id);
    entity = getEntityForUpdate(entity, inputMap);
    entity.setModifiedBy(access.getEmail());
    Response response = null;
    try {
      dao.update(entity);
      return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
    }
    catch (RollbackException e) {
      LOGGER.debug("Error updating entity.", e);
      response = ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot update entity. Error message: %s", e.getMessage())));
    }
    catch (Exception e) {
      LOGGER.debug("Error updating entity.", e);
      return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot update entity. Error message: %s", e.getMessage())));
    }
    return response;
  }

  @Override
  public Response delete(@Auth User access, @PathParam("id") String id) {
    T entity = dao.get(id);
    if (entity != null) {
      try {
        dao.delete(entity);
        return ResourceUtils.asSuccessResponse(Status.OK, null);
      }
      catch (Exception e) {
        LOGGER.debug("Error deleting entity.", e);
        return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR,
            new ServiceErrorMessage(String.format("Cannot delete entity. Error message: %s", e.getMessage())));
      }
    }
    else {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Entity not found."));
    }
  }

  protected HashMap<String, Object> getMapFromEntity(T entity) {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
    return returnMap;
  }

  protected List<HashMap<String, Object>> getMapListFromEntities(Collection<T> entities) {
    return ModelHelpers.fromEntities(entities);
  }

  public Class<?> getClassT() {
    return mClass;
  }

  @SuppressWarnings("unchecked")
  protected T getEntityForInsertion(HashMap<String, Object> inputMap) {
    try {
      return (T) getClassT().newInstance();
    }
    catch (Exception e) {
      LOGGER.error("Error creating object.", e);
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  protected T getEntityForUpdate(T entity, HashMap<String, Object> inputMap) {
      return entity;
  }
}

