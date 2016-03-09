package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.ItemSize;
import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.OrderStatus;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

//@Path("/orderDetail")
//@Api("/orderDetail")
public class OrderDetailsResource extends AbstractResource<OrderDetail>{
	
	@Inject 
	private RestaurantDao restaurantDao;
	
	@Inject
	private OrderDao orderDao;
	
	@Inject
	private CustomerDao customerDao;
	
	@Inject
	private ItemDao itemDao;
	
	@Override
	protected HashMap<String, Object> getMapFromEntity(OrderDetail entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("price", entity.getPrice());
		dto.put("quantity", entity.getQuantity());
		return dto;
	}

	
	

	
	@Override
	protected Response onCreate(User access, OrderDetail entity, Restaurant restaurant) {
		return super.onCreate(access, entity, restaurant);
	}

	
	
	
	//=====================================METHOD=========================================//
	
	
	@GET
	@ApiOperation("api get all OrderDetails of restaurant for ADMIN and OWNER")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data") 
			})
	@Override
	public Response getAll(@Auth User access) {
		return super.getAll(access);
	}
	
	
	
	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of OrderDetails")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	@Override
	public Response get(@Auth User access,@PathParam("id") String id) {
		return super.get(access, id);
	}
	
	
	@POST
	@ApiOperation(value="api add new OrderDetails for restaurant", notes="<pre><code>{"
			+ "<br/>  \"unitPrice\": 56,"
			+ "<br/>  \"quantity\": 3,"
			+ "<br/>  \"deliveryFee\": 3,"
			+ "<br/>  \"tax\": 3,"
			+ "<br/>  \"tip\": 3,"
			+ "<br/>  \"subTotal\": 20,"
			+ "<br/>  \"total\": 25"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response create(@Auth User access, HashMap<String, Object> inputMap) {
		if (access.getRole() == UserRole.CUSTOMER) {
			return super.create(access, inputMap);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, "");
	}
	
	
	@PUT
	@ApiOperation(value="update OrderDetail", notes="<pre><code>{"
			+ "<br/>    \"total\": 25,"
			+ "<br/>    \"id\": \"d1b9b311-c879-482d-b8aa-2c1885230bd2\","
			+ "<br/>    \"tax\": 3,"
			+ "<br/>    \"subTotal\": 25,"
			+ "<br/>    \"quantity\": 3,"
			+ "<br/>    \"unitPrice\": 56,"
			+ "<br/>    \"tip\": 3,"
			+ "<br/>    \"deliveryFee\": 3"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Override
	public Response update(@Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
		return super.update(access, id, inputMap);
	}
	
	
	@DELETE
	@Path("/{id}")
	@ApiOperation("delete Order Detail by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity")
			})
	@Override
	public Response delete(@Auth User access, @PathParam("id") String id){
		return super.delete(access, id);
	}
	
	@OPTIONS
	@Path("/{restaurant_id}")
	@ApiOperation(value="Get All Order Details By Restaurant", notes="status=open/accepted/completed/scheduled/late <br/> from: 2015/01/01 <br/> to: 2015/03/19")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 400, message = "page not format number") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	public Response getOrderByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, @QueryParam("status") String status, @QueryParam("from") String from,@QueryParam("to") String to, @QueryParam("page") String page, @QueryParam("size") String size)
	{	

		int iPage = 1;
		int iSize = 50;
		
		if (restaurantDao.get(restaurant_id) != null) {

			if (page != null) {
				try {
					iPage = Integer.parseInt(page);
				} catch (Exception e) {
					return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
							new ServiceErrorMessage("page not format number"));
				}
			}
			if (size != null) {
				try {
					iSize = Integer.parseInt(size);
				} catch (Exception e) {
					return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
							new ServiceErrorMessage("size not format number"));
				}
			}
			List<Order> entities = new ArrayList<Order>();
			if (status != null && !status.equals("")) {
				// have Status
				OrderStatus orderstatus = OrderStatus.OPEN;
				if (status.toLowerCase().equals("open")) {
					orderstatus = OrderStatus.OPEN;
				}
				if (status.toLowerCase().equals("accepted")) {
					orderstatus = OrderStatus.ACCEPTED;
				}
				if (status.toLowerCase().equals("completed")) {
					orderstatus = OrderStatus.COMPLETED;
				}
				if (status.toLowerCase().equals("scheduled")) {
					orderstatus = OrderStatus.SCHEDULED;
				}
				if (status.toLowerCase().equals("late")) {
					orderstatus = OrderStatus.LATE;
				}

				if (from != null && to != null) {
					// have from - to
					//entities = orderDao.getAddonsByRestaurantId(restaurant_id, orderstatus, from, to, iPage, iSize);
				} else {
					entities = orderDao.getListByRestaurant(restaurant_id, orderstatus, iPage, iSize);
				}
			} else {
				// status == null
				if (from != null && to != null) {
					// have from - to
					//entities = orderDao.getAddonsByRestaurantId(restaurant_id, from, to, iPage, iSize);
				} else {
					entities = orderDao.getListByRestaurant(restaurant_id, iPage, iSize);
					
				}
			}
			List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
			for (Order dto : entities) {
				dtos.add(onGet(dto));
			}
			return ResourceUtils.asSuccessResponse(Status.OK, dtos);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@GET
	@Path("/search_orders/{restaurant_id}")
	@ApiOperation(value="Search Order of Restaurant")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 400, message = "page not format number") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	public Response searchByOrder(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, @QueryParam("query") String query){
		if (restaurantDao.get(restaurant_id) != null) {
			if (query != null) {
				List<Order> entities = new ArrayList<Order>();
				entities = orderDao.searchByOrder(restaurant_id, query);
				List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
				for (Order dto : entities) {
					dtos.add(onGet(dto));
				}
				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
			}else {
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("query is not null"));
			}
		}else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}

	}
	
	private HashMap<String, Object> onGet(Order entity) {
		HashMap<String, Object> uti = entity.toDto();
		

		List<HashMap<String, Object>> orderdetails = new ArrayList<HashMap<String,Object>>();
		for (OrderDetail orderDetail : entity.getOrderDetails()) {
			HashMap<String, Object> order = new HashMap<String, Object>();
			List<Item> listItem = itemDao.getListItemByOrderDetails(orderDetail.getId());
			
			order.put("id", orderDetail.getId());
			order.put("unitPrice", orderDetail.getPrice());
			order.put("quantity", orderDetail.getQuantity());
			
			List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
			for (Item item : listItem) {
				HashMap<String, Object> itemtemp = new HashMap<String, Object>();
				itemtemp.put("id",item.getId());
				itemtemp.put("itemName",item.getName());
				itemtemp.put("itemDescription",item.getDescription());
				itemtemp.put("notes",item.getNotes());
				itemtemp.put("linkImage",item.getImage());
				
				Set<HashMap<String, Object>> sizeDtos = new HashSet<HashMap<String, Object>>();
				for (ItemSize itemSize : item.getItemSizes()) {
					Size size = itemSize.getSize();
					HashMap<String, Object> sizeDto = new HashMap<String, Object>();
					sizeDto.put("id", size.getId());
					sizeDto.put("sizeName", size.getSizeName());
					sizeDto.put("sizeDescription", size.getSizeDescription());
					sizeDto.put("price", itemSize.getPrice());
					sizeDtos.add(sizeDto);
				}
				itemtemp.put("sizes", sizeDtos);
				
				
				
				items.add(itemtemp);
			}
			order.put("items", items);
			orderdetails.add(order);
		}
		
		uti.put("orderDetails", orderdetails);
		
		Customer cus = customerDao.findByOrder(entity.getId().toString());
		HashMap<String, Object> customer = new HashMap<String, Object>();
		customer.put("id",cus.getId());
		customer.put("firstName",cus.getFirstName());
		customer.put("lastName",cus.getLastName());
		customer.put("email",cus.getEmail());
		customer.put( "phoneNumber", cus.getPhoneNumber());
		
		uti.put("customer", customer);
		return uti;
	}

}
