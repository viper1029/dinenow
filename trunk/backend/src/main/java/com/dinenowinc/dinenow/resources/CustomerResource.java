package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.ArrayList;
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
import com.dinenowinc.dinenow.model.helpers.UserRole;
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

	@Inject
	private AddressBookDao addressBookDao;
	
	@Inject
	private CartDao cartDao;
	
	@Override
	protected HashMap<String, Object> getMapFromEntity(Customer entity) {
		HashMap<String, Object> dto = new HashMap<>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

	@Override
	protected Customer getEntityForUpdate(Customer customer, HashMap<String, Object> inputMap) {
		if(inputMap.containsKey("email")){
			customer.setEmail(inputMap.get("email").toString());
		}
		if(inputMap.containsKey("password")){
			customer.setPassword(MD5Hash.md5Spring(inputMap.get("password").toString()));
		}
		if(inputMap.containsKey("firstName")){
			customer.setFirstName(inputMap.get("firstName").toString());
		}
		if(inputMap.containsKey("lastName")){
			customer.setLastName(inputMap.get("lastName").toString());
		}
		if(inputMap.containsKey("phoneNumber")){
			if(!customer.getPhoneNumber().equals(inputMap.get("phoneNumber").toString()))
			{
			   customer.setIsPhoneNumberValid(false);
			   customer.setPhoneNumber(inputMap.get("phoneNumber").toString());
			}
		}
		return customer;
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

			LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
			List<Customer> entities = this.dao.getByPage(iPage, iSize);
			List<HashMap<String, Object>> dtos = getMapListFromEntities(entities);
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
//			return super.create(dto);
//		}else if (access.getRole() == UserRole.ADMIN) {
//			return super.create(access, dto);
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
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
		
		//if(access == null){
			//return super.add(dto);
		 if (access.getRole() == UserRole.ADMIN) {
			return super.update(access, id, inputMap);
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
			if (customerDao.get(id) != null) {
				List<Order> entities = orderDao.findByCustomer(id);
				List<HashMap<String, Object>> dtos = new ArrayList<>();
				for (Order dto : entities) {
					dtos.add(dto.toDto());
				}
				LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
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
			if (customerDao.get(customerId) != null) {
				 Order order = orderDao.findByCustomerAndOrder(customerId , orderId);
				LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
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
			Customer customer = customerDao.get(id);
			if (customer != null) {
				HashMap<String, Object> dto = new HashMap<>();
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
			Customer customer = customerDao.get(id);
			if (customer != null) {
				HashMap<String, Object> dto = new HashMap<>();
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
			Customer customer = customerDao.get(id);
			if (customer != null) {
				if (dto.containsKey("phoneNumber")) {
					if(!customer.getPhoneNumber().equals(dto.get("phoneNumber").toString()))
					{
						customer.setIsPhoneNumberValid(false);
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
				HashMap<String, Object> dtos = new HashMap<>();
				dto.put(getClassT().getSimpleName().toLowerCase(), customer.toDto());
				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
			}
		}

		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("not implement"));
	}
	

	private HashMap<String, Object> onGetOrder(Order entity) {
		HashMap<String, Object> dto = new LinkedHashMap<>();
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
		dto.put("postalCode", entity.getPostalCode());
		dto.put("orderType", entity.getOrderType());
		dto.put("receivedAt", entity.getReceivedTime());
		dto.put("expectedCompletionAt", entity.getExpectedCompletionTime());
		dto.put("completionAt", entity.getCompletionTime());
		dto.put("location", entity.getLocation() != null ? entity.getLocation() : "");
	//	dto.put("status", entity.getAvailstatus());
	//	Restaurant restaurantInfo = null;
		List<HashMap<String, Object>> orderdetails = new ArrayList<>();
		for (OrderDetail orderDetail : entity.getOrderDetails()) {
			HashMap<String, Object> order = new HashMap<>();
			List<Item> listItem = itemDao.getListItemByOrderDetails(orderDetail.getId());
			order.put("id", orderDetail.getId());
			order.put("unitPrice", orderDetail.getPrice());
			order.put("quantity", orderDetail.getQuantity());
			List<HashMap<String, Object>> items = new ArrayList<>();
			for (Item item : listItem) {
				HashMap<String, Object> itemtemp = new HashMap<>();
				itemtemp.put("id",item.getId());
				itemtemp.put("itemName",item.getName());
				itemtemp.put("itemDescription",item.getDescription());
				itemtemp.put("notes",item.getNotes());
				itemtemp.put("linkImage",item.getImage());
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
		customer.put("firstName",cus.getFullName());
		customer.put("lastName",cus.getLastName());
		customer.put("email",cus.getEmail());
		customer.put( "phoneNumber", cus.getPhoneNumber());
//		customer.put("address",cus.getAddress());
		dto.put("customer", customer);*/
		LinkedHashMap<String, Object> odto = new LinkedHashMap<>();
		odto.put("order", dto);
		return dto;
	}
	
	
	
	protected HashMap<String, Object> fromEntity(Cart entity) {
		LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
		dto.put("id", entity.getId());
		dto.put("orderType", entity.getOrderType());
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
