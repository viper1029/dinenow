package com.dinenowinc.dinenow.resources;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dinenowinc.dinenow.dao.CartDao;
import com.dinenowinc.dinenow.dao.CartItemDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.OrderDetailsDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.model.Cart;
import com.dinenowinc.dinenow.model.CartItem;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;

@Path("/cart_item")
@Api("/carts_item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartItemResources extends AbstractResource<CartItem>{
	
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
	protected HashMap<String, Object> getMapFromEntity(CartItem entity) {
		LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
/*		dto.put("id", entity.getId());
		dto.put("orderType", entity.getOrderType());
		dto.put("status", entity.getAvailstatus());
		dto.put("total", entity.getTotal());
		dto.put("tax", entity.getTaxes());
		dto.put("orderStatus", entity.getOrderStatus());
		List<HashMap<String, Object>> items = new LinkedList<>();
		System.out.println("::::::::::::::::::::::"+entity.getCartItems().size());
		for(CartItem item : entity.getCartItems()){
			HashMap<String, Object> itemdto = new HashMap<>();
			itemdto.put("id", item.getId());
			itemdto.put("price", item.getPrice());
			itemdto.put("quantity", item.getQuantity());
			itemdto.put("notes", item.getNote());
			items.create(itemdto);
		}
		dto.put("cartItems", items);*/
		
		return dto;
	}
	

}
