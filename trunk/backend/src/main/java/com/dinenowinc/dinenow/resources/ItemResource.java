package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.AddonDao;
import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.helpers.ModelHelpers;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.validation.ItemValidator;
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.*;
import com.google.inject.Inject;
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
  private AddonDao addonDao;

  @Inject
  private ModifierDao modifierDao;

  @Inject
  private RestaurantDao restaurantDao;

  @GET
  @ApiOperation("api get all items of restaurant for ADMIN and OWNER")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user")
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
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
  @ApiOperation(value = "api add new items")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      ItemValidator itemValidator = new ItemValidator(inputMap);
      List<ServiceErrorMessage> errorMessages = itemValidator.validateForCreation();
      if (errorMessages.size() == 0) {
        return super.create(access, inputMap);
      }
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, errorMessages);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Item entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addItem(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity, access));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Item getEntityForInsertion(HashMap<String, Object> inputMap) {
    Item entity = super.getEntityForInsertion(inputMap);
    entity.setName(inputMap.get("name").toString());
    entity.setDescription(inputMap.get("description") == null ? "" : inputMap.get("description").toString());
    entity.setImage(inputMap.get("image") == null ? "" : inputMap.get("image").toString());
    entity.setNotes(inputMap.get("notes") == null ? "" : inputMap.get("notes").toString());

    if (inputMap.containsKey("itemSize")) {
      ArrayList<ItemSize> itemSizes = new ArrayList<>();
      List<HashMap<String, Object>> itemSizeList = (List<HashMap<String, Object>>) inputMap.get("itemSize");
      for (HashMap<String, Object> itemSize : itemSizeList) {
        itemSizes.add(createNewItemSize(itemSize, entity));
      }
      entity.addAllItemSize(itemSizes);
    }

    if (inputMap.containsKey("modifiers")) {
      ArrayList<Modifier> modifiers = new ArrayList<>();
      List<HashMap<String, Object>> modifierList = (List<HashMap<String, Object>>) inputMap.get("modifiers");
      for (HashMap<String, Object> modifier : modifierList) {
        modifiers.add(modifierDao.get(modifier.get("id").toString()));
      }
      entity.addAllModifier(modifiers);
    }
    return entity;
  }

  private ItemSize createNewItemSize(HashMap<String, Object> itemSize, Item entity) {
    Size size = sizeDao.get(((HashMap<String, Object>) itemSize.get("size")).get("id").toString());
    double price = Double.parseDouble(itemSize.get("price").toString());
    ItemSize itemSizeEntity = new ItemSize();
    itemSizeEntity.setSize(size);
    itemSizeEntity.setPrice(price);
    itemSizeEntity.setItem(entity);
    itemSizeEntity.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    itemSizeEntity.setCreatedBy("system");
    itemSizeEntity.setCreatedDate(new Date());
    return itemSizeEntity;
  }


  @SuppressWarnings("unchecked")
  @Override
  protected Item getEntityForUpdate(Item item, HashMap<String, Object> inputMap) {
    item.setName(inputMap.get("name").toString());
    item.setDescription(inputMap.get("description").toString());
    item.setImage(inputMap.get("linkImage") == null ? "" : inputMap.get("linkImage").toString());
    item.setNotes(inputMap.get("notes").toString());
    //entity.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
    if (inputMap.containsKey("sizePrices")) {
      ArrayList<ItemSize> listsInfo = new ArrayList<>();
      List<HashMap<String, Object>> listSizePrice = (List<HashMap<String, Object>>) inputMap.get("sizePrices");
      for (HashMap<String, Object> hashMap : listSizePrice) {
        Size s = sizeDao.get(hashMap.get("size").toString());
        if (s != null) {
          double price = Double.parseDouble(hashMap.get("price").toString());
          ItemSize sInfo = new ItemSize();
          sInfo.setCreatedBy("self");
          sInfo.setCreatedDate(new Date());
          sInfo.setSize(s);
          sInfo.setPrice(price);
          listsInfo.add(sInfo);
        }
      }
      item.addAllItemSize(listsInfo);
    }
//		if (inputMap.containsKey("modifiers")) { // TODO: temp comment
//			ArrayList<ItemModifier> listmInfo = new ArrayList<ItemModifier>();
//			List<HashMap<String, Object>> listModifier = (List<HashMap<String, Object>>) inputMap.get("modifiers");
//			for (HashMap<String, Object> hashMap : listModifier) {
//				Modifier modifies = modifierDao.get(hashMap.get("modifier").toString());
//				if (modifies != null) {
//					ItemModifier mInfo = new ItemModifier();
//					if (hashMap.containsKey("availabilityStatus")) {
//						mInfo.setAvailabilityStatus(AvailabilityStatus.valueOf(hashMap.get("availabilityStatus").toString()));
//					}else {
//						mInfo.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
//					}
//					mInfo.setModifier(modifies);
//					mInfo.setCreatedBy("self");
//					mInfo.setCreatedDate(new Date());
//					listmInfo.add(mInfo);
//				}
//			}
//			item.addAllModifier(listmInfo);
//		}

    return item;
  }


  //=============================METHOD=============================//

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
			Item item = itemDao.get(id);
			if (item != null) {
				ItemValidator mItemValidator = new ItemValidator(itemDao, dto);
				List<ServiceErrorMessage> mListError = mItemValidator.validateForCreation();
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
      Item item = itemDao.get(id);
      if (item != null) {
        return super.delete(access, id);
      }
      else {
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
        Item item = itemDao.get(id);
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
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }


  //Get Image
  @GET
  @Path("/{id}/images/{fileName}")
  @ApiOperation(value = "api get image items", notes = "http://192.168.1.66:30505/api/{restaurant_id}/items/{item_id}/images/ZVPgntuvC6YakayFBtPyvqPVH.jpg")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "image"),
      @ApiResponse(code = 404, message = "restaurant not found"),
      @ApiResponse(code = 404, message = "item not found"),
      @ApiResponse(code = 404, message = "file not found"),
  })
  @Produces("image/jpg")
  public Response getImage(@PathParam("id") String id, @PathParam("fileName") String fileName) {
    Item item = itemDao.get(id);
    if (item != null) {
      java.nio.file.Path photoPath = Paths.get(System.getProperty("user.dir"), "photos", fileName);
      return returnFile(photoPath.toFile());
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("item not found"));
    }
  }

  private Response returnFile(File file) {
    if (!file.exists()) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "file not found");
    }
    try {
      return Response.ok(new FileInputStream(file)).build();
    }
    catch (FileNotFoundException e) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "file not found");
    }
  }

  @Override
  protected HashMap<String, Object> getMapFromEntity(Item entity) {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", entity.getId());
    dto.put("name", entity.getName());
    dto.put("description", entity.getDescription());
    dto.put("notes", entity.getNotes());
    dto.put("linkImage", entity.getImage());
    dto.put("itemSize", ModelHelpers.fromEntities(entity.getItemSizes()));
    dto.put("modifiers", ModelHelpers.fromEntities(entity.getModifiers()));
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.put(getClassT().getSimpleName().toLowerCase(), dto);
    return returnMap;
  }
}
