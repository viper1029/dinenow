package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.dinenowinc.dinenow.dao.AddOnDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.ModifierAddOn;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Modifier;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/modifiers")
@Api("/modifiers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModifierResource extends AbstractResource<Modifier> {

  @Inject
  private RestaurantDao restaurantDao;

  @Inject
  private AddOnDao addOnDao;

  @Inject
  private ItemDao itemDao;

  @Inject
  SizeDao sizeDao;

  @Inject
  private ModifierDao modifierDao;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResource.class);


  @Override
  protected HashMap<String, Object> getMapFromEntity(Modifier entity) {
    HashMap<String, Object> dto = new HashMap<String, Object>();
/*		dto.put("id", entity.getId());
    dto.put("modifierName", entity.getModifierName());
		dto.put("modifierDescription", entity.getModifierDescription());
		dto.put("isSelectMultiple", entity.isSelectMultiple());
		dto.put("minSelection", entity.getMinSelection());
		dto.put("maxSelection", entity.getMaxSelection());*/
    dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
    return dto;
  }

  @Override
  protected Modifier getEntityForInsertion(HashMap<String, Object> inputMap) {
    Modifier entity = super.getEntityForInsertion(inputMap);

    entity.setModifierName(inputMap.get("name").toString());
    entity.setModifierDescription(inputMap.get("description").toString());
    entity.setSelectMultiple(Boolean.parseBoolean(inputMap.get("isSelectMultiple").toString()));
    entity.setMinSelection(Integer.parseInt(inputMap.get("minSelection").toString()));
    entity.setMaxSelection(inputMap.containsKey("maxSelection") ? Integer.parseInt(inputMap.get("maxSelection").toString()) : entity.getMinSelection());

    System.out.println("::::::::::::::::::::::::::::::::::");
    if (inputMap.containsKey("addOns")) {
      ArrayList<ModifierAddOn> listInfo = new ArrayList<ModifierAddOn>();

      List<HashMap<String, Object>> listAddOns = (List<HashMap<String, Object>>) inputMap.get("addOns");
      for (HashMap<String, Object> hashMap : listAddOns) {
        AddOn addon = addOnDao.get(hashMap.get("addOn").toString());
        double price = Double.parseDouble(hashMap.get("price").toString());
        boolean isDefault = Boolean.parseBoolean(hashMap.get("isDefault").toString());
        AvailabilityStatus availabilityStatus = AvailabilityStatus.valueOf(hashMap.get("availabilityStatus").toString());
        ModifierAddOn info = new ModifierAddOn();
        info.setAddOn(addon);
        info.setPrice(price);
        info.setDefault(isDefault);
        info.setAvailStatus(availabilityStatus);
        listInfo.add(info);
      }
      entity.addAllAddOns(listInfo);
    }
		
		/*if (dto.containsKey("itemSizes")) {
			ArrayList<ItemSizeInfo> listInfo = new ArrayList<ItemSizeInfo>();
			
			List<HashMap<String, Object>> listItemSize = (List<HashMap<String, Object>>) dto.get("itemSizes");
			for (HashMap<String, Object> hashMap : listItemSize) {
				Item item = itemDao.get(hashMap.get("item").toString());
				Size size = sizeDao.get(hashMap.get("size").toString());
				ItemSizeInfo info = new ItemSizeInfo();
				info.setItem(item);
				info.setSize(size);
				listInfo.create(info);
			}
			entity.addAllItems(listInfo);
		}*/
    return entity;
  }

  @Override
  protected Modifier getEntityForUpdate(Modifier modifier, HashMap<String, Object> inputMap) {
    System.out.println(":::::::::::::::::::::::::::::::" + inputMap);
    modifier.setModifierName(inputMap.get("name").toString());
    modifier.setModifierDescription(inputMap.get("description").toString());
    modifier.setSelectMultiple(Boolean.parseBoolean(inputMap.get("isSelectMultiple").toString()));
    modifier.setMinSelection(Integer.parseInt(inputMap.get("minSelection").toString()));
    modifier.setMaxSelection(inputMap.containsKey("maxSelection") ? Integer.parseInt(inputMap.get("maxSelection").toString()) : modifier.getMinSelection());
    if (inputMap.containsKey("addOns")) {
      ArrayList<ModifierAddOn> listInfo = new ArrayList<ModifierAddOn>();

      List<HashMap<String, Object>> listAddOns = (List<HashMap<String, Object>>) inputMap.get("addOns");
      for (HashMap<String, Object> hashMap : listAddOns) {
        AddOn addon = addOnDao.get(hashMap.get("addOn").toString());
        double price = Double.parseDouble(hashMap.get("price").toString());
        boolean isDefault = Boolean.parseBoolean(hashMap.get("isDefault").toString());
        AvailabilityStatus availabilityStatus = AvailabilityStatus.valueOf(hashMap.get("availabilityStatus").toString());

        ModifierAddOn info = new ModifierAddOn();
        info.setAddOn(addon);
        info.setPrice(price);
        info.setDefault(isDefault);
        info.setAvailStatus(availabilityStatus);
        listInfo.add(info);
        System.out.println(":::::::::::::::::::::::::::::::" + availabilityStatus);
      }
      modifier.addAllAddOns(listInfo);
    }
		
		/*if (dto.containsKey("itemSizes")) {
			ArrayList<ItemSizeInfo> listInfo = new ArrayList<ItemSizeInfo>();
			
			List<HashMap<String, Object>> listItemSize = (List<HashMap<String, Object>>) dto.get("itemSizes");
			for (HashMap<String, Object> hashMap : listItemSize) {
				Item item = itemDao.get(hashMap.get("item").toString());
				Size size = sizeDao.get(hashMap.get("size").toString());
				ItemSizeInfo info = new ItemSizeInfo();
				info.setItem(item);
				info.setSize(size);
				listInfo.create(info);
			}
			entity.addAllItems(listInfo);
		}*/
    return modifier;
  }


  //============================ACTION===================================//

  @Override
  protected HashMap<String, Object> onGet(Modifier entity, User access) {

    HashMap<String, Object> dto = new HashMap<String, Object>();

    ArrayList<HashMap<String, Object>> listAddOn = new ArrayList<HashMap<String, Object>>();
    for (ModifierAddOn addonInfo : entity.getAddOns()) {
      HashMap<String, Object> en = new HashMap<String, Object>();
      en.put("addOn", addonInfo.getAddOn().getId());
      en.put("price", addonInfo.getPrice());
//			en.put("isDefault", addonInfo.isDefault());
      en.put("availabilityStatus", addonInfo.getAvailStatus());
      listAddOn.add(en);
    }
    dto.put("addOns", listAddOn);
		
		/*ArrayList<HashMap<String, Object>> listItemSizes = new ArrayList<HashMap<String, Object>>();
		for (ItemSizeInfo itemInfo : entity.getItems()) {
			HashMap<String, Object> en = new HashMap<String, Object>();
			en.put("item", itemInfo.getItem().getId());
			en.put("size", itemInfo.getSize().getId());
			listItemSizes.create(en);
		}
		dto.put("itemSizes", listItemSizes);*/
    return dto;
  }

  @Override
  protected Response onCreate(User access, Modifier entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addModifier(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, onGet(entity, access));
  }


  //==============================METHOD=================================//


  @GET
  @ApiOperation("api get all Modifier of restaurant for ADMIN and OWNER")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

  @GET
  @Path("/{id}")
  @ApiOperation("api get detail of Modifier")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "cannot found entity"),
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
  @ApiOperation(value = "add new modifier", notes = "<pre><code>{"
      + "<br/>  \"restaurantId\": \"2a91b0ba-eae6-4442-b94d-410e6a382a75\","
      + "<br/>  \"modifierName\": \"modifier edited 2 item\","
      + "<br/>  \"modifierDescription\": \"modifier description edited item\","
      + "<br/>  \"isSelectMultiple\":true,"
      + "<br/>  \"minSelection\":2,"
      + "<br/>  \"maxSelection\":5,"
      + "<br/>  \"addOns\":[],"
      + "<br/>  \"itemSizes\":[]"
      + "<br/>}</code></pre>"
      + "<br/>"
      + "<br/>"
      + "<br/>"
      + "<pre><code>{"
      + "<br/>  \"restaurantId\": \"2a91b0ba-eae6-4442-b94d-410e6a382a75\","
      + "<br/>  \"modifierName\": \"modifier New Test\","
      + "<br/>  \"modifierDescription\": \"modifier New Test Decription\","
      + "<br/>  \"isSelectMultiple\": true,"
      + "<br/>  \"minSelection\": 5,"
      + "<br/>  \"maxSelection\": 20,"
      + "<br/>  \"addOns\": ["
      + "<br/>    {"
      + "<br/>      \"addOn\": \"7063824d-5da8-44cf-affe-0645bb529cf6\","
      + "<br/>      \"price\": 15,"
      + "<br/>      \"isDefault\": true,"
      + "<br/>      \"availabilityStatus\": \"AVAILABLE\""
      + "<br/>    }"
      + "<br/>  ]"
      + "<br/>}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 500, message = "cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }


  @PUT
  @ApiOperation(value = "update Modifier")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "modifier not found"),
      @ApiResponse(code = 500, message = "cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Modifier modifier = dao.get(id);
      Restaurant restaurant = restaurantDao.get((String) inputMap.get("restaurantId"));
      modifier = getEntityForUpdate(modifier, inputMap);
      try {
        if (modifier != null) {
          dao.update(modifier);
          restaurant.addModifier(modifier);
          return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(modifier));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("modifier not found"));
        }
      }
      catch (RollbackException e) {
        LOGGER.debug("Error updating entity.", e);
        return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot update entity. Error message: %s", e.getMessage())));
      }
      catch (Exception e) {
        LOGGER.debug("Error updating entity.", e);
        return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(String.format("Cannot update entity. Error message: %s", e.getMessage())));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }


  @DELETE
  @Path("/{id}")
  @ApiOperation("delete Modifier by id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 404, message = "modifier not found"),
      @ApiResponse(code = 500, message = "cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "cannot found entity"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Modifier modifier = dao.get(id);
      if (modifier != null) {
        return super.delete(access, id);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("modifier not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

}