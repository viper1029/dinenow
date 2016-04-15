package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.OrderDetailsDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.helpers.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.helpers.Hour;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.helpers.LatLng;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.helpers.OrderStatus;
import com.dinenowinc.dinenow.model.helpers.OrderType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.ItemSize;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.utils.Utils;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;



@Path("/order_details")
@Api("/order_details")
public class CustomerOrderResource extends AbstractResource<Order>{

	@Inject
	private CustomerDao customerDao;

	@Inject
	private RestaurantDao restaurantDao;

	//POST/PUT
//	@Override
//	protected Order fromFullDto(HashMap<String, Object> dto) {
//		Order co = new Order();
//		if (!dto.containsKey("id")) {
//			co.setReceivedAt(new Date());
//		}
//		co.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
//		co.setAvailstatus(AvailabilityStatus.valueOf(dto.get("orderStatus").toString()));
//		co.setExpectedCompletionAt(new Date(Long.parseLong(dto.get("expectedCompletionAt").toString())));
//		co.setTip(Double.parseDouble(dto.get("tip").toString()));
//		co.setTotal(Double.parseDouble(dto.get("total").toString()));
//		if (dto.containsKey("location")) {
//			HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("location");
//			double lat = loca.get("lat");
//			double lng = loca.get("lng");
//			co.setLocation(new LatLng(lat, lng));
//		}
//		co.setAddress1(dto.get("address1").toString());
//		co.setAddress2(dto.get("address2").toString());
//		co.setCity(dto.get("city").toString());
//		co.setProvince(dto.get("provinence").toString());
//		co.setCountry(dto.get("country").toString());
//		co.setPostal_code(dto.get("postal_code").toString());
//
//
//		if (dto.containsKey("orderDetails")) {
//			ArrayList<ItemSizeInfo> listInfo = new ArrayList<ItemSizeInfo>();
//
//			List<HashMap<String, Object>> listItemSize = (List<HashMap<String, Object>>) dto.get("orderDetails");
//			for (HashMap<String, Object> hashMap : listItemSize) {
//				OrderDetail orderDetail = new OrderDetail();
//				orderDetail.setCreatedBy("Auto");
//				orderDetail.setCreatedDate(new Date());
//				orderDetail.setPrice(Double.parseDouble(hashMap.get("price").toString()));
//				orderDetail.setPrice(Integer.parseInt(hashMap.get("quantity").toString()));
//				Item item = itemDao.get(hashMap.get("item").toString(),dto.get("restaurantId").toString());
//				item.addOrderDetails(orderDetail);
//				co.addOrderDetail(orderDetail);
//			}
//		}
//
//		try {
//			Properties prop = new Properties();
//
//			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
//
//			if (inputStream != null) {
//				prop.load(inputStream);
//			} else {
//				throw new FileNotFoundException("property file not found in the classpath");
//
//			}
//
//			String number = prop.getProperty("OrderNumber");
//			System.out.println("-----------------" + number);
//			Order orderNum = orderDao.getLastOrder();
//			if(orderNum == null)
//				co.setOrderNumber(Long.parseLong(number)+1);
//			else
//				co.setOrderNumber(orderNum.getOrderNumber() + 1);
//
//			inputStream.close();
//
//		} catch (Exception e) {
//			System.out.println("Exception: " + e);
//		} 
//
//		/*	//	 Point p = new Point();
//		    int total= (int) (Double.parseDouble(dto.get("total").toString())*10);
//			p.setPoints(total);
//			p.setCreatedBy("custumer_order");
//			p.setCreatedDate(new Date());
//			p.setStatus(0);
//			co.addPoints(p);*/
//		if (dto.containsKey("restaurantId")) {
//			Restaurant restaurant = restaurantDao.get(dto.get("restaurantId").toString());
//			restaurant.addOrder(co);
//		}
//		return co;
//	}


