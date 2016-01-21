package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceErrorValidationMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.DeliveryZoneType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.validation.DeliveryZoneValidator;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
/**
 * @author Laptop-02
 *
 */
@Path("/deliver_zones")
@Api("/deliver_zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryZoneResource extends AbstractResource<DeliveryZone>{

	@Inject 
	private RestaurantDao restaurantDao;
	@Inject DeliveryZoneDao deliveryZoneDao;
	
	
	@Override
	protected HashMap<String, Object> fromEntity(DeliveryZone entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
/*		dto.put("id", entity.getId());
		dto.put("name", entity.getName());
		dto.put("description", entity.getDescription());
		dto.put("minimum", entity.getMinimum());
		dto.put("fee", entity.getFee());
		dto.put("type", entity.getType());
		List<LatLng> coords = new ArrayList<LatLng>();
		for (int i = 0; i < entity.getCoordinates().getCoordinates().length; i++) {
			Coordinate  coord = entity.getCoordinates().getCoordinates()[i];
			LatLng latlng = new LatLng(coord.x, coord.y);
			coords.add(latlng);
		}
		dto.put("coordinates", coords);*/
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected DeliveryZone fromFullDto(HashMap<String, Object> dto) {
		DeliveryZone entity = super.fromFullDto(dto);
		entity.setName(dto.get("name").toString());
		entity.setDescription(dto.get("description").toString());
		entity.setMinimum(Double.parseDouble(dto.get("minimum").toString()));
		entity.setFee(Double.parseDouble(dto.get("fee").toString()));
		entity.setType(DeliveryZoneType.valueOf(dto.get("type").toString()));		
		/*
		HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("deliveryZoneCoords");		
		double lat = loca.get("lat");
		double lng = loca.get("lng");
		entity.setLat(lat);
		entity.setLng(lng);
		*/
		List<LinkedHashMap<String,Double>> polygon = (List<LinkedHashMap<String,Double>>)dto.get("coordinates");
		Coordinate[] coordinates = new Coordinate[polygon.size()];
		for (int i = 0; i < polygon.size(); i++) {
			coordinates[i] = new Coordinate(polygon.get(i).get("lat"), polygon.get(i).get("lng"));
		}
		GeometryFactory gf = new GeometryFactory();
		entity.setCoordinates(gf.createPolygon(coordinates));
		return entity;
	}
	
	
	
	/**
	 * lat long need at least 3 and last one as same as first
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected DeliveryZone fromAddDto(HashMap<String, Object> dto) {
		DeliveryZone entity = super.fromAddDto(dto);
		entity.setName(dto.get("name").toString());
		entity.setDescription(dto.get("description").toString());
		entity.setMinimum(Double.parseDouble(dto.get("minimum").toString()));
		entity.setFee(Double.parseDouble(dto.get("fee").toString()));
		entity.setType(DeliveryZoneType.valueOf(dto.get("type").toString()));
		/*
		HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("deliveryZoneCoords");		
		double lat = loca.get("lat");
		double lng = loca.get("lng");
		entity.setLat(lat);
		entity.setLng(lng);
		*/
	

		List<LinkedHashMap<String,Double>> polygon = (List<LinkedHashMap<String,Double>>)dto.get("coordinates");
		Coordinate[] coordinates = new Coordinate[polygon.size()];
		for (int i = 0; i < polygon.size(); i++) {
			coordinates[i] = new Coordinate(polygon.get(i).get("lat"), polygon.get(i).get("lng"));
		}
		GeometryFactory gf = new GeometryFactory();
		entity.setCoordinates(gf.createPolygon(coordinates));
		
		System.out.println("::::::::::::::::::::::::::::::::::::");
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected DeliveryZone fromUpdateDto(DeliveryZone t, HashMap<String, Object> dto) {
		DeliveryZone entity = super.fromUpdateDto(t, dto);
		
		entity.setName(dto.get("name").toString());
		entity.setDescription(dto.get("description").toString());
		entity.setMinimum(Double.parseDouble(dto.get("minimum").toString()));
		entity.setFee(Double.parseDouble(dto.get("fee").toString()));
		entity.setType(DeliveryZoneType.valueOf(dto.get("type").toString()));
		/*
		HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("deliveryZoneCoords");		
		double lat = loca.get("lat");
		double lng = loca.get("lng");
		entity.setLat(lat);
		entity.setLng(lng);
		*/
		List<LinkedHashMap<String,Double>> polygon = (List<LinkedHashMap<String,Double>>)dto.get("coordinates");
		Coordinate[] coordinates = new Coordinate[polygon.size()];
		for (int i = 0; i < polygon.size(); i++) {
			coordinates[i] = new Coordinate(polygon.get(i).get("lat"), polygon.get(i).get("lng"));
		}
		GeometryFactory gf = new GeometryFactory();
		entity.setCoordinates(gf.createPolygon(coordinates));
		return entity;
	}
	
	
	
	
	
	
	
	//==============================ACTION====================================//
	@Override
	protected Response onAdd(AccessToken access, DeliveryZone entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		restaurant.addDeliveryZone(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	@Override
	protected Response onUpdate(AccessToken access, DeliveryZone entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));	
	}
	
	@Override
	protected Response onDelete(AccessToken access, DeliveryZone entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED,"has relationship");
		}
	}
	
	
	
	
	
	//===========================METHOD=================================//
	
	
	
	@GET
	@ApiOperation("api get all delivery zone of restaurant for ADMIN and OWNER")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") 
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
//		if (access.getRole() == UserRole.OWNER) {
//			List<DeliveryZone> entities = deliveryZoneDao.getListByUser(access);		
//			List<HashMap<String, Object>> dtos = fromEntities(entities);
//			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, "access denied for user");
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
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access,@PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user")); 
	}
	
	
	@POST
	@ApiOperation(value="api add new delivery zone", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"2100d678-83c5-4c6f-9c0b-5a9584bbf7f3\","
			+ "<br/>  \"deliveryZoneName\": \"Zone delivery\","
			+ "<br/>  \"deliveryZoneDescription\": \"description\","
			+ "<br/>  \"orderMinimun\": 34,"
			+ "<br/>  \"deliveryFee\": 67,"
			+ "<br/>  \"deliveryZoneType\": \"CUSTOM\","
			+ "<br/>  \"deliveryZoneCoords\": ["
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.796507,"
			+ "<br/>      \"lng\": 106.64822"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.782511,"
			+ "<br/>      \"lng\": 106.644572"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.803926,"
			+ "<br/>      \"lng\": 106.673926"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.796507,"
			+ "<br/>      \"lng\": 106.64822"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		DeliveryZoneValidator deliveryZoneValidator = new DeliveryZoneValidator(deliveryZoneDao, dto);
		List<ServiceErrorMessage> mListError = deliveryZoneValidator.validateForAdd();
		if (mListError.size() == 0) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
	}
	
	
	@PUT
	@ApiOperation(value="api add update delivery zone", notes="<pre><code>{"
			+ "<br/>  \"deliveryZoneName\": \"Zone delivery\","
			+ "<br/>  \"deliveryZoneDescription\": \"description\","
			+ "<br/>  \"orderMinimun\": 34,"
			+ "<br/>  \"deliveryFee\": 67,"
			+ "<br/>  \"deliveryZoneType\": \"CUSTOM\","
			+ "<br/>  \"deliveryZoneCoords\": ["
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.796507,"
			+ "<br/>      \"lng\": 106.64822"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.782511,"
			+ "<br/>      \"lng\": 106.644572"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.803926,"
			+ "<br/>      \"lng\": 106.673926"
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"lat\": 10.796507,"
			+ "<br/>      \"lng\": 106.64822"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 400, message = "List Error"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		DeliveryZoneValidator deliveryZoneValidator = new DeliveryZoneValidator(deliveryZoneDao, dto);
		ServiceErrorValidationMessage mListError = deliveryZoneValidator.validateForUpdate();
		if (mListError.getErrors().size() == 0) {
			return super.update(access, id, dto);
		}
		return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
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
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		return super.delete(access, id);
	}
	
	
	@OPTIONS
	@ApiOperation(value="Get List By Restaurant-Id", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"aa27652f-864c-4a2a-b44d-a1ccc3be8b36\","
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	public Response getAllByRestaurantID(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		if (!dto.containsKey("restaurantId")) {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
		else if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			String res_id=dto.get("restaurantId").toString();		
			List<DeliveryZone> entities = deliveryZoneDao.getAllDeliveryZonesByRestaurantId(res_id);
			List<HashMap<String, Object>> dtos = fromEntities(entities);
			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));	
	}
	
}
