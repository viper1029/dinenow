package com.dinenowinc.dinenow.resources;

import io.dropwizard.auth.Auth;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import com.dinenowinc.dinenow.dao.CartDao;
import com.dinenowinc.dinenow.dao.CartItemDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.OrderDetailsDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Cart;
import com.dinenowinc.dinenow.model.CartItem;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.OrderStatus;
import com.dinenowinc.dinenow.model.OrderType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SizeInfo;
import com.dinenowinc.dinenow.model.UserRole;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/carts")
@Api("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResources extends AbstractResource<Cart> {
	
	
	@Inject
	CustomerDao customerDao;
	
	@Inject
	RestaurantDao restaurantDao;
	
	@Inject
	CartDao dao;
	
	@Inject
	CartItemDao cartItemDao;
	
	
	@Inject 
	private OrderDetailsDao orderDetailsDao;
	@Inject
	private ItemDao itemDao;
	@Inject 
	private OrderDao orderDao;

	@Override
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
		HashMap<String, Object> cartdto = new HashMap<String, Object>();
		cartdto.put(getClassT().getSimpleName().toLowerCase(), dto);
		return cartdto;
	}
	
	protected Cart fromFullDto(AccessToken access,HashMap<String, Object> dto) {
		
		double subtotal = 0;
	    double tip = 0;
		double tax = 0;
		double discount = 0;
		try{
		Customer cus = customerDao.findOne(access.getId().toString());
		System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMM"+cus.getCart());
		Cart co = (cus.getCart()!=null)?cus.getCart():new Cart();
		double total = co.getTotal();
		co.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
		co.setAvailstatus(AvailabilityStatus.AVAILABLE);
	//	co.setTotal(Double.parseDouble(dto.get("total").toString()));
		    
			List<HashMap<String, Object>> listItemSize = (List<HashMap<String, Object>>) dto.get("items");
			for (HashMap<String, Object> hashMap : listItemSize) {
				CartItem cartItem = new CartItem();
				cartItem.setCreatedBy("self");
				cartItem.setCreatedDate(new Date());
			//	cartItem.setPrice(Double.parseDouble(hashMap.get("price").toString()));
				cartItem.setQuantity(Integer.parseInt(hashMap.get("quantity").toString()));
				Item item = itemDao.findOne(hashMap.get("itemid").toString(),dto.get("restaurantId").toString());
				for(SizeInfo size : item.getSizes()){
					if(hashMap.get("sizeId").toString().equals(size.getId())){
						subtotal  = subtotal + cartItem.getQuantity()*size.getPrice();
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+subtotal);
						break;
					}
				}
				cartItem.setNote(item.getNotes());
				item.addCartItems(cartItem);
				cartItem.setCart(co);
				co.addCartItem(cartItem);
				cartItem.setPrice(subtotal);
			//	 tip = (subtotal*tip)/100;
			//	  tax = (subtotal*tax)/100;
			//	discount = (subtotal*discount)/100;
			}
			System.out.println(":::::::::::::::::::::::::::::::::"+total);
		total = total+ subtotal + tip + tax + discount;
		co.setTotal(total);
				Restaurant restaurant = restaurantDao.findOne(dto.get("restaurantId").toString());	
				restaurant.addCarts(co);
		return co;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	protected Response onDelete(AccessToken access, String id) {
		try{
			CartItem item = cartItemDao.findOne(id);
			Cart cart  = item.getCart();
			cart.setTotal(cart.getTotal()-item.getPrice());
			cart.getCartItems().remove(item);
		//	System.out.println(item);
		//	cartItemDao.delete(item);
			dao.update(cart);
			return ResourceUtils.asSuccessResponse(Status.OK, null);
		}
        catch(Exception e){
        	e.printStackTrace();
        	return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("entity not found"));
        }
	}
	
	@Override
	protected Response onAdd(AccessToken access, Cart entity, Restaurant restaurant) {
		//corrected
		try{
		Customer cus = customerDao.findOne(access.getId().toString());
		entity.setCreatedBy(cus.getFirstName());
		entity.setOrderStatus(OrderStatus.OPEN);
		cus.setCart(entity);
		entity.setCustomer(cus);
		dao.save(entity);
		customerDao.update(cus);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
		}catch(Exception e){
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("entity not found"));
		}
	}
	
	
	@GET
	@ApiOperation("api get all Customers Cart")
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth AccessToken access) {
		return super.getAll(access);
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of Customer Cart")
	@Override
	public Response get(@ApiParam(access = "internal") @Auth AccessToken access,@PathParam("id") String id) {
		return super.get(access, id);
	}
	
	
	@POST
	@ApiOperation(value="api add new Customer Order", notes="<pre><code>{"
			+ "<br/>    \"status\": \"AVAILABLE\","
			+ "<br/>    \"orderType\": \"PICKUP\","
			+ "<br/>    \"orderStatus\": \"OPEN\","
			+ "<br/>  }</code></pre>")
	@Override
	public Response add(@ApiParam(access = "internal") @Auth AccessToken access, HashMap<String, Object> dto) {
		System.out.println("@@@@@@@@@" + dto);
		if (access.getRole() == UserRole.CUSTOMER) {
			return onAdd(access, fromFullDto(access,dto), null);
		}
		return ResourceUtils.asSuccessResponse(Status.UNAUTHORIZED, "only for customer");
	}
	
	
/*	@PUT
	@ApiOperation(value="update Customer cart", notes="<pre><code>{"
			+ "<br/>    \"id\": \"7f8dab69-dd7b-4a10-bce9-f319e761dc74\","
			+ "<br/>    \"receivedAt\": 1423475871505,"
			+ "<br/>    \"status\": \"AVAILABLE\","
			+ "<br/>    \"orderType\": \"PICKUP\","
			+ "<br/>    \"orderNumber\": 1300,"
			+ "<br/>    \"expectedCompletionAt\": null,"
			+ "<br/>    \"orderStatus\": \"OPEN\","
			+ "<br/>    \"tip\": 0"
			+ "<br/>  }</code></pre>")
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto) {
		return super.update(access, id, dto);
	}*/
	
	
	@DELETE
	@Path("/{id}")
	@ApiOperation("delete Customer Order Detail by id")
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth AccessToken access, @PathParam("id") String id){
		if (access.getRole() == UserRole.CUSTOMER) {
			return onDelete(access, id);
		}
		return ResourceUtils.asSuccessResponse(Status.UNAUTHORIZED, "only for customer");
	}

	
}
