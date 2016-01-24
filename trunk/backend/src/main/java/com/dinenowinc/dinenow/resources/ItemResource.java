package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



import java.util.LinkedHashMap;
import java.util.List;

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

import org.apache.commons.lang.RandomStringUtils;

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.*;
import com.dinenowinc.dinenow.validation.ItemValidator;
import com.google.inject.Inject;
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import static java.util.Collections.singletonMap;

@Path("/items")
@Api("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource extends AbstractResource<Item> {	

	@Inject
	private CategoryDao categoryDao;

	@Inject
	private ItemDao itemDao;
	@Inject
	private SizeDao sizeDao;
	@Inject
	private ModifierDao modifierDao;
	@Inject
	private RestaurantDao restaurantDao;

	@Override
	protected HashMap<String, Object> fromEntity(Item entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("name", entity.getItemName());
		dto.put("description", entity.getItemDescription());
		dto.put("notes", entity.getNotes());
		dto.put("isVegeterian", entity.isVegeterian());
		dto.put("spiceLevel", entity.getSpiceLevel());
		dto.put("linkImage", entity.getLinkImage());
		dto.put("availabilityStatus", entity.getAvailabilityStatus());
		dto.put("spiceLevel", entity.getSpiceLevel());

		List<HashMap<String, Object>> sizes = new ArrayList<HashMap<String, Object>>();
		for (SizeInfo size : entity.getSizes()) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			temp.put("id", size.getSize().getId());
			temp.put("name", size.getSize().getSizeName());
			temp.put("price", size.getPrice());
			sizes.add(temp);
		}
		dto.put("sizePrices", sizes);
		List<HashMap<String, Object>> mod = new ArrayList<HashMap<String, Object>>();
		for (ModifierInfo modi : entity.getModifiers()) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			temp.put("id", modi.getModifier().getId());
			temp.put("name", modi.getModifier().getModifierName());
			mod.add(temp);
		}
		dto.put("modifiers", mod);
		HashMap<String, Object> rdto = new LinkedHashMap<String, Object>();
		rdto.put(getClassT().getSimpleName().toLowerCase(), dto);
		return rdto;
	}




	@SuppressWarnings("unchecked")
	@Override
	protected Item fromAddDto(HashMap<String, Object> dto) {
		Item entity = super.fromAddDto(dto);
		entity.setItemName(dto.get("name").toString());
		entity.setItemDescription(dto.get("description").toString());
		entity.setLinkImage(dto.get("linkImage") == null ? "" : dto.get("linkImage").toString());
		entity.setNotes(dto.get("notes").toString());
		entity.setVegeterian(Boolean.parseBoolean(dto.get("isVegeterian").toString()));
		entity.setSpiceLevel(Integer.parseInt(dto.get("spiceLevel").toString()));
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(dto.get("availabilityStatus").toString()));
		//entity.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
	//	if (dto.containsKey("sizePrices")) {
			ArrayList<SizeInfo> listsInfo = new ArrayList<SizeInfo>();
			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) dto.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.findOne(hashMap.get("size").toString());
				if (s != null) {
					double price = Double.parseDouble(hashMap.get("price").toString());
					SizeInfo sInfo = new SizeInfo();
					sInfo.setCreatedBy("item");
					sInfo.setCreatedDate(new Date());
					sInfo.setSize(s);
					sInfo.setPrice(price);
					listsInfo.add(sInfo);
				}
			}
			entity.addAllSize(listsInfo);
	//	}
		if (dto.containsKey("modifiers")) {
			ArrayList<ModifierInfo> listmInfo = new ArrayList<ModifierInfo>();
			List<HashMap<String, Object>> listModifier = (List<HashMap<String, Object>>) dto.get("modifiers");
			for (HashMap<String, Object> hashMap : listModifier) {
				Modifier modifies = modifierDao.findOne(hashMap.get("modifier").toString());
				if (modifies != null) {
					ModifierInfo mInfo = new ModifierInfo();
					if (hashMap.containsKey("availabilityStatus")) {
						mInfo.setAvailabilityStatus(AvailabilityStatus.valueOf(hashMap.get("availabilityStatus").toString()));
					}else {
						mInfo.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
					}
					mInfo.setModifier(modifies);
					mInfo.setCreatedBy("self");
					mInfo.setCreatedDate(new Date());
					listmInfo.add(mInfo);
				}
			}
			entity.addAllModifier(listmInfo);
		}
		return entity;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Item fromUpdateDto(Item t, HashMap<String, Object> dto) {
		Item entity = super.fromUpdateDto(t, dto);
		entity.setItemName(dto.get("name").toString());
		entity.setItemDescription(dto.get("description").toString());
		entity.setLinkImage(dto.get("linkImage") == null ? "" : dto.get("linkImage").toString());
		entity.setNotes(dto.get("notes").toString());
		entity.setVegeterian(Boolean.parseBoolean(dto.get("isVegeterian").toString()));
		entity.setSpiceLevel(Integer.parseInt(dto.get("spiceLevel").toString()));
		entity.setAvailabilityStatus(AvailabilityStatus.valueOf(dto.get("availabilityStatus").toString()));
		entity.setSpiceLevel(Integer.parseInt(dto.get("spiceLevel").toString()));
		//entity.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
		if (dto.containsKey("sizePrices")) {
			ArrayList<SizeInfo> listsInfo = new ArrayList<SizeInfo>();
			List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) dto.get("sizePrices");
			for (HashMap<String, Object> hashMap : listSizePrice) {
				Size s = sizeDao.findOne(hashMap.get("size").toString());
				if (s != null) {
					double price = Double.parseDouble(hashMap.get("price").toString());
					SizeInfo sInfo = new SizeInfo();
					sInfo.setCreatedBy("self");
					sInfo.setCreatedDate(new Date());
					sInfo.setSize(s);
					sInfo.setPrice(price);
					listsInfo.add(sInfo);
				}
			}
			entity.addAllSize(listsInfo);
		}
		if (dto.containsKey("modifiers")) {
			ArrayList<ModifierInfo> listmInfo = new ArrayList<ModifierInfo>();
			List<HashMap<String, Object>> listModifier = (List<HashMap<String, Object>>) dto.get("modifiers");
			for (HashMap<String, Object> hashMap : listModifier) {
				Modifier modifies = modifierDao.findOne(hashMap.get("modifier").toString());
				if (modifies != null) {
					ModifierInfo mInfo = new ModifierInfo();
					if (hashMap.containsKey("availabilityStatus")) {
						mInfo.setAvailabilityStatus(AvailabilityStatus.valueOf(hashMap.get("availabilityStatus").toString()));
					}else {
						mInfo.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
					}
					mInfo.setModifier(modifies);
					mInfo.setCreatedBy("self");
					mInfo.setCreatedDate(new Date());
					listmInfo.add(mInfo);
				}
			}
			entity.addAllModifier(listmInfo);
		}

		return entity;
	}





	//================================ACTION================================//

