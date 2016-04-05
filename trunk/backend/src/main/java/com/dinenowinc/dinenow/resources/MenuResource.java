package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.MenuDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.CategoryItem;
import com.dinenowinc.dinenow.model.Hour;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.ItemPrice;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.model.WeekDayType;
import com.dinenowinc.dinenow.utils.Utils;
import com.dinenowinc.dinenow.validation.MenuValidator;
import com.google.inject.Inject;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Path("/menus")
@Api("/menus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuResource extends AbstractResource<Menu> {

  @Inject
  private MenuDao menuDao;

  @Inject
  private CategoryDao categoryDao;

  @Inject
  private ItemDao itemDao;

  @Inject
  private RestaurantDao restaurantDao;

  @GET
  @ApiOperation("get all menu by OWNER or ADMIN")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
  })
  @Override
  public Response getAll(@ApiParam(access = "internal") @Auth User access) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.getAll(access);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @GET
  @ApiOperation("get detail of menu by id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "Access denied for user")
  })
  @Path("/{id}")
  @Override
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }


  @POST
  @ApiOperation(value = "add new menu")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "Access denied for user"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      MenuValidator menuValidator = new MenuValidator(inputMap);
      List<ServiceErrorMessage> errorMessages = menuValidator.validateForCreation();
      if (errorMessages.size() == 0) {
        return super.create(access, inputMap);
      }
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, errorMessages);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Response onCreate(User access, Menu entity, Restaurant restaurant) {
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
    }
    restaurant.addMenu(entity);
    dao.save(entity);
    return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
  }

  @Override
  protected Menu getEntityForInsertion(HashMap<String, Object> inputMap) {
    Menu entity = super.getEntityForInsertion(inputMap);
    entity.setName(inputMap.get("name").toString());
    entity.setDescription(inputMap.get("description") == null ? "N/A" : inputMap.get("description").toString());
    entity.setNotes(inputMap.get("notes") == null ? "N/A" : inputMap.get("notes").toString());

    if (inputMap.containsKey("categoryItem")) {
      ArrayList<CategoryItem> categoryItems = new ArrayList<>();
      List<HashMap<String, Object>> categoryItemList = (ArrayList<HashMap<String, Object>>) inputMap.get("categoryItem");
      for (HashMap<String, Object> categoryItem : categoryItemList) {
        categoryItems.add(createNewCategoryItem(categoryItem, entity));
      }
      entity.addAllCategoryItem(categoryItems);
    }
    return entity;
  }

  private CategoryItem createNewCategoryItem(HashMap<String, Object> categoryItem, Menu entity) {
    CategoryItem categoryItemEntity = new CategoryItem();
    HashMap<String, Object> categoryItemMap = (HashMap<String, Object>) categoryItem.get("category");
    Category category = categoryDao.get(categoryItemMap.get("id").toString());
    if (categoryItem.containsKey("itemPrice")) {
      List<HashMap<String, Object>> itemPriceList = (ArrayList<HashMap<String, Object>>) categoryItem.get("itemPrice");
      for (HashMap<String, Object> itemPrice : itemPriceList) {
        Item item = itemDao.get(((HashMap<String, Object>)itemPrice.get("item")).get("id").toString());
        ItemPrice itemPriceEntity = new ItemPrice();
        itemPriceEntity.setPrice(Double.valueOf((itemPrice.get("price").toString())));
        itemPriceEntity.setItem(item);
        itemPriceEntity.setCategoryItem(categoryItemEntity);
        itemPriceEntity.setCreatedBy("system");
        itemPriceEntity.setCreatedDate(new Date());
        categoryItemEntity.addItemPrice(itemPriceEntity);
      }
    }
    categoryItemEntity.setCreatedBy("system");
    categoryItemEntity.setCreatedDate(new Date());
    categoryItemEntity.setMenu(entity);
    categoryItemEntity.setCategory(category);
    return categoryItemEntity;
  }

  @PUT
  @ApiOperation(value = "update menu")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "menu not found"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Menu menu = menuDao.get(id);
      if (menu != null) {
        MenuValidator typeValidator = new MenuValidator(inputMap);
        List<ServiceErrorMessage> errorMessages = typeValidator.validateForCreation();
        if (errorMessages.size() == 0) {
          return super.update(access, id, inputMap);
        }
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, errorMessages);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Menu not found."));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Override
  protected Menu getEntityForUpdate(Menu menu, HashMap<String, Object> inputMap) {
    menu.setName(inputMap.get("name").toString());
    menu.setDescription(inputMap.get("description") == null ? "N/A" : inputMap.get("description").toString());
    menu.setNotes(inputMap.get("notes") == null ? "N/A" : inputMap.get("notes").toString());
    if (inputMap.containsKey("categoryItem")) {
      ArrayList<CategoryItem> newCategoryItems = new ArrayList<>();
      ArrayList<CategoryItem> keepExistingCategoryItems = new ArrayList<>();
      List<HashMap<String, Object>> categoryItemList = (List<HashMap<String, Object>>) inputMap.get("categoryItem");
      for(HashMap<String, Object> categoryItem: categoryItemList) {
        boolean foundExisting = false;
        if(categoryItem.get("id") != null) {
          for(CategoryItem existingCategoryItem : menu.getCategoryItem()) {
            if(existingCategoryItem.getId().matches(categoryItem.get("id").toString())) {
              if(existingCategoryItem.getCategory().getId().matches(((HashMap<String, Object>) categoryItem.get("category")).get("id").toString()))  {
                Category category = categoryDao.get(((HashMap<String, Object>) categoryItem.get("category")).get("id").toString());
                existingCategoryItem.setCategory(category);
                existingCategoryItem.setMenu(menu);
              }
              updateItemPrice(existingCategoryItem, inputMap);
              foundExisting = true;
              keepExistingCategoryItems.add(existingCategoryItem);
              break;
            }
          }
        }
        if(!foundExisting) {
          newCategoryItems.add(createNewCategoryItem(categoryItem, menu));
        }
      }
      menu.getCategoryItem().retainAll(keepExistingCategoryItems);
      menu.addAllCategoryItem(newCategoryItems);
    }
    return menu;
  }

  private CategoryItem updateItemPrice(CategoryItem categoryItem, HashMap<String, Object> inputMap) {

    if (inputMap.containsKey("addonSize")) {
      ArrayList<ItemPrice> newItemPrices = new ArrayList<>();
      ArrayList<ItemPrice> keepExistingItemPrices = new ArrayList<>();
      List<HashMap<String, Object>> itemPriceList = (List<HashMap<String, Object>>) inputMap.get("itemPrice");
      for (HashMap<String, Object> itemPrice : itemPriceList) {
        boolean foundExisting = false;
        if (itemPrice.get("id") != null) {
          for (ItemPrice existingItemPrice : categoryItem.getItemPrices()) {
            if (existingItemPrice.getId().matches(itemPrice.get("id").toString())) {
              existingItemPrice.setPrice(Double.valueOf(itemPrice.get("price").toString()));
              if (!existingItemPrice.getItem().getId().matches(((HashMap<String, Object>) itemPrice.get("item")).get("id").toString())) {
                Item item = itemDao.get(((HashMap<String, Object>) itemPrice.get("item")).get("id").toString());
                existingItemPrice.setItem(item);
                //existingItemPrice.et
              }
              foundExisting = true;
              keepExistingItemPrices.add(existingItemPrice);
              break;
            }
          }
        }
        if (!foundExisting) {
          Item item = itemDao.get(((HashMap<String, Object>) itemPrice.get("item")).get("id").toString());
          ItemPrice newItemPrice = new ItemPrice();
          newItemPrice.setItem(item);
          newItemPrice.setPrice(Double.valueOf(itemPrice.get("price").toString()));
          newItemPrice.setCreatedBy("system");
          newItemPrice.setCreatedDate(new Date());
          newItemPrices.add(newItemPrice);
        }
      }
      categoryItem.getItemPrices().retainAll(keepExistingItemPrices);
      categoryItem.addAllItemPrice(newItemPrices);
    }
    return categoryItem;
  }

  @Override
  protected HashMap<String, Object> getMapFromEntity(Menu entity) {
    HashMap<String, Object> dto = new HashMap<String, Object>();
    LinkedHashMap<String, Object> rdto = new LinkedHashMap<String, Object>();
    rdto.put("id", entity.getId());
    rdto.put("name", entity.getName());
    rdto.put("description", entity.getDescription());
    List<HashMap<String, Object>> categoryItems = new ArrayList<HashMap<String, Object>>();
    for (CategoryItem categoryItem : entity.getCategoryItem()) {
      LinkedHashMap<String, Object> categoryItemMap = new LinkedHashMap<String, Object>();
      LinkedHashMap<String, Object> categoryDto = new LinkedHashMap<String, Object>();
      categoryDto.put("id", categoryItem.getCategory().getId());
      categoryDto.put("name", categoryItem.getCategory().getCategoryName());

      List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
      for (ItemPrice itemPrice : categoryItem.getItemPrices()) {
        LinkedHashMap<String, Object> itemPriceDto = new LinkedHashMap<String, Object>();
        itemPriceDto.put("id", itemPrice.getId());
        LinkedHashMap<String, Object> itemDto = new LinkedHashMap<String, Object>();
        itemDto.put("id", itemPrice.getItem().getId());
        itemDto.put("name", itemPrice.getItem().getName());
        itemPriceDto.put("item", itemDto);
        items.add(itemPriceDto);
      }
      categoryItemMap.put("id", categoryItem.getId());
      categoryItemMap.put("category", categoryDto);
      categoryItemMap.put("itemPrice", items);
      categoryItems.add(categoryItemMap);
    }
    rdto.put("categoryItem", categoryItems);
    rdto.put("hours", entity.getHours());
    dto.put(getClassT().getSimpleName().toLowerCase(), rdto);
    return dto;
  }

  //Update


  //this for adding in existing
  @PUT
  @ApiOperation(value = "update time menu", notes = "MON(0),"
      + "TUE(1),"
      + "WED(2),"
      + "THU(3),"
      + "FRI(4),"
      + "SET(5),"
      + "SUN(6);<br/>"
      + "<br/><br/><pre><code>{"
      + "<br/>  \"weekDayType\": \"MON\","
      + "<br/>  \"fromTime\": \"2015-03-19T13:26:55Z\","
      + "<br/>  \"toTime\": \"2015-03-19T13:26:55Z\""
      + "<br/>}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 404, message = "menu not found"),
      @ApiResponse(code = 500, message = "Exeption:###")
  })
  @Path("/{id}/time")
  public Response updateTimeMenu(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Menu menu = menuDao.get(id);

      if (menu != null) {
        Restaurant restaurant = restaurantDao.findByMenuId(menu.getId());
        String timezone = restaurant.getTimezoneId();
        if (dto.containsKey("hours")) {
          List<HashMap<String, Object>> menuHours = (List<HashMap<String, Object>>) dto.get("hours");
          ArrayList<Hour> arrHour = new ArrayList<Hour>();
          for (HashMap<String, Object> menuHour : menuHours) {
            Hour hour = new Hour();
            if (menuHour.containsKey("weekDayType")) {
              hour.setWeekDayType(WeekDayType.valueOf(menuHour.get("weekDayType").toString()));
            }
            if (menuHour.containsKey("fromTime")) {
              hour.setFromTime(Utils.convertTimeZones(timezone, Utils.UTC, menuHour.get("fromTime").toString()));
            }
            if (menuHour.containsKey("toTime")) {
              hour.setToTime(Utils.convertTimeZones(timezone, Utils.UTC, menuHour.get("toTime").toString()));
            }
            arrHour.add(hour);
          }
          menu.setHours(arrHour);
        }
        if (!Utils.inRange(restaurant.getDineInHours(), restaurant.getTimezoneId(), menu.getHours())) {
          List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
          errorMessages.add(new ServiceErrorMessage("these hours not available"));
          return ResourceUtils.asFailedResponse(Status.NOT_ACCEPTABLE, errorMessages);
        }
        dao.update(menu);
        return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(menu));
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("menu not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }



  @DELETE
  @Path("/{id}")
  @ApiOperation("delete menu by id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 404, message = "menu not found"),
      @ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Menu menu = menuDao.get(id);
      if (menu != null) {
        return super.delete(access, id);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("menu not found"));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }


}
