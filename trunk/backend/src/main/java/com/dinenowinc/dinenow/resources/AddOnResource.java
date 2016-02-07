package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.AddOnDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.AddOnSize;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SizeInfo;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.UserRole;
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
public class AddOnResource extends AbstractResource<AddOn>{

	@Inject
	private AddOnDao addOnDao;

	@Inject
	private SizeDao sizeDao;

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
	@ApiOperation(value="api add new add on for restaurant")
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

	@PUT
	@ApiOperation(value="update add on")
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
			AddOn addon = addOnDao.get(id);
			if (addon != null) {
				return super.update(access, id, inputMap);
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
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

	@Override
	protected HashMap<String, Object> getMapFromEntity(AddOn entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("name", entity.getAddOnName());
		dto.put("description", entity.getAddOnDescription());		
		dto.put("availabilityStatus", entity.getAvailabilityStatus());
		
		List<HashMap<String, Object>> sizes = new ArrayList<HashMap<String, Object>>();
		for (AddOnSize size : entity.getSizes()) {
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

	@SuppressWarnings("unchecked")
	@Override
	protected AddOn getEntityForInsertion(HashMap<String, Object> inputMap) {
		AddOn entity = super.getEntityForInsertion(inputMap);

		entity.setAddOnName(inputMap.get("name").toString());
		entity.setAddOnDescription(inputMap.get("description").toString());
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(inputMap.get("availabilityStatus").toString()));

		if (inputMap.containsKey("sizePrices")) {
			ArrayList<AddOnSize> listsInfo = new ArrayList<AddOnSize>();

			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) inputMap.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.get(hashMap.get("size").toString());
				double price = Double.parseDouble(hashMap.get("price").toString());
        AddOnSize sInfo = new AddOnSize();
				sInfo.setSize(s);
				sInfo.setPrice(price);
        sInfo.setAddon(entity);
        sInfo.setCreatedBy("system");
        sInfo.setCreatedDate(new Date());
        sInfo.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
				listsInfo.add(sInfo);
			}
			entity.addAllSize(listsInfo);
		}
		return entity;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected AddOn getEntityForUpdate(AddOn entity, HashMap<String, Object> inputMap) {
		entity.setAddOnName(inputMap.get("name").toString());
		entity.setAddOnDescription(inputMap.get("description").toString());
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(inputMap.get("availabilityStatus").toString()));

		if (inputMap.containsKey("sizePrices")) {
			ArrayList<AddOnSize> listsInfo = new ArrayList<AddOnSize>();

			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) inputMap.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.get(hashMap.get("size").toString());
				double price = Double.parseDouble(hashMap.get("price").toString());
        AddOnSize sInfo = new AddOnSize();
				sInfo.setSize(s);
				sInfo.setPrice(price);
        sInfo.setAddon(entity);
				listsInfo.add(sInfo);

			}
			entity.addAllSize(listsInfo);

		}
		return entity;
	}


	@Override
	protected Response onCreate(User access, AddOn entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		restaurant.addAddOns(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, entity);
	}
}