	@Override
	protected HashMap<String, Object> getMapFromEntity(Order entity) {
		HashMap<String, Object> dto = new HashMap<>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Order getEntityForUpdate(Order order, HashMap<String, Object> inputMap) {
		if (!inputMap.containsKey("id")) {
			order.setReceivedTime(new Date());
		}
		order.setOrderStatus(inputMap.containsKey("orderStatus") ? OrderStatus.valueOf(inputMap.get("orderStatus").toString()) : order.getOrderStatus());
		order.setOrderType(inputMap.containsKey("orderType") ? OrderType.valueOf(inputMap.get("orderType").toString()) : order.getOrderType());
		order.setTip(inputMap.containsKey("tip") ? Double.parseDouble(inputMap.get("tip").toString()) : order.getTip());
		order.setTotal(inputMap.containsKey("total") ? Double.parseDouble(inputMap.get("total").toString()): order.getTotal());
		if (inputMap.containsKey("expectedCompletionAt") && inputMap.get("expectedCompletionAt") != null) {
			order.setExpectedCompletionTime(new Date(Long.parseLong(inputMap.get("expectedCompletionAt").toString())));
		}
		if (inputMap.containsKey("completionAt") && inputMap.get("completionAt") != null) {
			order.setCompletionTime(new Date(Long.parseLong(inputMap.get("completionAt").toString())));
		}
		if (inputMap.containsKey("location")) {
			HashMap<String, Double> loca = (HashMap<String, Double>) inputMap.get("location");
			double lat = loca.get("lat");
			double lng = loca.get("lng");
			order.setLocation(new LatLng(lat, lng));
		}
		return order;
	}



	//======================================ACTION======================================//


	@Override
	protected HashMap<String, Object> onGet(Order entity, User access) {
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
		List<HashMap<String, Object>> orderdetails = new ArrayList<>();
		for (OrderDetail orderDetail : entity.getOrderDetails()) {
			HashMap<String, Object> order = new HashMap<>();
			List<Item> listItem = itemDao.getListItemByOrderDetails(orderDetail.getId());
			order.put("id", orderDetail.getId());
			order.put("unitPrice", orderDetail.getPrice());
			order.put("quantity", orderDetail.getQuantity());
			List<HashMap<String, Object>> items = new ArrayList<>();
			for (Item item : listItem) {
				HashMap<String, Object> itemtemp = new LinkedHashMap<>();
				itemtemp.put("id",item.getId());
				itemtemp.put("itemName",item.getName());
				itemtemp.put("itemDescription",item.getDescription());
				itemtemp.put("notes",item.getNotes());
				itemtemp.put("linkImage",item.getImage());
				items.add(itemtemp);
			}
			order.put("items", items);
			orderdetails.add(order);
		}
		dto.put("orderDetails", orderdetails);
		return dto;
	}

	@Override
	protected Response onCreate(User access, Order entity, Restaurant restaurant) {
		//corrected
		Customer cus = customerDao.get(access.getId().toString());
		entity.setCreatedBy(cus.getLastName());
		entity.setOrderStatus(OrderStatus.OPEN);
		cus.addCustomerOrder(entity);
		dao.save(entity);
		customerDao.update(cus);
		return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(entity));
	}




	//=====================================METHOD=========================================//

	/*
	 *
			{
			  "items": [
			    {
			      "id": "025bdda2-642b-47b0-8650-75e004c37568",
			      "quantity": 2,
			      "addons": [
			        "id",
			        "id"
			      ]
			    }
			  ],
			  "tip": 0,
			  "orderType": "DELIVERY"
			}
	 */

	@Inject 
	private OrderDetailsDao orderDetailsDao;
	@Inject
	private ItemDao itemDao;
	@Inject 
	private OrderDao orderDao;


	@POST
	@Path("/checkout")
	@ApiOperation(value="api CheckOut for Customer", notes="<pre><code>{"
			+ "<br/>  \"items\": ["
			+ "<br/>    {"
			+ "<br/>      \"id\": \"025bdda2-642b-47b0-8650-75e004c37568\","
			+ "<br/>      \"quantity\": 2,"
			+ "<br/>      \"addons\": ["
			+ "<br/>        \"id add on\","
			+ "<br/>        \"id add on\""
			+ "<br/>      ]"
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"tip\": 0,"
			+ "<br/>  \"orderType\": \"DELIVERY\""
			+ "<br/>}</code></pre>")
	public Response checkOut(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto){ System.out.println(access.getRole());

	double subtotal = 0;
	double tip = 0;
	double tax = 0;
	double discount = 0;	

	try {

		System.out.println("///////////////////////////////////////////////");
		if (access.getRole() == UserRole.CUSTOMER) {

			if (!dto.containsKey("restaurantId")) {
				return ResourceUtils.asSuccessResponse(Status.NOT_ACCEPTABLE, "missing field resturantId");
			}
			Restaurant restaurant = restaurantDao.get(dto.get("restaurantId").toString());
			if(restaurant.getNetworkStatus() == NetworkStatus.OFFLINE){
				return ResourceUtils.asSuccessResponse(Status.NOT_ACCEPTABLE, "Reastaurant is OFFLINE");						
			}
			if(!dto.containsKey("orderType")){
				return ResourceUtils.asSuccessResponse(Status.NOT_ACCEPTABLE, "missing field orderType");
			}
			Customer cus = customerDao.get(access.getId().toString());
			Order co = new Order();
			co.setOrderStatus(OrderStatus.OPEN);

			OrderType type = OrderType.valueOf(dto.get("orderType").toString());
			co.setOrderType(type);
			co.setTip(Double.parseDouble(dto.get("tip").toString()));
			co.setTotal(Double.parseDouble(dto.get("total").toString()));

			if(type.name().equals("DELIVERY")){
				HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("location");
				double lat = loca.get("lat");
				double lng = loca.get("lng");
				boolean ava = false;
				for(DeliveryZone zone : restaurant.getDeliveryZone()){
					final GeometryFactory gf = new GeometryFactory();
					final Coordinate coord = new Coordinate(lat, lng);
					final Point point = gf.createPoint(coord);
					ava = point.within(zone.getCoordinates());
				}
				if(!ava){
					return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("delivery is not available at this zone"));
				}
				ArrayList<Hour> hours = restaurant.getAcceptDeliveryHours();
				for(Hour hour :hours){
					Date serverDate = Utils.convertTimeZones(TimeZone.getDefault().getID(),restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(new Date()));
					Date restfromDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getFromTime()));
					Date resttoDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getToTime()));
					
