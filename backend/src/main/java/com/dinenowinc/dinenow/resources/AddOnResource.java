package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.ModelHelpers;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SizeInfo;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Path("/addons")
@Api("/addons")
public class AddOnResource extends AbstractResource<AddOn>{

	@Inject
	private AddOnDao addOnDao;

	@Inject
	private SizeDao sizeDao;

	@Inject 
	private RestaurantDao restaurantDao;

	@Override
	protected HashMap<String, Object> fromEntity(AddOn entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("name", entity.getAddOnName());
		dto.put("description", entity.getAddOnDescription());		
		dto.put("availabilityStatus", entity.getAvailabilityStatus());
		
		List<HashMap<String, Object>> sizes = new ArrayList<HashMap<String, Object>>();
		for (SizeInfo size : entity.getSizes()) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			temp.put("id", size.getSize().getId());
			temp.put("name", size.getSize().getSizeName());
			temp.put("price", size.getPrice());
			sizes.add(temp);
		}
		dto.put("sizePrices", sizes);
		HashMap<String, Object> idto = new HashMap<String, Object>();
		idto.put(getClassT().getSimpleName().toLowerCase(), dto);
		return idto;
	}


	//	@SuppressWarnings("unchecked")
	//	@Override
	//	protected AddOn fromFullDto(HashMap<String, Object> dto) {
	//		AddOn entity = super.fromFullDto(dto);
	//		entity.setAddOnName(dto.get("addOnName").toString());
	//		entity.setAddOnDescription(dto.get("addOnDescription").toString());
	//		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(dto.get("availabilityStatus").toString()));
	//		
	//		if (dto.containsKey("sizePrices")) {
	//			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) dto.get("sizePrices");
	//			for (HashMap<String, Object> hashMap : listSizePrice) {
	//				String iSize = hashMap.get("size").toString();
	//				double price = Double.parseDouble(hashMap.get("price").toString());
	//				SizePrice sizePrice = new SizePrice();
	//				sizePrice.setPrice(price);
	//				Size size = sizeDao.findOne(iSize);
	//				sizePrice.setSize(size);
	//				//entity.addSizePrices(sizePrice);
	//			}
	//		}
	//		return entity;
	//	}


	@SuppressWarnings("unchecked")
	@Override
	protected AddOn fromAddDto(HashMap<String, Object> dto) {
		AddOn entity = super.fromAddDto(dto);

		entity.setAddOnName(dto.get("name").toString());
		entity.setAddOnDescription(dto.get("description").toString());
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(dto.get("availabilityStatus").toString()));

		if (dto.containsKey("sizePrices")) {
			ArrayList<SizeInfo> listsInfo = new ArrayList<SizeInfo>();

			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) dto.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.findOne(hashMap.get("size").toString());
				double price = Double.parseDouble(hashMap.get("price").toString());
				SizeInfo sInfo = new SizeInfo();
				sInfo.setSize(s);
				sInfo.setPrice(price);
				listsInfo.add(sInfo);
			}
			entity.addAllSize(listsInfo);
		}
		return entity;
	}



	@SuppressWarnings("unchecked")
	@Override
	protected AddOn fromUpdateDto(AddOn t, HashMap<String, Object> dto) {
		AddOn entity = super.fromUpdateDto(t, dto);
		entity.setAddOnName(dto.get("name").toString());
		entity.setAddOnDescription(dto.get("description").toString());
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(dto.get("availabilityStatus").toString()));

		if (dto.containsKey("sizePrices")) {
			ArrayList<SizeInfo> listsInfo = new ArrayList<SizeInfo>();

			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) dto.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.findOne(hashMap.get("size").toString());
				double price = Double.parseDouble(hashMap.get("price").toString());
				SizeInfo sInfo = new SizeInfo();
				sInfo.setSize(s);
				sInfo.setPrice(price);
				listsInfo.add(sInfo);

			}
			entity.addAllSize(listsInfo);

		}
		return entity;
	}



	//==============================ACTION====================================//


/*	@Override
	protected HashMap<String, Object> onGet(AddOn entity) {

		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto = super.onGet(entity);
		//		ArrayList<HashMap<String, Object>> listSizePrice = new ArrayList<HashMap<String, Object>>();
		//		for (SizePrice size_price : entity.getSizePrices()) {
		//			HashMap<String, Object> obj = new HashMap<String, Object>();
		//			obj.put("id", size_price.getId());
		//			obj.put("size", size_price.getSize().getId());
		//			obj.put("price", size_price.getPrice());
		//			listSizePrice.add(obj);
		//		}
		//		dto.put("sizePrices", listSizePrice);

		ArrayList<HashMap<String, Object>> listAddOn = new ArrayList<HashMap<String, Object>>();
		for (SizeInfo sInfo : entity.getSizes()) {
			HashMap<String, Object> en = new HashMap<String, Object>();
			en.put("size", sInfo.getSize().getId());
			en.put("price", sInfo.getPrice());
			listAddOn.add(en);
		}
		dto.put("sizePrices", listAddOn);


		return dto;
	}*/




	@Override
	protected Response onAdd(AccessToken access, AddOn entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		restaurant.addAddOns(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));
	}

	@Override
	protected Response onUpdate(AccessToken access, AddOn entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));	
	}

	@Override
	protected Response onDelete(AccessToken access,AddOn entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED, new ServiceErrorMessage("has relationship"));
		}
	}



	//============================METHOD=====================================//

	@GET
	@ApiOperation("api get all addon of restaurant for ADMIN and OWNER")
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
	@ApiOperation("api get detail of add on")
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
			+ "<br/>  \"restaurantId\":\"18c8fed0-a7bc-4887-9951-990e829285db\","
			+ "<br/>  \"name\": \"add on 1\","
			+ "<br/>  \"description\": \"add on description 1\","
			+ "<br/>  \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>  \"sizePrices\": ["
			+ "<br/>    {"
			+ "<br/>      \"size\": \"18481583-4518-47a7-b398-9fc88200a0ef\","
			+ "<br/>      \"price\": 67"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}"
			+ "<br/>"
			+ "</code></pre><br/>if select All size containt all id")
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
			+ "<br/>    \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>    \"description\": \"add on description edited\","
			+ "<br/>    \"name\": \"add on\""
			+ "<br/>  }</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"name\": \"add on 1\","
			+ "<br/>  \"description\": \"add on description 1\","
			+ "<br/>  \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>  \"sizePrices\": ["
			+ "<br/>    {"
			+ "<br/>      \"size\": \"84fd0fd9-65b4-42a4-b83e-967d52577712\","
			+ "<br/>      \"price\": 101"
			+ "<br/>    }"
			+ "<br/>  ]"
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
			AddOn addon = addOnDao.findOne(id);
			if (addon != null) {
				return super.update(access, id, dto);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
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
			AddOn addon = addOnDao.findOne(id);
			if (addon != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

}