/*	@Override
	protected HashMap<String, Object> onGet(Item entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto = super.onGet(entity);
		ArrayList<HashMap<String, Object>> listAddOn = new ArrayList<HashMap<String, Object>>();
		for (SizeInfo sInfo : entity.getSizes()) {
			HashMap<String, Object> en = new LinkedHashMap<String, Object>();
			en.put("size", sInfo.getSize().getId());
			en.put("name", sInfo.getSize().getSizeName());
			en.put("price", sInfo.getPrice());
			listAddOn.add(en);
		}
		dto.put("sizePrices", listAddOn);
		ArrayList<HashMap<String, Object>> listModifiers = new ArrayList<HashMap<String, Object>>();
		for (ModifierInfo mInfo : entity.getModifiers()) {
			HashMap<String, Object> en = new LinkedHashMap<String, Object>();
			en.put("name", mInfo.getModifier().getModifierName());
			en.put("modifier", mInfo.getModifier().getId());
			en.put("availabilityStatus", mInfo.getAvailabilityStatus());
			listModifiers.add(en);
		}
		dto.put("modifiers", listModifiers);

		return dto;
	}*/

	@Override
	protected Response onAdd(User access, Item entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		entity.setCompositeId(restaurant.getId());
		restaurant.addItem(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));
	}


	@Override
	protected Response onUpdate(User access, Item entity, Restaurant restaurant) {
		//entity.setCompositeId(restaurant.getId());
		restaurant = restaurantDao.findByItemId(entity.getId());
		entity.setCompositeId(restaurant.getId());
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity));
	}

	@Override
	protected Response onDelete(User access,Item entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED, new ServiceErrorMessage("has relationship"));
		}
	}



	//=============================METHOD=============================//

	@GET
	@ApiOperation("api get all items of restaurant for ADMIN and OWNER")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user")
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
//		if (access.getRole() == UserRole.OWNER) {
//			List<Item> entities = this.itemDao.getListByUser(access);
//			List<HashMap<String, Object>> dtos = fromEntities(entities);
//			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}




	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of items")
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
	@ApiOperation(value="api add new items for restaurant OWNER", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"a85b2e3d-1511-47fa-886e-9d81e4436d3e\","
			+ "<br/>  \"name\": \"item 2 test\","
			+ "<br/>  \"description\": \"item test\","
			+ "<br/>  \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>  \"isVegeterian\": false,"
			+ "<br/>  \"spiceLevel\": 3,"
			+ "<br/>  \"notes\": \"notes\","
			+ "<br/>  \"isShowSpice\": false,"
			+ "<br/>  \"orderType\": \"ALL\","
			+ "<br/>  \"keywords\": ["
			+ "<br/>    \"abc\","
			+ "<br/>    \"abc\""
			+ "<br/>  ],"
			+ "<br/>  \"linkImage\": \"adbc2321312.jpg\","
			+ "<br/>  \"price\": 34,"
			+ "<br/>  \"sizePrices\": ["
			+ "<br/>    {"
			+ "<br/>      \"price\": 50,"
			+ "<br/>      \"size\": \"12180d97-c26f-434a-8960-045e9a76b161\""
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"modifiers\": ["
			+ "<br/>    {"
			+ "<br/>      \"modifier\": \"a8a7c07a-0562-452b-8967-eec2777718cb\""
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
    /*
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			ItemValidator mItemValidator = new ItemValidator(itemDao, dto);
			List<ServiceErrorMessage> mListError = mItemValidator.validateForAdd();
			if (mListError.size() == 0) {
				return super.add(access, dto);
			}
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		*/
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

  /*
	@PUT
	@ApiOperation(value="api update items for restaurant OWNER", notes="<pre><code>{"
			+ "<br/>  \"name\": \"item 2 test\","
			+ "<br/>  \"description\": \"item test\","
			+ "<br/>  \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>  \"isVegeterian\": false,"
			+ "<br/>  \"spiceLevel\": 3,"
			+ "<br/>  \"notes\": \"notes\","
			+ "<br/>  \"isShowSpice\": false,"
			+ "<br/>  \"orderType\": \"ALL\","
			+ "<br/>  \"keywords\": ["
			+ "<br/>    \"abc\","
			+ "<br/>    \"abc\""
			+ "<br/>  ],"
			+ "<br/>  \"linkImage\": \"adbc2321312.jpg\","
			+ "<br/>  \"price\": 34,"
			+ "<br/>  \"sizePrices\": ["
			+ "<br/>    {"
			+ "<br/>      \"price\": 50,"
			+ "<br/>      \"size\": \"12180d97-c26f-434a-8960-045e9a76b161\""
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"modifiers\": ["
			+ "<br/>    {"
			+ "<br/>      \"modifier\": \"a8a7c07a-0562-452b-8967-eec2777718cb\""
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 404, message = "Item not found"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Item item = itemDao.findOne(id);
			if (item != null) {
				ItemValidator mItemValidator = new ItemValidator(itemDao, dto);
				List<ServiceErrorMessage> mListError = mItemValidator.validateForAdd();
				if (mListError.size() == 0) {
					return super.update(access, id, dto);
				}
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Item not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}
	*/
	@DELETE
	@ApiOperation("api delete items")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "Item not found"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity")
			})
	@Path("/{id}")
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Item item = itemDao.findOne(id);
			if (item != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Item not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}