					if (restfromDate.getTime() <= serverDate.getTime() 
							&& resttoDate.getTime() >= (serverDate.getTime()+Utils.TENMINUTE)) 
					{ 
						co.setReceivedTime(serverDate);
						//co.setReceivedAt(Utils.GetUTCdatetimeAsDate());//for storing utc
						break;
					}
				}
				if(co.getReceivedTime()==null){
					return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("delivery is not available at this hour"));
				}
			} else 	if(type.name().equals("OUT")){
				ArrayList<Hour> hours = restaurant.getAcceptTakeOutHours();
				for(Hour hour :hours){
					Date serverDate = Utils.convertTimeZones(TimeZone.getDefault().getID(),restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(new Date()));
					Date restfromDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getFromTime()));
					Date resttoDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getToTime()));
					
					System.out.println(serverDate + "    "+restfromDate+" "+resttoDate);
					if (restfromDate.getTime() <= serverDate.getTime() 
							&& resttoDate.getTime() >= (serverDate.getTime()+Utils.TENMINUTE)) 
					{ 
						co.setReceivedTime(serverDate);
						//co.setReceivedAt(Utils.GetUTCdatetimeAsDate());//for storing utc
						break;
					}
				}
				if(co.getReceivedTime()==null){
					return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("takeout is not available at this hour"));
				}
			} else 	{
				ArrayList<Hour> hours = restaurant.getDineInHours();
				
//				for weekly schedule
//				for(Hour hour :hours){
//					SimpleDateFormat printFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//					Date serverDate = Utils.convertTimeZones(TimeZone.getDefault().getID(),restaurant.getTimezoneId(), printFormat.format(new Date()));
//					Date restfromDate = Utils.convertTimeZones("UTC",restaurant.getTimezoneId(), printFormat.format(hour.getFromTime()));
//					Date resttoDate = Utils.convertTimeZones("UTC",restaurant.getTimezoneId(), printFormat.format(hour.getToTime()));
//				//	System.out.println(serverDate + "    "+restfromDate+" "+resttoDate);
//					if (DateTimeFormat.forPattern("EEEE").print(new LocalDate()).toString().toUpperCase().matches(hour.getWeekDayType().name()+"(.*)")
//							&& Utils.getTimeInLongByDate(restfromDate) <= Utils.getTimeInLongByDate(serverDate) 
//							&& Utils.getTimeInLongByDate(resttoDate) >= (Utils.getTimeInLongByDate((serverDate))+Utils.ONEMINUTE*10)) 
//					{ //Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
//						co.setReceivedAt(Utils.GetUTCdatetimeAsDate());
//						break;
//					}
//				}
				
				
				for(Hour hour :hours){
					Date serverDate = Utils.convertTimeZones(TimeZone.getDefault().getID(),restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(new Date()));
					Date restfromDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getFromTime()));
					Date resttoDate = Utils.convertTimeZones(Utils.UTC,restaurant.getTimezoneId(), Utils.DATEMORMAT1.format(hour.getToTime()));
					
					if (restfromDate.getTime() <= serverDate.getTime() 
							&& resttoDate.getTime() >= (serverDate.getTime()+Utils.TENMINUTE)) 
					{ 
						co.setReceivedTime(serverDate);
						//co.setReceivedAt(Utils.GetUTCdatetimeAsDate());//for storing utc
						break;
					}
				}
				if(co.getReceivedTime()==null){
					return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("dinein is not available at this hour"));
				}
			}

			co.setAddress_1(dto.get("address1").toString());
			co.setAddress_2(dto.get("address2").toString());
			co.setCity(dto.get("city").toString());
			co.setProvince(dto.get("province").toString());
			co.setCountry(dto.get("country").toString());
			co.setPostalCode(dto.get("postal_code").toString());
			co.setTax(dto.containsKey("tax") ? Double.parseDouble( dto.get("tax").toString()):co.getTax());
			co.setCreatedBy(cus.getLastName());

			if (dto.containsKey("items")) {
				ArrayList<HashMap<String, Object>> items = (ArrayList<HashMap<String, Object>>)dto.get("items");
				for (HashMap<String, Object> hashMap : items) {
					OrderDetail od = new OrderDetail();
					int i = Integer.parseInt(hashMap.get("quantity").toString());
					od.setQuantity(i);
					od.setCreatedBy(cus.getLastName());
					od.setCreatedDate(new Date());
					Item item = itemDao.findOne(hashMap.get("itemId").toString(),dto.get("restaurantId").toString());
					for(ItemSize size : item.getItemSizes()){
						if(hashMap.get("sizeId").toString().equals(size.getSize().getId())){
							subtotal  = subtotal + od.getQuantity()*size.getPrice();
							break;
						}
					}
					//item.addOrderDetails(od); //TODO: temp comment

					co.addOrderDetail(od);
					//	 tip = (subtotal*tip)/100;
					//	  tax = (subtotal*tax)/100;
					//	discount = (subtotal*discount)/100;
				}
			}

			if (dto.containsKey("location")) {
				HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("location");
				double lat = loca.get("lat");
				double lng = loca.get("lng");
				co.setLocation(new LatLng(lat, lng));
			}

			try {
				Properties prop = new Properties();
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
				if (inputStream != null) {
					prop.load(inputStream);
				} else {
					throw new FileNotFoundException("property file not found in the classpath");
				}
				String number = prop.getProperty("OrderNumber");
				Order orderNum = orderDao.getLastOrder();
				if(orderNum == null)
					co.setOrderNumber(Long.parseLong(number)+1);
				else
					co.setOrderNumber(orderNum.getOrderNumber() + 1);
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception: " + e);
			} 


			double r_points = dto.containsKey("redeemed_points")?Double.parseDouble(dto.get("redeemed_points").toString()):0;
			if(cus.getPoint()>=r_points){
				cus.setPoint(cus.getPoint()-r_points);
			}
			co.setPoint(co.getTotal());

			cus.setPoint(cus.getPoint()+co.getPoint());
			cus.addCustomerOrder(co);
			restaurant.addOrder(co);
			dao.save(co);
			customerDao.update(cus);

			return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(co));
		}else {
			return ResourceUtils.asSuccessResponse(Status.UNAUTHORIZED, "only for customer");
		}
	} catch (Exception e) { 
		e.printStackTrace();
		return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, e.getMessage());
	}

	}


	@GET
	@ApiOperation("api get all Customer Order")
	@Override
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
		return super.getAll(access);
	}



	@GET
	@Path("/{id}")
	@ApiOperation("api get detail of Customer Order")
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access,@PathParam("id") String id) {
		return super.get(access, id);
	}

	/*	
	@POST
	@ApiOperation(value="api create new Customer Order", notes="<pre><code>{"
			+ "<br/>    \"receivedAt\": 1423475871505,"
			+ "<br/>    \"status\": \"AVAILABLE\","
			+ "<br/>    \"orderType\": \"PICKUP\","
			+ "<br/>    \"orderNumber\": 1300,"
			+ "<br/>    \"expectedCompletionAt\": null,"
			+ "<br/>    \"orderStatus\": \"OPEN\","
			+ "<br/>    \"tip\": 0"
			+ "<br/>  }</code></pre>")
	@Override
	public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		System.out.println("@@@@@@@@@" + dto);
		if (access.getRole() == UserRole.CUSTOMER) {
			return onCreate(access, fromFullDto(dto), null);
			//return create(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, "only for customer");
	}*/


	@PUT
	@ApiOperation(value="update Customer Order", notes="<pre><code>{"
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
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
		return super.update(access, id, inputMap);
	}


	@DELETE
	@Path("/{id}")
	@ApiOperation("delete Customer Order Detail by id")
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		return super.delete(access, id);
	}

}
