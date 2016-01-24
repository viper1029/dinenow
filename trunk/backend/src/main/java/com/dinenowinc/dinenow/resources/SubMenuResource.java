package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
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

import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.SubMenuDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.*;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/submenus")
@Api("/submenus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubMenuResource extends AbstractResource<SubMenu> {	
	
	@Inject
	private SubMenuDao subMenuDao;
	@Inject
	private CategoryDao categoryDao;
	@Inject
	private ItemDao itemDao;
	@Inject
	private RestaurantDao restaurantDao;
	
	
	@Override
	protected HashMap<String, Object> fromEntity(SubMenu entity) {
		HashMap<String, Object> dto = new LinkedHashMap<>();
		dto.put("id", entity.getId());
		dto.put("name", entity.getMenuSubName());
		dto.put("description", entity.getSubMenuDescription());
		dto.put("notes", entity.getSubMenuNotes());
			List<HashMap<String, Object>> categories = new ArrayList<HashMap<String, Object>>();
/*			for (CategoryInfo category : entity.getCategories()) {
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
			}*/
			dto.put("categories", categories);
		HashMap<String, Object> dtos = new LinkedHashMap<>();
		dtos.put(getClassT().getSimpleName().toLowerCase(), dto);
		return dtos;
	}

	@Override
	protected SubMenu fromDto(HashMap<String, Object> dto) {
		SubMenu entity = super.fromDto(dto);
		entity.setMenuSubName(dto.get("name").toString());
		entity.setSubMenuDescription(dto.get("description").toString());	
		entity.setSubMenuNotes(dto.get("notes").toString());
		return entity;
	}
	
	/*@Override
	protected HashMap<String, Object> onGet(SubMenu entity) {
		HashMap<String, Object> dto = super.onGet(entity);
		
		ArrayList<HashMap<String, Object>> listCate = new ArrayList<HashMap<String, Object>>();
		for (CategoryInfo cInfo : entity.getCategories()) {
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
		dto.put("categories", listCate);
		
		return dto;		
	}
	*/
	
	
	@Override
	protected SubMenu fromAddDto(HashMap<String, Object> dto) {
		SubMenu entity = super.fromAddDto(dto);
		entity.setMenuSubName(dto.get("name").toString());
		entity.setSubMenuDescription(dto.get("description").toString());	
		entity.setSubMenuNotes(dto.get("notes").toString());
/*		if (dto.containsKey("categories")) {
			ArrayList<CategoryInfo> listcInfo = new ArrayList<CategoryInfo>();
			ArrayList<HashMap<String, Object>> listKeyCategories = (ArrayList<HashMap<String, Object>>)dto.get("categories");
			for (int i = 0; i < listKeyCategories.size(); i++) {
				//{"menuSubName":"Sub Name 1 sub menu","subMenuDescription":"sub menu des", "categories":[{"id":"b467544f-f78f-4141-83b1-3ed9c902dbb0","items":["2823f44a-e225-4584-9f96-60b4857ca2b1"]}]}
				Category c = categoryDao.findOne(listKeyCategories.get(i).get("id").toString());
				CategoryInfo cInfo = new CategoryInfo();
				Set<ItemInfo> listItemInfo = new HashSet<ItemInfo>();
				ArrayList<HashMap<String, Object>> listItems = (ArrayList<HashMap<String, Object>>)listKeyCategories.get(i).get("items");
				
				for (HashMap<String, Object> id_item : listItems) {
					Item tmp = itemDao.findOne(id_item.get("id").toString());
					ItemInfo ItemInfotmp = new ItemInfo();
					ItemInfotmp.setItem(tmp);
			//		ItemInfotmp.setPrice(Double.parseDouble(id_item.get("price").toString()));
					listItemInfo.add(ItemInfotmp);
					ItemInfotmp.setCreatedBy("SUBMENU");
					ItemInfotmp.setCreatedDate(new Date());
				}
				cInfo.setItems(listItemInfo);
				cInfo.setCategory(c);
				listcInfo.add(cInfo);
				entity.addCategory(cInfo);
				System.out.println("::::::::::::::::::::::::::::::::::::::LLLL");
			}
			entity.addAllCategory(listcInfo);
		}*/
		return entity;
	}
	
	@Override
	protected SubMenu fromUpdateDto(SubMenu t, HashMap<String, Object> dto) {
		SubMenu entity = super.fromUpdateDto(t, dto);
		entity.setMenuSubName(dto.get("name").toString());
		entity.setSubMenuDescription(dto.get("description").toString());	
		entity.setSubMenuNotes(dto.get("notes").toString());
/*		if (dto.containsKey("categories")) {
			ArrayList<CategoryInfo> listcInfo = new ArrayList<CategoryInfo>();
			ArrayList<HashMap<String, Object>> listKeyCategories = (ArrayList<HashMap<String, Object>>)dto.get("categories");
			for (int i = 0; i < listKeyCategories.size(); i++) {
				//{"menuSubName":"Sub Name 1 sub menu","subMenuDescription":"sub menu des", "categories":[{"id":"b467544f-f78f-4141-83b1-3ed9c902dbb0","items":["2823f44a-e225-4584-9f96-60b4857ca2b1"]}]}
				Category c = categoryDao.findOne(listKeyCategories.get(i).get("id").toString());
				CategoryInfo cInfo = new CategoryInfo();
				Set<ItemInfo> listItemInfo = new HashSet<ItemInfo>();
				List<HashMap<String, Object>> listItems = (ArrayList<HashMap<String, Object>>)listKeyCategories.get(i).get("items");
				
				for (HashMap<String, Object> id_item : listItems) {
					Item tmp = itemDao.findOne(id_item.get("id").toString());
					ItemInfo ItemInfotmp = new ItemInfo();
					ItemInfotmp.setItem(tmp);
					//ItemInfotmp.setPrice(Double.parseDouble(id_item.get("price").toString()));
					listItemInfo.add(ItemInfotmp);
				}
				cInfo.setItems(listItemInfo);
				cInfo.setCategory(c);
				listcInfo.add(cInfo);
			}
			entity.addAllCategory(listcInfo);
		}*/
		return entity;
	}
	
	
	
	
	
	
	//==================================ACTION===========================================//
	
	@Override
	protected Response onAdd(User access, SubMenu entity, Restaurant restaurant) {
		if (restaurant == null) {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found"));
		}
		//restaurant.addSubMenu(entity);
		dao.save(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	
	@Override
	protected Response onUpdate(User access, SubMenu entity, Restaurant restaurant) {
		dao.update(entity);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));	
	}
	
	@Override
	protected Response onDelete(User access, SubMenu entity) {
		try {
			dao.delete(entity);
			return ResourceUtils.asSuccessResponse(Status.OK,fromEntity(entity));
		} catch (RollbackException e) {
			return ResourceUtils.asFailedResponse(Status.PRECONDITION_FAILED,"has relationship");
		}
	}
	
	
	
	
	
	//====================================METHOD============================================//
	
	
	@GET
	@ApiOperation("get all submenu by restaurant OWNER or ADMIN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
//		if (access.getRole() == UserRole.OWNER) {
//			List<SubMenu> entities = subMenuDao.getListByUser(access);	
//			List<HashMap<String, Object>> dtos = fromEntities(entities);
//			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
//		}
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.getAll(access);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
		
	}
	

	@GET
	@ApiOperation("get submenu detail")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Path("/{id}")
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
				return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	@POST
	@ApiOperation(value="add new Sub menu",notes="<pre><code>{"
			+ "<br/>  \"restaurantId\": \"95260ea2-facb-408e-8d11-aca816b2a27f\","
			+ "<br/>  \"name\": \"Sub Menu Name\","
			+ "<br/>  \"description\": \"Sub Menu Description\","
			+ "<br/>  \"notes\":\"notes\","
			+ "<br/>  \"categories\": []"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"restaurantId\": \"95260ea2-facb-408e-8d11-aca816b2a27f\","
			+ "<br/>  \"name\": \"Sub Name 1 sub menu\","
			+ "<br/>  \"description\": \"sub menu des\","
			+ "<br/>  \"notes\":\"notes\","
			+ "<br/>  \"categories\": ["
			+ "<br/>    {"
			+ "<br/>      \"id\": \"958c6d94-eee4-4a7a-aeb3-54ab4c49f00f\","
			+ "<br/>      \"items\": []"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"restaurantId\": \"95260ea2-facb-408e-8d11-aca816b2a27f\","
			+ "<br/>  \"menuSubName\": \"Sub Name 1 sub menu\","
			+ "<br/>  \"subMenuDescription\": \"sub menu des\","
			+ "<br/>  \"subMenuNotes\":\"notes\","
			+ "<br/>  \"categories\": ["
			+ "<br/>    {"
			+ "<br/>      \"id\": \"b467544f-f78f-4141-83b1-3ed9c902dbb0\","
			+ "<br/>      \"items\": ["
			+ "<br/>        {"
			+ "<br/>          \"id\": \"2823f44a-e225-4584-9f96-60b4857ca2b1\","
			+ "<br/>          \"price\": 0"
			+ "<br/>        },"
			+ "<br/>        {"
			+ "<br/>          \"id\": \"2823f44a-e225-4584-9f96-60b4857ca2b1\","
			+ "<br/>          \"price\": 0"
			+ "<br/>        }"
			+ "<br/>      ]"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	@PUT
	@ApiOperation(value="update Sub menu",notes="<pre><code>{"
			+ "<br/>  \"menuSubName\": \"Sub Menu Name\","
			+ "<br/>  \"subMenuDescription\": \"Sub Menu Description\","
			+ "<br/>  \"subMenuNotes\":\"update notes\","
			+ "<br/>  \"categories\": []"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"menuSubName\": \"Sub Name 1 sub menu\","
			+ "<br/>  \"subMenuDescription\": \"sub menu des\","
			+ "<br/>  \"subMenuNotes\":\"update notes\","
			+ "<br/>  \"categories\": ["
			+ "<br/>    {"
			+ "<br/>      \"id\": \"958c6d94-eee4-4a7a-aeb3-54ab4c49f00f\","
			+ "<br/>      \"items\": []"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"menuSubName\": \"Sub Name 1 sub menu\","
			+ "<br/>  \"subMenuDescription\": \"sub menu des\","
			+ "<br/>  \"subMenuNotes\":\"update notes\","
			+ "<br/>  \"categories\": ["
			+ "<br/>    {"
			+ "<br/>      \"id\": \"b467544f-f78f-4141-83b1-3ed9c902dbb0\","
			+ "<br/>      \"items\": ["
			+ "<br/>        {"
			+ "<br/>          \"id\": \"2823f44a-e225-4584-9f96-60b4857ca2b1\","
			+ "<br/>          \"price\": 0"
			+ "<br/>        },"
			+ "<br/>        {"
			+ "<br/>          \"id\": \"2823f44a-e225-4584-9f96-60b4857ca2b1\","
			+ "<br/>          \"price\": 0"
			+ "<br/>        }"
			+ "<br/>      ]"
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@Path("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 404, message = "submenu not found"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			SubMenu submenu = subMenuDao.findOne(id);
			if (submenu != null) {
				return super.update(access, id, dto);
			}
			else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("submenu not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation("delete sub menu by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 404, message = "submenu not found"),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			SubMenu submenu = subMenuDao.findOne(id);
			if (submenu != null) {
				return super.delete(access, id);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("submenu not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}



}
