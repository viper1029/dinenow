package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;



import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.AddressBookDao;
import com.dinenowinc.dinenow.dao.CartDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Cart;
import com.dinenowinc.dinenow.model.CartItem;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.utils.MD5Hash;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/customers")
@Api("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource extends AbstractResource<Customer>{
	
	//@Auth
	//User access;
	
	@Inject
	private AddressBookDao addressBookDao;
	
	@Inject
	private CartDao cartDao;
	
	@Override
	protected HashMap<String, Object> fromEntity(Customer entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

/*	@Override
	protected List<HashMap<String, Object>> fromEntities(List<Customer> entities)	{
		List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
		for (Customer entity : entities) {
			dtos.add(fromEntity(entity));			
		}
		return dtos;
		
	}*/
	//POST/PUT
	@Override
	protected Customer fromFullDto(HashMap<String, Object> dto) {
		Customer entity = super.fromFullDto(dto);
		entity.setEmail(dto.get("email").toString());
		entity.setPassword(dto.get("password").toString());
		entity.setFirstName(dto.get("firstName").toString());
		entity.setLastName(dto.get("lastName").toString());
		entity.setPhoneNumber(dto.get("phoneNumber").toString());
		Cart cart = new Cart();
		cart.setCreatedBy(dto.get("firstName").toString());
		cart.setCreatedDate(new Date());
		cart.setCustomer(entity);
		entity.setCart(cart);
		cartDao.save(cart);
		return entity;
	}
	
	
	
	@Override
	protected Customer fromUpdateDto(Customer t, HashMap<String, Object> dto) {
		System.out.println(dto.entrySet());
		Customer entity = super.fromUpdateDto(t, dto);
//		if(dto.containsKey("address")){
//			entity.setAddress(dto.get("address").toString());
//		}
		if(dto.containsKey("email")){
			entity.setEmail(dto.get("email").toString());
		}
		if(dto.containsKey("password")){
			entity.setPassword(MD5Hash.md5Spring(dto.get("password").toString()));
		}
		if(dto.containsKey("firstName")){
			entity.setFirstName(dto.get("firstName").toString());
		}
		if(dto.containsKey("lastName")){
			entity.setLastName(dto.get("lastName").toString());
		}
		if(dto.containsKey("phoneNumber")){
			if(!entity.getPhoneNumber().equals(dto.get("phoneNumber").toString()))
			{
			   entity.setPhone_number_valid(false);
			   entity.setPhoneNumber(dto.get("phoneNumber").toString());
			}
		}
		return entity;
	}
	

	

	@GET
	@ApiOperation("get all customer for ADMIN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 400, message = "page not format number"),
			@ApiResponse(code = 400, message = "size not format number"),
			})
	public Response getAll(@ApiParam(access = "internal") @Auth User access, @QueryParam("page") String page, @QueryParam("size") String size) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			int iPage = 1;
			int iSize = 50;
			
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

			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			List<Customer> entities = this.dao.findAll(iPage, iSize);	
			List<HashMap<String, Object>> dtos = fromEntities(entities);
			dto.put(getClassT().getSimpleName().toLowerCase()+'s', dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation(value="get detail of customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") ,
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access,@PathParam("id") String id) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			return super.get(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
    // No need to use 
	@POST
	@ApiOperation(value="api add new customer", notes="	{"
			+ "<br/>  \"id\": \"\","
			+ "<br/>  \"address\": \"\","
			+ "<br/>  \"addressBooks\": \"\","
			+ "<br/>  \"email\": \"\","
			+ "<br/>  \"firstName\": \"\","
			+ "<br/>  \"lastName\": \"\","
			+ "<br/>  \"phoneNumber\": \"\","
			+ "<br/>  \"recentUseAddresses\": \"\","
			+ "<br/>  \"socialAccounts\": \"\""
			+ "<br/>}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	//@Override
	public Response add(HashMap<String, Object> dto) {
		System.out.println("%%%%%%%%%%%%%%%%%%%%" + dto);
//		if(access == null){
//			return super.add(dto);
//		}else if (access.getRole() == UserRole.ADMIN) {
//			return super.add(access, dto);
//		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}
	
	
	@PUT
	@ApiOperation(value="api update customer", notes="NOT YET IMPLEMENTS")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		
		if(access == null){
			return super.add(dto);
		}else if (access.getRole() == UserRole.ADMIN) {
			return super.update(access, id, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Only for Admin"));
	}
	
	
/*	@DELETE
	@Path("/{id}")
	@ApiOperation(value="api delete customer", notes="NOT YET IMPLEMENTS")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		return super.delete(access, id);
	}*/
	
	@Inject
	private OrderDao orderDao;
	@Inject
	private ItemDao itemDao;
	@Inject
	private CustomerDao customerDao;
	@Inject
	private RestaurantDao restaurantDao;
	
	@GET
	@ApiOperation("get order by customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer"),
			@ApiResponse(code = 404, message = "customer not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Path("/{id}/order_details")
	public Response getOrder(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.CUSTOMER) {
			if (customerDao.findOne(id) != null) {
				List<Order> entities = orderDao.findByCustomer(id);
				List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String,Object>>();
				for (Order dto : entities) {
					dtos.add(dto.toDto());
				}
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("orders", dtos);
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("customer not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
	}
	

	@GET
	@ApiOperation("get order detail by customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer"),
			@ApiResponse(code = 404, message = "customer not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Path("/{customerId}/order_details/{orderId}")
	public Response getOrderDetail(@ApiParam(access = "internal") @Auth User access, @PathParam("customerId") String customerId
			, @PathParam("orderId") String orderId){
		if (access.getRole() == UserRole.CUSTOMER) {
			if (customerDao.findOne(customerId) != null) {
				 Order order = orderDao.findByCustomerAndOrder(customerId , orderId);
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("order", onGetOrder(order));
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("customer not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
	}
	
	@GET
	@ApiOperation("get cart by customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer"),
			@ApiResponse(code = 404, message = "customer not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Path("/{id}/cart")
	public Response getCart(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.CUSTOMER) {
			Customer customer = customerDao.findOne(id);
			if (customer != null) {
				HashMap<String, Object> dto = new HashMap<String, Object>();
				dto.put("cart", fromEntity(customer.getCart()));
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("customer not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
	}
	
	@GET
	@ApiOperation("get addressbook by customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer"),
			@ApiResponse(code = 404, message = "customer not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Path("/{id}/address_book")
	public Response getAddressBook(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		if (access.getRole() == UserRole.CUSTOMER) {
			Customer customer = customerDao.findOne(id);
			if (customer != null) {
				HashMap<String, Object> dto = new HashMap<String, Object>();
				dto.put("addressbooks", customer.getAddressBooks());
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
			}else {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("customer not found"));
			}
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
	}
	
	
	@PUT
	@ApiOperation( value="edit by customer", notes="<pre><code>{"
			+ "<br/>  \"firstName\":\"abc\","
			+ "<br/>  \"lastName\":\"abc\""
			+ "<br/>}"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "{"
			+ "<br/>  \"email\":\"abc@gmail.com\","
			+ "<br/>  \"newEmail\":\"123@gmail.com\""
			+ "<br/>}"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "{"
			+ "<br/>  \"password\":\"123456\","
			+ "<br/>  \"newPassword\":\"12345678\""
			+ "<br/>}"
			+ "<br/>"
			+ "<br/>"
			+ "<br/></code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer"),
			@ApiResponse(code = 404, message = "customer not found"),
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Path("/{id}/edit")
	public Response editCustomer(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto){
		if (access.getRole() == UserRole.CUSTOMER) {
			Customer customer = customerDao.findOne(id);
			if (customer != null) {
				if (dto.containsKey("phoneNumber")) {
					if(!customer.getPhoneNumber().equals(dto.get("phoneNumber").toString()))
					{
						customer.setPhone_number_valid(false);
						customer.setPhoneNumber(dto.get("phoneNumber").toString());
					}
					
				}
				if (dto.containsKey("firstName") && dto.containsKey("lastName")) {
					customer.setFirstName(dto.get("firstName").toString());
					customer.setLastName(dto.get("lastName").toString());
				}
				if (dto.containsKey("email")) {
					if (customer.getEmail().equals(dto.get("email").toString())) {
						String newemail = dto.get("newEmail").toString();
						customer.setEmail(newemail);
					}else {
						return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("email current is incorrect"));
					}
				}
				if (dto.containsKey("password") && dto.containsKey("newPassword")) {
					if (customer.getPassword().equals(dto.get("password").toString())) {
						Customer uCustomer = customerDao.checkByPassword(MD5Hash.md5Spring(dto.get("password").toString()));
						if(uCustomer == null)
							return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Invalid password"));
						
						String newPasword = dto.get("newPassword").toString();
						customer.setPassword(MD5Hash.md5Spring(newPasword));
					}else {
						return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("password current is incorrect"));
					}
				}
				customerDao.update(customer);
				HashMap<String, Object> dtos = new HashMap<String, Object>();
				dto.put(getClassT().getSimpleName().toLowerCase(), customer.toDto());
				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
			}
		}

		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("not implement"));
	}
	

	private HashMap<String, Object> onGetOrder(Order entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("orderNumber", entity.getOrderNumber());
	//	dto.put("paymentAt", this.getPaymentAt());
		dto.put("total", entity.getTotal());
		dto.put("tip", entity.getTip());
		dto.put("tax", entity.getTax());
		dto.put("discount", entity.getDiscount());
		dto.put("orderStatus", entity.getOrderStatus());
		dto.put("orderType", entity.getOrderType());
		dto.put("address1", entity.getAddress_1());
		dto.put("address2", entity.getAddress_2());
		dto.put("city", entity.getCity());
		dto.put("country", entity.getCountry());
		dto.put("province", entity.getProvince());
		dto.put("postalCode", entity.getPostal_code());
		dto.put("orderType", entity.getOrderType());
		dto.put("receivedAt", entity.getReceivedAt());
		dto.put("expectedCompletionAt", entity.getExpectedCompletionAt());
		dto.put("completionAt", entity.getCompletionAt());
		dto.put("location", entity.getLocation() != null ? entity.getLocation() : "");
	//	dto.put("status", entity.getAvailstatus());
	//	Restaurant restaurantInfo = null;
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
				itemtemp.put("availabilityStatus",item.getAvailabilityStatus());
				itemtemp.put("itemName",item.getItemName());
				itemtemp.put("itemDescription",item.getItemDescription());
				itemtemp.put("notes",item.getNotes());
				itemtemp.put("spiceLevel",item.getSpiceLevel());
				itemtemp.put("linkImage",item.getLinkImage());
				itemtemp.put("isVegeterian",item.isVegeterian());
				items.add(itemtemp);
//				if (restaurantInfo == null) {
//					restaurantInfo = restaurantDao.findByItemId(item.getId());
//					dto.put("restaurant", restaurantInfo.toDto());
//				}
			}
			order.put("items", items);
			orderdetails.add(order);
		}
		dto.put("orderDetails", orderdetails);
		
/*		Customer cus = customerDao.findByOrder(entity.getId().toString());
		HashMap<String, Object> customer = new HashMap<String, Object>();
		customer.put("id",cus.getId());
		customer.put("firstName",cus.getFirstName());
		customer.put("lastName",cus.getLastName());
		customer.put("email",cus.getEmail());
		customer.put( "phoneNumber", cus.getPhoneNumber());
//		customer.put("address",cus.getAddress());
		dto.put("customer", customer);*/
		LinkedHashMap<String, Object> odto = new LinkedHashMap<String, Object>();
		odto.put("order", dto);
		return dto;
	}
	
	
	
	protected HashMap<String, Object> fromEntity(Cart entity) {
		LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("orderType", entity.getOrderType());
		dto.put("status", entity.getAvailstatus());
		dto.put("total", entity.getTotal());
		dto.put("tax", entity.getTax());
		dto.put("orderStatus", entity.getOrderStatus());
		List<HashMap<String, Object>> items = new LinkedList<>();
		System.out.println("::::::::::::::::::::::"+entity.getCartItems().size());
		for(CartItem item : entity.getCartItems()){
			HashMap<String, Object> itemdto = new HashMap<>();
			itemdto.put("id", item.getId());
			itemdto.put("price", item.getPrice());
			itemdto.put("quantity", item.getQuantity());
			itemdto.put("notes", item.getNote());
			itemdto.put("itemid",itemDao.getListItemByCartItems(item.getId()).getId());
			items.add(itemdto);
		}
		dto.put("cartItems", items);
		
		return dto;
	}
	
	
}