/*
	//Image Items
	@POST
	@Path("/{id}/images")
	@ApiOperation(value="api upload image item",notes="POST file=XXXXXXXXXXXXXXXXX")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "image"),
			@ApiResponse(code = 404, message = "restaurant not found"),
			@ApiResponse(code = 404, message = "item not found"),
			@ApiResponse(code = 404, message = "file not found"),
			})
	@ApiImplicitParams(@ApiImplicitParam(
            dataType = "file",
            name = "file",
            value="Certificate file",
            paramType = "body"))
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@PathParam("id") String id, @ApiParam(access = "internal") @FormDataParam("file") InputStream uploadedInputStream, @ApiParam(access = "internal") @FormDataParam("file") FormDataContentDisposition fileDetail)  {
			Item item = itemDao.findOne(id);
			if (item != null) {
				try {
					java.nio.file.Path photoPath = Paths.get(
							System.getProperty("user.dir"), "photos");
					if (!photoPath.toFile().exists()) {
						photoPath.toFile().mkdir();
					}
					String fileName = String.format("%s.%s",
							RandomStringUtils.randomAlphanumeric(25), "jpg");
					java.nio.file.Path filePath = Paths.get(
							System.getProperty("user.dir"), "photos", fileName);

					// save it
					writeToFile(uploadedInputStream, filePath.toString());
					return ResourceUtils.asSuccessResponse(Status.OK,
							singletonMap("link", fileName));
				} catch (Exception e) {
					return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
				}
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("item not found"));
			}
	}
*/
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//Get Image
	@GET
	@Path("/{id}/images/{fileName}")
	@ApiOperation(value="api get image items", notes="http://192.168.1.66:30505/api/{restaurant_id}/items/{item_id}/images/ZVPgntuvC6YakayFBtPyvqPVH.jpg")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "image"),
			@ApiResponse(code = 404, message = "restaurant not found"),
			@ApiResponse(code = 404, message = "item not found"),
			@ApiResponse(code = 404, message = "file not found"),
			})
	@Produces("image/jpg")
	public Response getImage( @PathParam("id") String id, @PathParam("fileName") String fileName){
			Item item = itemDao.findOne(id);
			if (item != null) {
				java.nio.file.Path photoPath = Paths.get(System.getProperty("user.dir"), "photos",fileName);
				return returnFile(photoPath.toFile());
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("item not found"));
			}
	}

	private Response returnFile(File file) {
	    if (!file.exists()) {
	        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "file not found");
	    }
	    try {
	        return Response.ok(new FileInputStream(file)).build();
	    } catch (FileNotFoundException e) {
	    	return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "file not found");
	    }
	}
}
