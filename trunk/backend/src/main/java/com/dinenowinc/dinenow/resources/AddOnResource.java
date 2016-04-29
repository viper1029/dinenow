package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.AddonDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Addon;
import com.dinenowinc.dinenow.model.AddonSize;
import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.helpers.ModelHelpers;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Path("/addons")
@Api("/addons")
public class AddonResource extends AbstractResource<Addon> {

  @Inject
  private AddonDao addonDao;

  @Inject
  private SizeDao sizeDao;

  @Inject
  private RestaurantDao restaurantDao;

  @GET
  @ApiOperation("get all addon of restaurant for ADMIN and OWNER")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Path("/restaurant/{restaurant_id}")
  @ApiOperation("Get All AddOns By Restaurant")
  @GET
  public Response getAddOnsByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
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
  @ApiOperation(value = "api add new add on for restaurant")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
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

    if (inputMap.containsKey("addonSize")) {
      ArrayList<AddonSize> addonSizes = new ArrayList<>();
      List<HashMap<String, Object>> addonSizeList = (List<HashMap<String, Object>>) inputMap.get("addonSize");
      for (HashMap<String, Object> addonSize : addonSizeList) {
        addonSizes.add(createNewAddonSize(addonSize, entity));
      }
      entity.addAllSize(addonSizes);
    }
    return entity;
  }

  private AddonSize createNewAddonSize(HashMap<String, Object> addonSize, Addon entity) {
    Size size = sizeDao.get(((HashMap<String, Object>) addonSize.get("size")).get("id").toString());
    double price = Double.parseDouble(addonSize.get("price").toString());
    AddonSize addonSizeEntity = new AddonSize();
    addonSizeEntity.setSize(size);
    addonSizeEntity.setPrice(price);
    addonSizeEntity.setAddon(entity);
    addonSizeEntity.setCreatedBy("system");
    addonSizeEntity.setCreatedDate(new Date());
    addonSizeEntity.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    return addonSizeEntity;
  }

  @PUT
  @ApiOperation(value = "update add on")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 404, message = "Add on not found"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
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

    if (inputMap.containsKey("addonSize")) {
      ArrayList<AddonSize> newAddonSizes = new ArrayList<>();
      ArrayList<AddonSize> keepExistingAddonSizes = new ArrayList<>();
      List<HashMap<String, Object>> addonSizeList = (List<HashMap<String, Object>>) inputMap.get("addonSize");
      for (HashMap<String, Object> addonSize : addonSizeList) {
        boolean foundExisting = false;
        if (addonSize.get("id") != null) {
          for (AddonSize existingAddonSize : entity.getAddonSize()) {
            if (existingAddonSize.getId().matches(addonSize.get("id").toString())) {
              existingAddonSize.setPrice(Double.parseDouble(addonSize.get("price").toString()));
              existingAddonSize.setAvailabilityStatus(AvailabilityStatus.valueOf(addonSize.get("availabilityStatus").toString()));
              if (!existingAddonSize.getSize().getId().matches(
                  ((HashMap<String, Object>) addonSize.get("size")).get("id").toString())) {
                existingAddonSize.setSize(sizeDao.get(((HashMap<String, Object>) addonSize.get("size")).get("id").toString()));
                existingAddonSize.setAddon(entity);
              }
              foundExisting = true;
              keepExistingAddonSizes.add(existingAddonSize);
              break;
            }
          }
        }
        if (!foundExisting) {
          newAddonSizes.add(createNewAddonSize(addonSize, entity));
        }
      }
      entity.getAddonSize().retainAll(keepExistingAddonSizes);
      entity.addAllSize(newAddonSizes);
    }
    return entity;
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
    dto.put("addonSize", ModelHelpers.fromEntities(entity.getAddonSize()));
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.put(getClassT().getSimpleName().toLowerCase(), dto);
    return returnMap;
  }
}