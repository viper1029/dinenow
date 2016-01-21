package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.MenuDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SubMenuDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.CategoryInfo;
import com.dinenowinc.dinenow.model.Hour;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.ItemInfo;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.Restaurant;
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


@Path("/menus")
@Api("/menus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuResource extends AbstractResource<Menu> {	

	@Inject
	private MenuDao menuDao;
	@Inject
	private SubMenuDao subMenuDao;

	@Inject
	private CategoryDao categoryDao;
	@Inject
	private ItemDao itemDao;

	@Inject 
	private RestaurantDao restaurantDao;

	@Override
	protected HashMap<String, Object> fromEntity(Menu entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		LinkedHashMap<String, Object> rdto = new LinkedHashMap<String, Object>();
		rdto.put("id", entity.getId());
		rdto.put("name", entity.getMenuName());
		rdto.put("description", entity.getMenuDescription());
		List<HashMap<String, Object>> categories = new ArrayList<HashMap<String, Object>>();
		for (CategoryInfo category : entity.getCategories()) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			temp.put("id", category.getCategory().getId());
			temp.put("name", category.getCategory().getCategoryName());

			List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
			for (ItemInfo item : category.getItems()) {
				LinkedHashMap<String, Object> itemDto = new LinkedHashMap<String, Object>();
				itemDto.put("id", item.getItem().getId());
				itemDto.put("name", item.getItem().getItemName());
				items.add(itemDto);
			}
			temp.put("items", items);
			categories.add(temp);
		}
		rdto.put("categories", categories);
		rdto.put("hours", entity.getHours());
		dto.put(getClassT().getSimpleName().toLowerCase(), rdto);
		return dto;
	}

	@Override
	protected HashMap<String, Object> onGet(Menu entity) {
		HashMap<String, Object> dto = super.onGet(entity);

		//ArrayList<HashMap<String, Object>> listSubMenu = new ArrayList<HashMap<String, Object>>();
		/*	for (SubMenu submenu : entity.getSubMenus()) {
			HashMap<String, Object> enti = new HashMap<String, Object>();
			enti.put("id", submenu.getId());
			enti.put("menuSubName", submenu.getMenuSubName());
			enti.put("subMenuDescription", submenu.getSubMenuDescription());


			ArrayList<HashMap<String, Object>> listCate = new ArrayList<HashMap<String, Object>>();
			for (CategoryInfo cInfo : submenu.getCategories()) {
				HashMap<String, Object> en = new HashMap<String, Object>();
				en.put("categoryName", cInfo.getCategory().getCategoryName());
				en.put("categoryDescription", cInfo.getCategory().getCategoryDescription());
				en.put("id", cInfo.getCategory().getId());

				ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
				for (ItemInfo IInfo : cInfo.getItems()) {
					HashMap<String, Object> item = new HashMap<String, Object>();
					item.put("availabilityStatus", IInfo.getItem().getAvailabilityStatus());
					item.put("itemDescription", IInfo.getItem().getItemDescription());
					item.put("id", IInfo.getItem().getId());
					item.put("itemName", IInfo.getItem().getItemName());
					item.put("linkImage", IInfo.getItem().getLinkImage());
					item.put("notes", IInfo.getItem().getNotes());
					item.put("spiceLevel", IInfo.getItem().getSpiceLevel());
					item.put("isVegeterian", IInfo.getItem().isVegeterian());
					listItems.add(item);
				}
				en.put("items", listItems);
				listCate.add(en);
			}
			enti.put("categories", listCate);

			listSubMenu.add(enti);
		}
		dto.put("subMenus", listSubMenu);*/

		return dto;		
	}	

	@Override
	protected Menu fromDto(HashMap<String, Object> dto) {
		Menu entity = super.fromDto(dto);		
		entity.setMenuName(dto.get("name").toString());
		entity.setMenuDescription(dto.get("description").toString());
		return entity;
	}





	@Override
	protected Menu fromAddDto(HashMap<String, Object> dto) {
		Menu entity = super.fromAddDto(dto);
		entity.setMenuName(dto.get("name").toString());
		entity.setMenuDescription(dto.get("description").toString());
		if (dto.containsKey("categories")) {
			ArrayList<CategoryInfo> listcInfo = new ArrayList<CategoryInfo>();
			ArrayList<HashMap<String, Object>> listKeyCategories = (ArrayList<HashMap<String, Object>>)dto.get("categories");
			for (int i = 0; i < listKeyCategories.size(); i++) {
				//{"menuSubName":"Sub Name 1 sub menu","subMenuDescription":"sub menu des", "categories":[{"id":"b467544f-f78f-4141-83b1-3ed9c902dbb0","items":["2823f44a-e225-4584-9f96-60b4857ca2b1"]}]}
				Category c = categoryDao.findOne(listKeyCategories.get(i).get("id").toString());
				CategoryInfo cInfo = new CategoryInfo();
				ArrayList<ItemInfo> listItemInfo = new ArrayList<ItemInfo>();
				ArrayList<HashMap<String, Object>> listItems = (ArrayList<HashMap<String, Object>>)listKeyCategories.get(i).get("items");

				for (HashMap<String, Object> id_item : listItems) {
					Item tmp = itemDao.findOne(id_item.get("id").toString());
					ItemInfo ItemInfotmp = new ItemInfo();
					ItemInfotmp.setItem(tmp);
					//		ItemInfotmp.setPrice(Double.parseDouble(id_item.get("price").toString()));
					listItemInfo.add(ItemInfotmp);
					cInfo.addItem(ItemInfotmp);
				}
				cInfo.addAllItem(listItemInfo);
				cInfo.setCategory(c);
				listcInfo.add(cInfo);
				entity.addCategory(cInfo);
			}
			entity.addAllCategory(listcInfo);
		}
		return entity;
	}



	//Update 
	@Override
	protected Menu fromUpdateDto(Menu t, HashMap<String, Object> dto) {
		Menu entity = super.fromUpdateDto(t, dto);
		entity.setMenuName(dto.get("name").toString());
		entity.setMenuDescription(dto.get("description").toString());
		if (dto.containsKey("categories")) {
			ArrayList<CategoryInfo> listcInfo = new ArrayList<CategoryInfo>();
			ArrayList<HashMap<String, Object>> listKeyCategories = (ArrayList<HashMap<String, Object>>)dto.get("categories");
			for (int i = 0; i < listKeyCategories.size(); i++) {
				//{"menuSubName":"Sub Name 1 sub menu","subMenuDescription":"sub menu des", "categories":[{"id":"b467544f-f78f-4141-83b1-3ed9c902dbb0","items":["2823f44a-e225-4584-9f96-60b4857ca2b1"]}]}
				Category c = categoryDao.findOne(listKeyCategories.get(i).get("id").toString());
				CategoryInfo cInfo = new CategoryInfo();
				ArrayList<ItemInfo> listItemInfo = new ArrayList<ItemInfo>();
				ArrayList<HashMap<String, Object>> listItems = (ArrayList<HashMap<String, Object>>)listKeyCategories.get(i).get("items");

				for (HashMap<String, Object> id_item : listItems) {
					Item tmp = itemDao.findOne(id_item.get("id").toString());
					ItemInfo ItemInfotmp = new ItemInfo();
					/*					ItemInfotmp.setCreatedBy("SUBMENU");
					ItemInfotmp.setCreatedDate(new Date())*/;
					ItemInfotmp.setItem(tmp);
					//		ItemInfotmp.setPrice(Double.parseDouble(id_item.get("price").toString()));
					listItemInfo.add(ItemInfotmp);
					cInfo.addItem(ItemInfotmp);
				}

				cInfo.addAllItem(listItemInfo);
				cInfo.setCategory(c);
				listcInfo.add(cInfo);
				entity.addCategory(cInfo);
				System.out.println("::::::::::::::::::::::::::::::::::::::LLLL");
			}
			entity.addAllCategory(listcInfo);
		}
		return entity;
	}










	//==============================ACTION====================================//

	@Override
	protected Response onAdd(AccessToken access, Menu entity, Restaurant restaurant) {
		System.out.println("::::::::::::::::::::SCSDCSS");
		if (restaurant == null) {
			List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
			errorMessages.add(new ServiceErrorMessage("Restaurant not found"));
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, errorMessages);
		} 
		entity.setCompositeId(restaurant.getId());
		restaurant.addMenu(entity);
			dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}

	@Override
	protected Response onUpdate(AccessToken access, Menu entity, Restaurant restaurant) {
		restaurant = restaurantDao.findByMenuId(entity.getId());
		entity.setCompositeId(restaurant.getId());
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));	
	}

	@Override
	protected Response onDelete(AccessToken access, Menu entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK,fromEntity(entity));
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED,"has relationship");
		}
	}



	//============================METHOD=====================================//



	@GET
	@ApiOperation("get all menu by OWNER or ADMIN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
	})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {

		//		if (access.getRole() == UserRole.OWNER) {
		//				List<Menu> entities = menuDao.getListByUser(access);		
		//				List<HashMap<String, Object>> dtos = fromEntities(entities);
		//				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
		//		}
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
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}

	@POST
	@ApiOperation(value="add new menu", notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"eeba59d0-eb66-44fe-bfcc-00b777586702\","
			+ "<br/>  \"menuDescription\": \"menu for description new\","
			+ "<br/>  \"menuName\": \"menu name 1 new\","
			+ "<br/>  \"subMenus\": []"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"restaurantId\": \"eeba59d0-eb66-44fe-bfcc-00b777586702\","
			+ "<br/>  \"menuDescription\": \"menu description\","
			+ "<br/>  \"menuName\": \"menu name\","
			+ "<br/>  \"subMenus\": ["
			+ "<br/>    \"2c30f438-e75b-44c0-b35d-c1a9c9650b5b\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"restaurantId\": \"eeba59d0-eb66-44fe-bfcc-00b777586702\","
			+ "<br/>  \"menuDescription\": \"menu for description new\","
			+ "<br/>  \"menuName\": \"menu name 1 new\","
			+ "<br/>  \"categories\": ["
			+ "<br/>    \"ID Categories\","
			+ "<br/>    \"ID Categories\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "Access denied for user"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
	})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			MenuValidator typeValidator = new MenuValidator(dto , menuDao);
			List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
			if (mListError.size() == 0) {
				return super.add(access, dto);
			}
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
	}



	//this for adding in existing 
	@PUT
	@ApiOperation(value="update time menu", notes="MON(0),"
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
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "menu not found"),
			@ApiResponse(code = 500, message = "Exeption:###")
	})
	@Path("/{id}/time")
	public Response updateTimeMenu(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id,HashMap<String, Object> dto){
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Menu menu = menuDao.findOne(id);
			
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
				if(!Utils.inRange(restaurant.getDineInHours(), restaurant.getTimezoneId(),menu.getHours())){
					List<ServiceErrorMessage> errorMessages = new ArrayList<ServiceErrorMessage>();
					errorMessages.add(new ServiceErrorMessage("these hours not available"));
					return ResourceUtils.asFailedResponse(Status.NOT_ACCEPTABLE, errorMessages);
				}
				dao.update(menu);
				return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(menu));
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("menu not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}

	@PUT
	@ApiOperation(value="update menu", notes="<pre><code>"
			+ "{"
			+ "<br/>    \"id\": \"bfb687c2-e412-46ab-bae7-9b118b58c761\","
			+ "<br/>    \"hours\": [{"
			+ "<br/>      \"id\": \"_ybjvsequ9\","
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-23T07:00:00Z\","
			+ "<br/>      \"toTime\": \"2015-03-23T09:00:00Z\""
			+ "<br/>    }],"
			+ "<br/>    \"menuDescription\": \"Menu Add New Test Description Edited\","
			+ "<br/>    \"menuName\": \"Menu Add New Test\""
			+ "<br/>  }</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"menuDescription\": \"menu for description new\","
			+ "<br/>  \"menuName\": \"menu name 1 new\","
			+ "<br/>  \"subMenus\": []"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"menuDescription\": \"menu description\","
			+ "<br/>  \"menuName\": \"menu name\","
			+ "<br/>  \"subMenus\": ["
			+ "<br/>    \"2c30f438-e75b-44c0-b35d-c1a9c9650b5b\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"menuDescription\": \"menu for description new\","
			+ "<br/>  \"menuName\": \"menu name 1 new\","
			+ "<br/>  \"subMenus\": ["
			+ "<br/>    \"ID SUB MENU\","
			+ "<br/>    \"ID SUB MENU\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "menu not found"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")  
	})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Menu menu = menuDao.findOne(id);
			if (menu != null) {
				MenuValidator typeValidator = new MenuValidator(dto ,menuDao );
				List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
				if (mListError.size() == 0) {
					return super.update(access, id, dto);
				}
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
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
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id){
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			Menu menu = menuDao.findOne(id);
			if (menu != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("menu not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}


}
