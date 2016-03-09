package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.Addon;
import com.dinenowinc.dinenow.model.AddonSize;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.ModelHelpers;
import com.dinenowinc.dinenow.model.ModifierAddon;
import io.dropwizard.auth.Auth;

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

import com.dinenowinc.dinenow.dao.AddonDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.User;
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
  private AddonDao addonDao;

  @Inject
  private ItemDao itemDao;

  @Inject
  SizeDao sizeDao;

  @Inject
  private ModifierDao modifierDao;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResource.class);

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
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
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
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @POST
  @ApiOperation(value = "add new modifier")
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
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Modifier entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addModifier(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, entity);
  }

  @Override
  protected Modifier getEntityForInsertion(HashMap<String, Object> inputMap) {
    Modifier entity = super.getEntityForInsertion(inputMap);
    entity.setName(inputMap.get("name").toString());
    entity.setDescription(inputMap.get("description").toString());
    entity.setMultipleSelection(Boolean.parseBoolean(inputMap.get("isMultipleSelection").toString()));
    entity.setMinSelection(Integer.parseInt(inputMap.get("minSelection").toString()));
    entity.setMaxSelection(inputMap.containsKey("maxSelection") ? Integer.parseInt(inputMap.get("maxSelection").toString()) : entity.getMinSelection());

    if (inputMap.containsKey("modifierAddon")) {
      ArrayList<ModifierAddon> modifierAddons = new ArrayList<>();
      List<HashMap<String, Object>> modifierAddonList = (List<HashMap<String, Object>>) inputMap.get("modifierAddon");
      for (HashMap<String, Object> modifierAddon : modifierAddonList) {
        modifierAddons.add(createNewModifierAddon(modifierAddon, entity));
      }
      entity.addAllModifierAddon(modifierAddons);
    }
    return entity;
  }

  private ModifierAddon createNewModifierAddon(HashMap<String, Object> modifierAddon, Modifier entity) {
    Addon addon = addonDao.get(((HashMap<String, Object>) modifierAddon.get("addon")).get("id").toString());
    double price = Double.parseDouble(modifierAddon.get("price").toString());
    ModifierAddon modifierAddonEntity = new ModifierAddon();
    modifierAddonEntity.setModifier(entity);
    modifierAddonEntity.setAddon(addon);
    modifierAddonEntity.setPrice(price);
    modifierAddonEntity.setDefault(modifierAddon.get("isDefault") == null ? false : Boolean.valueOf(modifierAddon.get("isDefault").toString()));
    modifierAddonEntity.setCreatedBy("system");
    modifierAddonEntity.setCreatedDate(new Date());
    return modifierAddonEntity;
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
       if (modifier != null) {
         return super.update(access, id, inputMap);
        }
       else {
         return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Add on not found"));
       }
      }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Modifier getEntityForUpdate(Modifier modifier, HashMap<String, Object> inputMap) {
    modifier.setName(inputMap.get("name").toString());
    modifier.setDescription(inputMap.get("description").toString());
    modifier.setMultipleSelection(Boolean.parseBoolean(inputMap.get("isMultipleSelection").toString()));
    modifier.setMinSelection(Integer.parseInt(inputMap.get("minSelection").toString()));
    modifier.setMaxSelection(inputMap.containsKey("maxSelection") ? Integer.parseInt(inputMap.get("maxSelection").toString()) : modifier.getMinSelection());

    if (inputMap.containsKey("modifierAddon")) {
      ArrayList<ModifierAddon> newModifierAddons = new ArrayList<ModifierAddon>();
      ArrayList<ModifierAddon> keepExistingModifierAddons = new ArrayList<ModifierAddon>();
      List<HashMap<String, Object>> modifierAddonList = (List<HashMap<String, Object>>) inputMap.get("modifierAddon");
      for (HashMap<String, Object> modifierAddon : modifierAddonList) {
        boolean foundExisting = false;
        if (modifierAddon.get("id") != null) {
          for (ModifierAddon existingModifierAddon : modifier.getModifierAddons()) {
            if (existingModifierAddon.getId().matches(modifierAddon.get("id").toString())) {
              existingModifierAddon.setPrice(Double.parseDouble(modifierAddon.get("price").toString()));
              existingModifierAddon.setDefault(Boolean.valueOf(modifierAddon.get("isDefault").toString()));
              if (!existingModifierAddon.getAddon().getId().matches(
                  ((HashMap<String, Object>) modifierAddon.get("addon")).get("id").toString())) {
                existingModifierAddon.setAddon(addonDao.get(((HashMap<String, Object>) modifierAddon.get("addon")).get("id").toString()));
                existingModifierAddon.setModifier(modifier);
              }
              foundExisting = true;
              keepExistingModifierAddons.add(existingModifierAddon);
              break;
            }
          }
        }
        if (!foundExisting) {
          newModifierAddons.add(createNewModifierAddon(modifierAddon, modifier));
        }
      }
      modifier.getModifierAddons().retainAll(keepExistingModifierAddons);
      modifier.addAllModifierAddon(newModifierAddons);
    }
    return modifier;
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
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected HashMap<String, Object> getMapFromEntity(Modifier entity) {
    HashMap<String, Object> dto = new LinkedHashMap<>();
		dto.put("id", entity.getId());
    dto.put("name", entity.getName());
		dto.put("description", entity.getDescription());
		dto.put("isMultipleSelection", entity.isMultipleSelection());
		dto.put("minSelection", entity.getMinSelection());
		dto.put("maxSelection", entity.getMaxSelection());
    dto.put("modifierAddon", ModelHelpers.fromEntities(entity.getModifierAddons()));
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(getClassT().getSimpleName().toLowerCase(), dto);
    return returnMap;
  }
}
