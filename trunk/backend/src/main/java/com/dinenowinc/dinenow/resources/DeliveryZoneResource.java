package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.helpers.DeliveryZoneType;
import com.dinenowinc.dinenow.model.helpers.ModelHelpers;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.validation.DeliveryZoneValidator;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
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
import java.util.LinkedHashMap;
import java.util.List;

@Path("/delivery_zones")
@Api("/delivery_zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryZoneResource extends AbstractResource<DeliveryZone> {

  @Inject
  private RestaurantDao restaurantDao;

  @Inject
  DeliveryZoneDao deliveryZoneDao;

  @GET
  @ApiOperation("api get all delivery zone of restaurant for ADMIN and OWNER")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, "access denied for user");
  }


  @Path("/restaurant/{restaurant_id}")
  @ApiOperation(value = "Get all deliver zones by restaurant", notes = "Not Implement")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "restaurant not found")
  })
  @GET
  public Response getDeliveryZonesByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      if (restaurantDao.get(restaurant_id) != null) {
        List<DeliveryZone> entities = deliveryZoneDao.getAllDeliveryZonesByRestaurantId(restaurant_id);
        List<HashMap<String, Object>> returnMap = ModelHelpers.fromEntities(entities);
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("deliveryzones", returnMap);
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
  @ApiOperation("api get detail of Delivery Zone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

  @POST
  @ApiOperation(value = "api add new delivery zone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      DeliveryZoneValidator deliveryZoneValidator = new DeliveryZoneValidator(deliveryZoneDao, inputMap);
      List<ServiceErrorMessage> mListError = deliveryZoneValidator.validateForAdd();
      if (mListError.size() == 0) {
        return super.create(access, inputMap);
      }
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, DeliveryZone entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addDeliveryZone(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected DeliveryZone getEntityForInsertion(HashMap<String, Object> inputMap) {
    DeliveryZone entity = super.getEntityForInsertion(inputMap);
    entity.setName(inputMap.get("name").toString());
    entity.setDescription(inputMap.get("description").toString());
    entity.setMinimum(Double.parseDouble(inputMap.get("minimum").toString()));
    entity.setFee(Double.parseDouble(inputMap.get("fee").toString()));
    entity.setType(DeliveryZoneType.valueOf(inputMap.get("type").toString()));
    List<LinkedHashMap<String, Double>> polygon = (List<LinkedHashMap<String, Double>>) inputMap.get("coordinates");
    Coordinate[] coordinates = new Coordinate[polygon.size()];
    for (int i = 0; i < polygon.size(); i++) {
      coordinates[i] = new Coordinate(polygon.get(i).get("lat"), polygon.get(i).get("lng"));
    }
    GeometryFactory gf = new GeometryFactory();
    entity.setCoordinates(gf.createPolygon(coordinates));
    return entity;
  }

  @PUT
  @ApiOperation(value = "api add update delivery zone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 400, message = "List Error"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      DeliveryZoneValidator deliveryZoneValidator = new DeliveryZoneValidator(deliveryZoneDao, inputMap);
      ServiceErrorValidationMessage mListError = deliveryZoneValidator.validateForUpdate();
      if (mListError.getErrors().size() == 0) {
        DeliveryZone deliveryZone = deliveryZoneDao.get(id);
        if (deliveryZone != null) {
          return super.update(access, id, inputMap);
        }
        else {
          return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Delivery Zone on not found."));
        }
      }
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected DeliveryZone getEntityForUpdate(DeliveryZone deliveryZone, HashMap<String, Object> inputMap) {
    deliveryZone.setName(inputMap.get("name").toString());
    deliveryZone.setDescription(inputMap.get("description").toString());
    deliveryZone.setMinimum(Double.parseDouble(inputMap.get("minimum").toString()));
    deliveryZone.setFee(Double.parseDouble(inputMap.get("fee").toString()));
    deliveryZone.setType(DeliveryZoneType.valueOf(inputMap.get("type").toString()));
    List<LinkedHashMap<String, Double>> polygon = (List<LinkedHashMap<String, Double>>) inputMap.get("coordinates");
    Coordinate[] coordinates = new Coordinate[polygon.size()];
    for (int i = 0; i < polygon.size(); i++) {
      coordinates[i] = new Coordinate(polygon.get(i).get("lat"), polygon.get(i).get("lng"));
    }
    GeometryFactory gf = new GeometryFactory();
    deliveryZone.setCoordinates(gf.createPolygon(coordinates));
    return deliveryZone;
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation("api delete delivery zone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "Cannot found entity")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }
}
