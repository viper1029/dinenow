package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.AddonDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Addon;
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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Path("/addons")
@Api("/addons")
public class AddonResource extends AbstractResource<Addon> {

  @Inject
  private AddonDao addonDao;

  @Inject
  private RestaurantDao restaurantDao;

  @GET
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Path("/restaurant/{restaurant_id}")
  @GET
  public Response getAddOnsByRestaurantId(@ApiParam(access = "internal") @Auth User access,
      @PathParam("restaurant_id") String restaurant_id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      if (restaurantDao.get(restaurant_id) != null) {
        List<Addon> entities = addonDao.getAddonsByRestaurantId(restaurant_id);
        List<HashMap<String, Object>> returnMap = ModelHelpers.fromEntities(entities);
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("addons", returnMap);
        return ResourceUtils.asSuccessResponse(Status.OK, dto);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found."));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @GET
  @Path("/{id}")
  @ApiOperation("get detail of add on")
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
  @ApiOperation(value = "Create addon for a restaurant")
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Addon entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addAddOns(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, entity);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Addon getEntityForInsertion(HashMap<String, Object> inputMap) {
    Addon entity = super.getEntityForInsertion(inputMap);
    entity.setAddonName(inputMap.get("name").toString());
    entity.setAddonDescription(inputMap.get("description").toString());
    entity.setAddonPrice(Double.parseDouble(inputMap.get("price").toString()));
    return entity;
  }

  @PUT
  @ApiOperation(value = "Update an addon")
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Addon addon = addonDao.get(id);
      if (addon != null) {
        return super.update(access, id, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Addon getEntityForUpdate(Addon entity, HashMap<String, Object> inputMap) {
    entity.setAddonName(inputMap.get("name").toString());
    entity.setAddonDescription(inputMap.get("description").toString());
    entity.setAddonPrice(Double.parseDouble(inputMap.get("price").toString()));
    return entity;
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation("Delete addon by id")
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected HashMap<String, Object> getMapFromEntity(Addon entity) {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", entity.getId());
    dto.put("name", entity.getAddonName());
    dto.put("description", entity.getAddonDescription());
    dto.put("price", entity.getAddonPrice());
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.put(getClassT().getSimpleName().toLowerCase(), dto);
    return returnMap;
  }
}