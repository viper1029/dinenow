package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.AddOnDao;
import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ItemInfoDao;
import com.dinenowinc.dinenow.dao.MenuDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.PaymentTypeDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.dao.ReviewDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.dao.SubMenuDao;
import com.dinenowinc.dinenow.dao.TaxDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AddOn;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.ClosedDay;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.Hour;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.ItemInfo;
import com.dinenowinc.dinenow.model.ItemSizeInfo;
import com.dinenowinc.dinenow.model.LatLng;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.ModelHelpers;
import com.dinenowinc.dinenow.model.Modifier;
import com.dinenowinc.dinenow.model.ModifierAddOn;
import com.dinenowinc.dinenow.model.ModifierInfo;
import com.dinenowinc.dinenow.model.NetworkStatus;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.OrderStatus;
import com.dinenowinc.dinenow.model.OrderType;
import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.SearchOrderBy;
import com.dinenowinc.dinenow.model.SearchType;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.SizeInfo;
import com.dinenowinc.dinenow.model.SubMenu;
import com.dinenowinc.dinenow.model.Tax;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.model.WeekDayType;
import com.dinenowinc.dinenow.utils.Utils;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/restaurants")
@Api("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource extends AbstractResource<Restaurant> {

	@Inject
	private RestaurantDao restaurantDao;
	
	@Inject
    ReviewDao  reviewDao;
	
	@Inject
	private ItemDao itemDao;
	@Inject
	private SizeDao sizeDao;
	@Inject
    PaymentTypeDao  paymentTypeDao;
	@Inject private AddOnDao addonDao;
	@Inject private CategoryDao categoryDao;
	@Inject private SubMenuDao submenuDao;
	@Inject private MenuDao menuDao;
	@Inject private OrderDao orderDao;
	@Inject private CustomerDao customerDao;
	@Inject private OrderDao customerOrderDao;
	@Inject private ModifierDao modifierDao;
	@Inject private ItemInfoDao itemInfoDao;
	@Inject private TaxDao taxeDao;
	
	@Inject
	RestaurantUserDao restaurantUserDao;
	
	private AbstractResource<Size> size1;
	
	@POST
	@Path("/{restaurant_id}/checkout")
	@ApiOperation(value="api CheckOut for Customer", notes="<pre><code>{"
			+ "<br/>  \"orderType\": \"PICKUP\","
			+ "<br/>  \"tip\": 1.56,"
			+ "<br/>  \"location\": {"
			+ "<br/>    \"lat\": 10.23232,"
			+ "<br/>    \"lng\": 106.45345"
			+ "<br/>  },"
			+ "<br/>  \"deliveryAddress\": \"51 hoang viet\","
			+ "<br/>  \"paymentMethod\": {"
			+ "<br/>    \"name\": \"Visa\","
			+ "<br/>    \"tokenStripe\": \"tok_sdsdsdsdasgdfdf\""
			+ "<br/>  },"
			+ "<br/>  \"coupons\": \"0\","
			+ "<br/>  \"items\": ["
			+ "<br/>    {"
			+ "<br/>      \"sizes\": {"
			+ "<br/>        \"sizeDescription\": \"Small\","
			+ "<br/>        \"sizeName\": \"Small\","
			+ "<br/>        \"id\": \"c523e6cb-2221-4a25-b735-7dfb1276e50d\""
			+ "<br/>      },"
			+ "<br/>      \"addOns\": [],"
			+ "<br/>      \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>      \"info\": \"543c42a1-a83e-467f-a96c-fe4b29f4f69f\","
			+ "<br/>      \"itemDescription\": \"Item Utils\","
			+ "<br/>      \"itemName\": \"Item Utils\","
			+ "<br/>      \"linkImage\": \"empty\","
			+ "<br/>      \"notes\": \"Item Utils\","
			+ "<br/>      \"spiceLevel\": 2,"
			+ "<br/>      \"price\": 0,"
			+ "<br/>      \"isVegeterian\": false,"
			+ "<br/>      \"quantity\": 1,"
			+ "<br/>      \"id\": \"d37ae2d9-0d8d-4335-9809-c737bedd130c\""
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"sizes\": {"
			+ "<br/>        \"sizeDescription\": \"Medium\","
			+ "<br/>        \"sizeName\": \"Medium\","
			+ "<br/>        \"id\": \"4cafb028-2926-46c8-8c85-d8e4e42f0181\""
			+ "<br/>      },"
			+ "<br/>      \"addOns\": ["
			+ "<br/>        {"
			+ "<br/>          \"addOnDescription\": \"Add on\","
			+ "<br/>          \"addOnName\": \"Add on\","
			+ "<br/>          \"isChecked\": true,"
			+ "<br/>          \"price\": 10,"
			+ "<br/>          \"id\": \"b1f2311d-3c14-499d-bde0-59da358cb558\""
			+ "<br/>        }"
			+ "<br/>      ],"
			+ "<br/>      \"availabilityStatus\": \"AVAILABLE\","
			+ "<br/>      \"info\": \"543c42a1-a83e-467f-a96c-fe4b29f4f69f\","
			+ "<br/>      \"itemDescription\": \"Item Utils\","
			+ "<br/>      \"itemName\": \"Item Utils\","
			+ "<br/>      \"linkImage\": \"empty\","
			+ "<br/>      \"notes\": \"Item Utils\","
			+ "<br/>      \"spiceLevel\": 2,"
			+ "<br/>      \"price\": 0,"
			+ "<br/>      \"isVegeterian\": false,"
			+ "<br/>      \"quantity\": 1,"
			+ "<br/>      \"id\": \"d37ae2d9-0d8d-4335-9809-c737bedd130c\""
			+ "<br/>    }"
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "only for customer") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	public Response checkOut(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
		try {
			if (access.getRole() == UserRole.CUSTOMER) {
				Restaurant restaurant = restaurantDao.findOne(restaurant_id);
				if (restaurant != null) {

					Order co = new Order();
					co.setAvailstatus(AvailabilityStatus.AVAILABLE);
				//	co.setOrderStatus(OrderStatus.OPEN);
					co.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
					//co.setOrderNumber(new Date().getTime());
					co.setTip(Double.parseDouble(dto.get("tip").toString()));
					co.setReceivedAt(new Date());
					
					if (dto.containsKey("location")) {
						HashMap<String, Double> location = (HashMap<String, Double>)dto.get("location");
						co.setLocation(new LatLng(location.get("lat"), location.get("lng")));
					}
					/*if (dto.containsKey("deliveryAddress")) {
						co.setDeliveryAddress(dto.get("deliveryAddress").toString());
					}
					if (dto.containsKey("paymentMethod")) {
						HashMap<String, String> paymentMethod = (HashMap<String, String>)dto.get("paymentMethod");
						co.setPaymentMethod(new PaymentMethod(paymentMethod.get("name"), paymentMethod.get("cardStripe"), paymentMethod.get("last4")));
						
					}*/
					/*if (dto.containsKey("coupons")) {
						co.setCoupons(dto.get("coupons").toString());
					}*/
					if (dto.containsKey("items")) {
						ArrayList<HashMap<String, Object>> items = (ArrayList<HashMap<String, Object>>)dto.get("items");
						for (HashMap<String, Object> hashMap : items) {
							OrderDetail od = new OrderDetail();
							od.setQuantity(Integer.parseInt(hashMap.get("quantity").toString()));
							
							Item item = itemDao.findOne(hashMap.get("id").toString());
							ItemInfo itemInfo = itemInfoDao.findOne(hashMap.get("info").toString());
							
							
							HashMap<String, Object> sizes = (HashMap<String, Object>)hashMap.get("sizes");
							
							/////Must Price is Sizes.........
							
							
							
							od.setPrice(itemInfo.getPrice());
							od.setNote(hashMap.get("spacialNotes").toString());
							
							item.addOrderDetails(od);
							co.addOrderDetail(od);
						}
					}
					Customer cus = customerDao.findOne(access.getId().toString());
					cus.addCustomerOrder(co);
					
					
					
				//	double defaultTax = restaurant.getTax() == null ? 0.0 : restaurant.getTax().getTaxeValue();
					
				//	double amount = calculatorAmount((ArrayList<HashMap<String, Object>>)dto.get("items"), Double.parseDouble(dto.get("tip").toString()), defaultTax, 0);
					
					if (dto.containsKey("paymentMethod")) {
						HashMap<String, String> paymentMethod = (HashMap<String, String>)dto.get("paymentMethod");
						if (paymentMethod.get("name").equals("Cash")) {

							
							
							
						}else {
				//			DineNowApplication.stripe.charge(amount, "usd", co.getPaymentMethod().getCardStripe(), cus.getCustomerStripe(), String.format("Charge for %s", cus.getEmail() != null ? cus.getEmail() : cus.getFirstName() +" "+ cus.getLastName()));
						}
					}
					
					
					orderDao.save(co);
					customerDao.update(cus);
					return ResourceUtils.asSuccessResponse(Status.OK, co);

				}else {
					return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
				}
			}else {
				return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
			}
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	
	@Path("/{restaurant_id}/search_orders")
	@ApiOperation(value="Search Order of Restaurant")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 400, message = "page not format number") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	@GET
	public Response searchByOrder(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id,
			@QueryParam("query") String query, @QueryParam("customer_phone") String c_ph , @QueryParam("order_no") long o_no ){
		if (restaurantDao.findOne(restaurant_id) != null) {
			if (query != null) {
				List<Order> entities = new ArrayList<Order>();
				entities = customerOrderDao.searchByOrder(restaurant_id, query);
				List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
				for (Order dto : entities) {
					dtos.add(onGet(dto));
				}
				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
			} 	else if (c_ph != null) {
				List<Order> entities = new ArrayList<Order>();
				entities = customerOrderDao.searchByOrderAndCustomerPhone(restaurant_id, c_ph);
				List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
				for (Order dto : entities) {
					dtos.add(onGet(dto));
				}
				return ResourceUtils.asSuccessResponse(Status.OK, dtos);
			} else if (o_no > 0) {
				List<Order> entities = new ArrayList<Order>();
				entities = customerOrderDao.searchByOrderNo(restaurant_id, o_no);
				List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
				for (Order dto : entities) {
					dtos.add(onGet(dto));
				}
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("orders", dtos);
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
			} else {
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("query is not null"));
			}
		}else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}

	}
	
	
	@Path("/{restaurant_id}/deliver_zones")
	@ApiOperation(value="Get all deliver zones by restaurant", notes="Not Implement")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	@GET
	public Response getDeliverByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<DeliveryZone> entities = deliveryZoneDao.getAllDeliveryZonesByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("deliveryzones", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}

	}
	
	
	@Path("/{restaurant_id}/order_details")
	@ApiOperation(value="Get All Order Details By Restaurant", notes="status=open/accepted/completed/scheduled/late <br/> from: 2015/01/01 <br/> to: 2015/03/19")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 400, message = "page not format number") ,
			@ApiResponse(code = 404, message = "restaurant not found") 
			})
	@GET
	public Response getOrderByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, @QueryParam("status") String status, @QueryParam("from") String from,@QueryParam("to") String to, @QueryParam("page") String page, @QueryParam("size") String size)
	{	

		int iPage = 1;
		int iSize = 50;
		
		if (restaurantDao.findOne(restaurant_id) != null) {

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
					SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss"); 
					 Date f = new Date();
					 Date t = new Date();
					 try { 
				          f = ft.parse(from); 
				          t = ft.parse(to); 
				          System.out.println(f); 
				      } catch (ParseException e) { 
				          System.out.println("Unparseable using " + ft); 
				      }
					entities = customerOrderDao.getListByRestaurant(restaurant_id, orderstatus, f, t, iPage, iSize);
				} else {
					entities = customerOrderDao.getListByRestaurant(restaurant_id, orderstatus, iPage, iSize);
				}
			} else {
				// status == null
				if (from != null && to != null) {
					// have from - to
					SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss"); 
					 Date f = new Date();
					 Date t = new Date();
					 try { 
				          f = ft.parse(from); 
				          t = ft.parse(to); 
				          System.out.println(f); 
				      } catch (ParseException e) { 
				          System.out.println("Unparseable using " + ft); 
				      }
					entities = customerOrderDao.getListByRestaurant(restaurant_id, f, t, iPage, iSize);
				} else {
					entities = customerOrderDao.getListByRestaurant(restaurant_id, iPage, iSize);
					
				}
			}
			List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
			for (Order dto : entities) {
				dtos.add(onGet(dto));
			}
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("orders", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	private HashMap<String, Object> onGet(Order entity) {
		HashMap<String, Object> uti = entity.toDto();
		

		List<HashMap<String, Object>> orderdetails = new ArrayList<HashMap<String,Object>>();
/*		for (OrderDetail orderDetail : entity.getOrderDetails()) {
			HashMap<String, Object> order = new HashMap<String, Object>();
			List<Item> listItem = itemDao.getListItemByOrderDetails(orderDetail.getId());
			
			order.put("id", orderDetail.getId());
			//order.put("unitPrice", orderDetail.getUnitPrice());
			order.put("price", orderDetail.getPrice());
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
			//	itemtemp.put("keywords",item.getKeywords());
			//	itemtemp.put("price",item.getPrice());
				itemtemp.put("linkImage",item.getLinkImage());
				itemtemp.put("isVegeterian",item.isVegeterian());
				
				Set<HashMap<String, Object>> sizeDtos = new HashSet<HashMap<String, Object>>();
				for (SizeInfo sizeInfo : item.getSizes()) {
					Size size = sizeInfo.getSize();
					HashMap<String, Object> sizeDto = new HashMap<String, Object>();
					sizeDto.put("id", size.getId());
					sizeDto.put("sizeName", size.getSizeName());
					sizeDto.put("sizeDescription", size.getSizeDescription());
					sizeDto.put("price", sizeInfo.getPrice());
					sizeDtos.add(sizeDto);
				}
				itemtemp.put("sizes", sizeDtos);
				
				
				
				items.add(itemtemp);
			}
			order.put("items", items);
			orderdetails.add(order);
		}
		
		uti.put("orderDetails", orderdetails);
		*/
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
	
	
	private HashMap<String, Object> onGet(RestaurantUser entity) {
		HashMap<String, Object> uti = entity.toDto();
/*		uti.put("email", orderdetails);
		uti.put("first_name", customer);
		uti.put("last_name", customer);
		uti.put("phone_number", customer);
		uti.put("role", customer);*/
		return uti;
	}
	
	@Path("/{restaurant_id}/users")
    @GET
	@ApiOperation(value="Get User List By Restaurant-Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	public Response getUsersByRestaurantID(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
		if (restaurantDao.findOne(restaurant_id) == null) {
				return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
		else if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
			List<RestaurantUser> entities  = restaurantUserDao.getListByRestaurant(restaurant_id);
			List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
			for (RestaurantUser dto : entities) {
				dtos.add(onGet(dto));
			}
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("restaurantusers", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));	
	}
	
	@Path("/{restaurant_id}/items")
	@ApiOperation("Get All Items By Restaurant")
	@GET
	public Response getItemsByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Item> entities = itemDao.getListByRestaurant(restaurant_id);		
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			for (int i = 0; i < entities.size(); i++) {
				List<HashMap<String, Object>> sizes = new ArrayList<HashMap<String, Object>>();
				for (SizeInfo size : entities.get(i).getSizes()) {
					LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
					temp.put("id", size.getSize().getId());
					temp.put("name", size.getSize().getSizeName());
					temp.put("description", size.getSize().getSizeDescription());
					temp.put("price", size.getPrice());
					sizes.add(temp);
				}
				dtos.get(i).put("sizePrices", sizes);
				List<HashMap<String, Object>> modifiers = new ArrayList<HashMap<String, Object>>();
				for (ModifierInfo info : entities.get(i).getModifiers()) {
					LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
					temp.put("id", info.getModifier().getId());
					temp.put("name", info.getModifier().getModifierName());
					temp.put("description", info.getModifier().getModifierDescription());
					modifiers.add(temp);
				}
				dtos.get(i).put("modifiers", modifiers);
			}
			dto.put("items", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		}else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}
	}	


	
	@Path("/{restaurant_id}/sizes")
	@ApiOperation("Get All Sizes By Restaurant")
	@GET
	public Response getSizesByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
 {
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Size> entities = sizeDao.getSizesByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);

			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("sizes", dtos);
			
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	
	
	@Path("/{restaurant_id}/addons")
	@ApiOperation("Get All AddOns By Restaurant")
	@GET
	public Response getAddOnsByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<AddOn> entities = addonDao.getAddonsByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("addons", dtos);
			
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	
	@Path("/{restaurant_id}/categories")
	@ApiOperation("Get All Categories By Restaurant")
	@GET
	public Response getCategoriesByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Category> entities = categoryDao.getCategoriesByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			for (int i = 0; i < entities.size(); i++) {
				List<HashMap<String, Object>> submenus = new ArrayList<HashMap<String, Object>>();
				dtos.get(i).put("items", submenus);
			}
			dto.put("categories", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/taxes")
	@ApiOperation("Get All Taxes By Restaurant")
	@GET
	public Response getTaxesByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Tax> entities = taxeDao.getTaxesByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("taxes", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/aboutus")
	@GET
	public Response getAboutUsByRestaurantId(@PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			/*List<Restaurant> entities = null ;
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);*/
			return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("this is Utilsing for Aboutus"));
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/submenus")
	@ApiOperation("Get All SubMenus By Restaurant")
	@GET
	public Response getSubMenusByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<SubMenu> entities = submenuDao.getListByRestaurant(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
/*			for (int i = 0; i < entities.size(); i++) {
				List<HashMap<String, Object>> categories = new ArrayList<HashMap<String, Object>>();
				for (CategoryInfo category : entities.get(i).getCategories()) {
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
				dtos.get(i).put("categories", categories);
			}*/
			dto.put("submenus", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/menus")
	@ApiOperation("Get All Menus By Restaurant")
	@GET
	public Response getMenusByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Menu> entities = menuDao.getMenusByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			for (int i = 0; i < entities.size(); i++) {
				List<HashMap<String, Object>> submenus = new ArrayList<HashMap<String, Object>>();
	/*			for (SubMenu submenu : entities.get(i).getSubMenus()) {
					LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
					temp.put("id", submenu.getId());
					temp.put("name", submenu.getMenuSubName());
					temp.put("description", submenu.getSubMenuDescription());
					temp.put("notes", submenu.getSubMenuNotes());
					submenus.add(temp);
				}*/
				dtos.get(i).put("submenus", submenus);
			}
			dto.put("menus", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/comments")
	@ApiOperation("Get All Reviews By Restaurant")
	@GET
	public Response getReviewsByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id)
	{	
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Review> entities = reviewDao.getReviewsByRestaurantId(restaurant_id);
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("reviews", dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/modifiers")
	@ApiOperation("Get All Modifier By Restaurant")
	@GET
	public Response getModifiersByRestaurantId(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id){
		if (restaurantDao.findOne(restaurant_id) != null) {
			List<Modifier> entities = modifierDao.getModifiersByRestaurantId(restaurant_id);
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+entities.size());
			List<HashMap<String, Object>> dtos = ModelHelpers.fromEntities(entities);
			
			for (int i = 0; i < entities.size(); i++) {
				List<HashMap<String, Object>> addons = new ArrayList<HashMap<String, Object>>();
				for (ModifierAddOn addOnInfo : entities.get(i).getAddOns()) {
					HashMap<String, Object> temp = new HashMap<String, Object>();
					temp.put("addOn", addOnInfo.getAddOn().getId());
					temp.put("availabilityStatus", addOnInfo.getAvailStatus());
					temp.put("price", addOnInfo.getPrice());
//					temp.put("isDefault", addOnInfo.isDefault());
					addons.add(temp);
				}
				dtos.get(i).put("addOns", addons);
				
				List<HashMap<String, Object>> itemSizes = new ArrayList<HashMap<String, Object>>();
				for (ItemSizeInfo itemSizeInfo : entities.get(i).getItems()) {
					HashMap<String, Object> temp = new HashMap<String, Object>();
					temp.put("item", itemSizeInfo.getItem().getId());
					temp.put("size", itemSizeInfo.getSize().getId());
					itemSizes.add(temp);
				}
				dtos.get(i).put("itemSizes", itemSizes);
			}
			
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("modifiers", dtos);
			
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		} else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND,new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	
	
	@Path("/{restaurant_id}/holiday")
	@ApiOperation(value = "Set time  Dine-In Hours, Accept Delivery, Accept TakeOut")
	@PUT
	public Response setRestaurantHoliday(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
		Restaurant restaurant = restaurantDao.findOne(restaurant_id);
		if (restaurant != null) {
			
	     System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
			if (dto.containsKey("closedDays")) {
				List<HashMap<String, Object>> closedDays = (List<HashMap<String, Object>>) dto.get("closedDays");
				Set<ClosedDay> arrHour = restaurant.getClosedDay();
				arrHour.clear();
				for (HashMap<String, Object> day : closedDays) {
					ClosedDay cday = new ClosedDay();
					if (day.containsKey("description")) {
						cday.setDescription(day.get("description").toString());
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					if (day.containsKey("date")) {
						try {
							Date date = df.parse(day.get("date").toString());
							cday.setDate(Utils.convertTimeZones(restaurant.getTimezoneId() , "UTC", day.get("date").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					arrHour.add(cday);
				}
				restaurant.setClosedDay(arrHour);	
			}
			dao.update(restaurant);
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(restaurant));
		}else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}
	}
	
	@Path("/{restaurant_id}/time")
	@ApiOperation(value = "Set time  Dine-In Hours, Accept Delivery, Accept TakeOut", notes="<pre><code>{"
			+ "<br/>  \"dineInHours\": ["
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"acceptDeliveryHours\": ["
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"acceptTakeOutHours\": ["
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    },"
			+ "<br/>    {"
			+ "<br/>      \"weekDayType\": \"MON\","
			+ "<br/>      \"fromTime\": \"2015-03-19T13:26:55Z\","
			+ "<br/>      \"toTime\": \"2015-03-19T13:26:55Z\""
			+ "<br/>    }"
			+ "<br/>  ],"
			+ "<br/>  \"acceptDelivery\": true,"
			+ "<br/>  \"acceptTakeOut\": true,"
			+ "<br/>  \"timeZoneId\": \"Asia/Kolkata\""
			+ "<br/>}</code></pre>")
	@PUT
	public Response setTimeRestaurant(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
		Restaurant restaurant = restaurantDao.findOne(restaurant_id);
		if (restaurant != null) {
			
				   String timezone =  dto.get("timeZoneId").toString();
				   System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
				   restaurant.setTimezoneId(timezone);
			if (dto.containsKey("dineInHours")) {
				List<HashMap<String, Object>> dineInHours = (List<HashMap<String, Object>>) dto.get("dineInHours");
				ArrayList<Hour> arrHour = new ArrayList<Hour>();
				for (HashMap<String, Object> hashMapDineIn : dineInHours) {
					Hour hour = new Hour();
					if (hashMapDineIn.containsKey("weekDayType")) {
						hour.setWeekDayType(WeekDayType.valueOf(hashMapDineIn.get("weekDayType").toString()));
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					//df.setTimeZone(TimeZone.getTimeZone("UTC"));
					if (hashMapDineIn.containsKey("fromTime")) {
						try {
							Date date = df.parse(hashMapDineIn.get("fromTime").toString());
							hour.setFromTime(Utils.convertTimeZones(timezone, "UTC", hashMapDineIn.get("fromTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					if (hashMapDineIn.containsKey("toTime")) {
						try {
							Date date = df.parse(hashMapDineIn.get("toTime").toString());
							hour.setToTime(Utils.convertTimeZones(timezone, "UTC", hashMapDineIn.get("toTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					arrHour.add(hour);
				}
				restaurant.setDineInHours(arrHour);	
			}
			if (dto.containsKey("acceptDeliveryHours")) {
				List<HashMap<String, Object>> acceptDeliveryHours = (List<HashMap<String, Object>>) dto.get("acceptDeliveryHours");
				ArrayList<Hour> arrHour = new ArrayList<Hour>();
				for (HashMap<String, Object> hashMapacceptDelivery : acceptDeliveryHours) {
					Hour hour = new Hour();
					if (hashMapacceptDelivery.containsKey("weekDayType")) {
						hour.setWeekDayType(WeekDayType.valueOf(hashMapacceptDelivery.get("weekDayType").toString()));
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					if (hashMapacceptDelivery.containsKey("fromTime")) {
						try {
							Date date = df.parse(hashMapacceptDelivery.get("fromTime").toString());
							hour.setFromTime(Utils.convertTimeZones(timezone, "UTC", hashMapacceptDelivery.get("fromTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					if (hashMapacceptDelivery.containsKey("toTime")) {
						try {
							Date date = df.parse(hashMapacceptDelivery.get("toTime").toString());
							hour.setToTime(Utils.convertTimeZones(timezone, "UTC", hashMapacceptDelivery.get("toTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					arrHour.add(hour);
				}
				restaurant.setAcceptDeliveryHours(arrHour);
			}
			if (dto.containsKey("acceptTakeOutHours")) {
				List<HashMap<String, Object>> acceptTakeOutHours = (List<HashMap<String, Object>>) dto.get("acceptTakeOutHours");
				ArrayList<Hour> arrHour = new ArrayList<Hour>();
				for (HashMap<String, Object> hashMapacceptTakeOut : acceptTakeOutHours) {
					Hour hour = new Hour();
					if (hashMapacceptTakeOut.containsKey("weekDayType")) {
						hour.setWeekDayType(WeekDayType.valueOf(hashMapacceptTakeOut.get("weekDayType").toString()));
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					//df.setTimeZone(TimeZone.getTimeZone("UTC"));
					if (hashMapacceptTakeOut.containsKey("fromTime")) {
						try {
							Date date = df.parse(hashMapacceptTakeOut.get("fromTime").toString());
							hour.setFromTime(Utils.convertTimeZones(timezone, Utils.UTC, hashMapacceptTakeOut.get("fromTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					if (hashMapacceptTakeOut.containsKey("toTime")) {
						try {
							Date date = df.parse(hashMapacceptTakeOut.get("toTime").toString());
							hour.setToTime(Utils.convertTimeZones(timezone, Utils.UTC, hashMapacceptTakeOut.get("toTime").toString()));
						} catch (ParseException e) {
							e.printStackTrace();
							return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
						}
					}
					arrHour.add(hour);
				}
				restaurant.setAcceptTakeOutHours(arrHour);
			}
			if (dto.containsKey("acceptDelivery")) {
				boolean acceptDelivery = Boolean.parseBoolean(dto.get("acceptDelivery").toString());
				restaurant.setAccept_delivery(acceptDelivery);
			}
			if (dto.containsKey("acceptTakeOut")) {
				boolean acceptTakeOut = Boolean.parseBoolean(dto.get("acceptTakeOut").toString());
				restaurant.setAccept_takeout(acceptTakeOut);
			}
			dao.update(restaurant);
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(restaurant));
		}else {
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
		}
	}

	@Override
	protected HashMap<String, Object> fromEntity(Restaurant entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		return dto;
	}

/*	@Override
	protected List<HashMap<String, Object>> fromEntities(List<Restaurant> entities)	{
		List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();
//		
		for (Restaurant entity : entities) {
			dtos.add(fromEntity(entity));			
		}
//		
		return dtos;
		
		//return ModelHelpers.fromEntities(entities);
	}*/
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected Restaurant fromAddDto(HashMap<String, Object> dto) {
		Restaurant entity = super.fromAddDto(dto);
		entity.setName(dto.get("name").toString());
		entity.setDescription(dto.get("description").toString());
		entity.setAccept_delivery(dto.containsKey("acceptDelivery") ? Boolean.parseBoolean(dto.get(
				"acceptDelivery").toString()) : false);
		entity.setAccept_dinein(dto.containsKey("acceptDineIn") ? Boolean.parseBoolean(dto.get(
				"acceptDineIn").toString()) : false);
		entity.setAccept_takeout(dto.containsKey("acceptTakeOut") ? Boolean.parseBoolean(dto.get(
				"acceptTakeOut").toString()) : false);
		entity.setAddress_1(dto.get("address1").toString());
		entity.setAddress_2(dto.get("address2").toString());
		entity.setCity(dto.get("city").toString());
		entity.setKeyword(dto.get("keyword").toString());
		entity.setProvince(dto.get("province").toString());
		entity.setCountry(dto.get("country").toString());
		entity.setPostal_code(dto.get("postalCode").toString());
		entity.setPhone_number(dto.containsKey("phoneNumber") ? dto.get("phoneNumber").toString() : null);
		entity.setWebsite(dto.get("webSite").toString());
		entity.setContactPerson(dto.get("contactPerson").toString());
	//	entity.setActive(entity.parseBoolean(Boolean.parseBoolean(dto.get("active").toString()));.parseInt(dto.get("active").toString()));
	//	entity.setActive(Boolean.parseBoolean(dto.get("active").toString()));
		entity.setNetworkStatus(dto.containsKey("networkStatus") ? NetworkStatus.valueOf(dto.get("networkStatus").toString()) : NetworkStatus.OFFLINE);
		if (dto.containsKey("location")) {
			HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("location");
			double lat = loca.get("lat");
			double lng = loca.get("lng");
			 GeometryFactory gf = new GeometryFactory();
			 Point point = gf.createPoint(new Coordinate(lat,lng));
			 entity.setLocation(point);
		}
		entity.setTimezoneId(dto.containsKey("timeZoneId")?dto.get("timeZoneId").toString():"UTC");
		if (dto.containsKey("stripe")) {
			entity.setStripe(dto.get("stripe").toString());
		}
		if (dto.containsKey("cuisine")) {
			entity.setCuisine(dto.get("cuisine").toString());
		}
		if (dto.containsKey("logo")) {
			entity.setLogo(dto.get("logo").toString());
		}
		
		if (dto.containsKey("deliveryFee")) {
			entity.setLogo(dto.get("deliveryFee").toString());
		}
		
		if (dto.containsKey("minimumOrder")) {
			entity.setLogo(dto.get("minimumOrder").toString());
		}
		
/*		if (dto.containsKey("discount_allowed")) {
			entity.setDiscount(Boolean.parseBoolean(dto.get("discount_allowed").toString()));
		}*/

/*		if (dto.containsKey("acceptDeliveryHours")) {
			entity.setAccept_delivery_hours(dto.get("acceptDeliveryHours").toString());
		}
		if (dto.containsKey("acceptTakeOutHours")) {
			entity.setAccept_takeout_hours(dto.get("acceptTakeOutHours").toString());
		}*/
		if (dto.containsKey("paymentTypes")) {
			ArrayList<String> listKeyPaymentTypes = (ArrayList<String>)dto.get("paymentTypes");
			for (String key : listKeyPaymentTypes) {
				PaymentType paymentType = paymentTypeDao.findOne(key);
				entity.addPaymentTypes(paymentType);
				
			}
		}
		return entity;
	}
	
	
	
	@Override
	protected Restaurant fromUpdateDto(Restaurant t, HashMap<String, Object> dto) {
		Restaurant entity = super.fromUpdateDto(t, dto);
		entity.setName(dto.containsKey("name")?dto.get("name").toString():entity.getName());
		entity.setDescription(dto.containsKey("description")?dto.get("description").toString():entity.getDescription());
		entity.setAccept_delivery(dto.containsKey("acceptDelivery") ? Boolean.parseBoolean(dto.get(
				"acceptDelivery").toString()) : false);
		entity.setAccept_dinein(dto.containsKey("acceptDineIn") ? Boolean.parseBoolean(dto.get(
				"acceptDineIn").toString()) : false);
		entity.setAccept_takeout(dto.containsKey("acceptTakeOut") ? Boolean.parseBoolean(dto.get(
				"acceptTakeOut").toString()) : false);
		entity.setAddress_1(dto.get("address1").toString());
		entity.setAddress_2(dto.get("address2").toString());
		entity.setCity(dto.get("city").toString());
		entity.setKeyword(dto.containsKey("keyword")?dto.get("keyword").toString():entity.getKeyword());
		entity.setProvince(dto.get("province").toString());
		entity.setCountry(dto.get("country").toString());
		entity.setPostal_code(dto.get("postalCode").toString());
		entity.setPhone_number(dto.containsKey("phoneNumber") ? dto.get("phoneNumber").toString() : null);
		entity.setWebsite(dto.get("webSite").toString());
		entity.setContactPerson(dto.get("contactPerson").toString());
		entity.setActive(dto.containsKey("active")?Boolean.parseBoolean(dto.get("active").toString()):entity.isActive());
		entity.setNetworkStatus(dto.containsKey("networkStatus") ? NetworkStatus.valueOf(dto.get("networkStatus").toString()) :entity.getNetworkStatus());
		if (dto.containsKey("location")) {
			HashMap<String, Double> loca = (HashMap<String, Double>)dto.get("location");
			double lat = loca.get("lat");
			double lng = loca.get("lng");
			 GeometryFactory gf = new GeometryFactory();
			 Point point = gf.createPoint(new Coordinate(lat,lng));
			 entity.setLocation(point);
		}
		if (dto.containsKey("logo")) {
			entity.setLogo(dto.get("logo").toString());
		}
		
		if (dto.containsKey("deliveryFee")) {
			entity.setLogo(dto.get("deliveryFee").toString());
		}
		
		if (dto.containsKey("minimumOrder")) {
			entity.setLogo(dto.get("minimumOrder").toString());
		}
		
/*		if (dto.containsKey("discount_allowed")) {
			entity.setDiscount(Boolean.parseBoolean(dto.get("discount_allowed").toString()));
		}*/

		if (dto.containsKey("cuisine")) {
			entity.setCuisine(dto.get("cuisine").toString());
		}
/*		if (dto.containsKey("acceptDeliveryHours")) {
			entity.setAccept_delivery_hours(dto.get("acceptDeliveryHours").toString());
		}
		if (dto.containsKey("acceptTakeOutHours")) {
			entity.setAccept_takeout_hours(dto.get("acceptTakeOutHours").toString());
		}*/

		return entity;
	}
	
	
	
	
	
	
	
	@Override
	protected HashMap<String, Object> onGet(Restaurant entity) {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", entity.getId());
		dto.put("name", entity.getName());
		dto.put("description", entity.getDescription());
		dto.put("acceptDelivery", entity.isAccept_delivery());
		dto.put("acceptDineIn", entity.isAccept_dinein());
		dto.put("acceptTakeOut", entity.isAccept_takeout());
		dto.put("location", entity.getLocation() != null ? new LatLng(entity.getLocation().getX(), entity.getLocation().getY()) : "");
		dto.put("address1", entity.getAddress_1());
		dto.put("address2", entity.getAddress_2());
		dto.put("city", entity.getCity());
		dto.put("keyword", entity.getKeyword());
		dto.put("province", entity.getProvince());
		dto.put("postalCode", entity.getPostal_code());
		dto.put("country", entity.getCountry());
		dto.put("phoneNumber", entity.getPhone_number());
		dto.put("networkStatus", entity.getNetworkStatus());
		dto.put("contactPerson", entity.getContactPerson());
		dto.put("webSite", entity.getWebsite());
		dto.put("rating", entity.getRating());
		dto.put("active", entity.isActive());
//		dto.put("stripe", entity.getStripe());
		dto.put("dineInHours", entity.getDineInHours());
		dto.put("acceptDeliveryHours", entity.getAcceptDeliveryHours());
		dto.put("acceptTakeOutHours", entity.getAcceptTakeOutHours());
	//	dto.put("discount_allowed", entity.isDiscount());

		
		LinkedList<HashMap<String, Object>> holidayDtos = new LinkedList<HashMap<String, Object>>();
		for(ClosedDay day :entity.getClosedDay()){
			HashMap<String, Object> closeday = new LinkedHashMap<String, Object>();
			closeday.put("id", day.getId());
			closeday.put("date", day.getDate());
			closeday.put("description", day.getDescription());
			holidayDtos.add(closeday);
		}
		
		dto.put("closedays", holidayDtos);
		dto.put("paymentTypes", entity.getPaymentTypes());
		dto.put("timezone", entity.getTimezoneId());
		dto.put("logo", entity.getLogo());
System.out.println(entity.getMenus().size()+"++++++++++++++++++++++");
		// menus
		LinkedList<HashMap<String, Object>> menuDtos = new LinkedList<HashMap<String, Object>>();

		for (Menu menu : entity.getMenus()) {
			HashMap<String, Object> menuDto = new LinkedHashMap<String, Object>();
			menuDto.put("id", menu.getId());
			menuDto.put("name", menu.getMenuName());
			menuDto.put("description", menu.getMenuDescription());
			// Submenu
			ArrayList<HashMap<String, Object>> sunmenuDtos = new ArrayList<HashMap<String, Object>>();
/*			for (SubMenu submenu : menu.getSubMenus()) {
				HashMap<String, Object> submenuDto = new LinkedHashMap<String, Object>();
				submenuDto.put("id", submenu.getId());
				submenuDto.put("name", submenu.getMenuSubName());
				submenuDto.put("description",submenu.getSubMenuDescription());

				ArrayList<HashMap<String, Object>> categoryDtos = new ArrayList<HashMap<String, Object>>();
				for (CategoryInfo categoryInfo : submenu.getCategories()) {
					HashMap<String, Object> categoryDto = new LinkedHashMap<String, Object>();

					Category category = categoryInfo.getCategory();

					categoryDto.put("id", category.getId());
					categoryDto.put("name", category.getCategoryName());
					categoryDto.put("description",
							category.getCategoryDescription());

					// items
									ArrayList<HashMap<String, Object>> itemDtos = new ArrayList<HashMap<String, Object>>();
					for (ItemInfo itemInfo : categoryInfo.getItems()) {
						HashMap<String, Object> itemDto = new LinkedHashMap<String, Object>();
						Item item = itemInfo.getItem();

						itemDto.put("id", item.getId());
						itemDto.put("name", item.getItemName());
						itemDto.put("description",
								item.getItemDescription());
						itemDto.put("linkImage", item.getLinkImage());
						itemDto.put("notes", item.getNotes());
						itemDto.put("isVegeterian", item.isVegeterian());
						itemDto.put("spiceLevel", item.getSpiceLevel());
//						itemDto.put("isShowSpice", item.isShowSpice());
						itemDto.put("availabilityStatus",
								item.getAvailabilityStatus());
						itemDto.put(
								"price",
								(itemInfo.getPrice() != 0) ? itemInfo
										.getPrice() : item.getPrice());
						
						ArrayList<HashMap<String, Object>> sizeDtos = new ArrayList<HashMap<String, Object>>();
						for (SizeInfo sizeInfo : item.getSizes()) {
							Size size = sizeInfo.getSize();
							HashMap<String, Object> sizeDto = new LinkedHashMap<String, Object>();
							sizeDto.put("id", size.getId());
							sizeDto.put("name", size.getSizeName());
							sizeDto.put("description", size.getSizeDescription());
							sizeDto.put("price",sizeInfo.getPrice());
							sizeDtos.add(sizeDto);
						}
						itemDto.put("sizes", sizeDtos);
						
						
						
	//					 * MODIFIER INPUT THentityERE
						ArrayList<HashMap<String, Object>> modifierDtos = new ArrayList<HashMap<String, Object>>();
						//System.out.println(item.getModifiers().size()+"------------------");
						for (ModifierInfo modifierInfo : item.getModifiers()) {
							Modifier modifier = modifierInfo.getModifier();
							HashMap<String, Object> modifierDto = new LinkedHashMap<String, Object>();
							modifierDto.put("id", modifier.getId());
							modifierDto.put("name", modifier.getModifierName());
							modifierDto.put("description", modifier.getModifierDescription());
							modifierDto.put("minselection", modifier.getMinSelection());
							modifierDto.put("maxselection", modifier.getMaxSelection());
							modifierDto.put("availabilityStatus", modifierInfo.getAvailabilityStatus());
							
							
							//modifier addons
							ArrayList<HashMap<String, Object>> addonDtos = new ArrayList<HashMap<String, Object>>();
							for(ModifierAddOn modifierAddOn : modifier.getAddOns()){
							     AddOn addon = modifierAddOn.getAddOn();
							     HashMap<String, Object> addonDto = new LinkedHashMap<String, Object>();
							     addonDto.put("id", addon.getId());
							     addonDto.put("name", addon.getAddOnName());
							     addonDto.put("description", addon.getAddOnDescription());
							     addonDto.put("price", modifierAddOn.getPrice());
							     addonDto.put("availabilityStatus", addon.getAvailabilityStatus());
							     addonDtos.add(addonDto);
							}
							
							modifierDto.put("addons", addonDtos);
							modifierDtos.add(modifierDto);
						} 
						itemDto.put("modifiers", modifierDtos);
		             
						itemDtos.add(itemDto);

					}
					categoryDto.put("items", itemDtos);
					categoryDtos.add(categoryDto);
				}
				submenuDto.put("categories", categoryDtos);
				sunmenuDtos.add(submenuDto);

			}*/
			menuDto.put("submenus", sunmenuDtos);
			menuDtos.add(menuDto);
		}
		dto.put("menus", menuDtos);
		HashMap<String, Object> dtos = new HashMap<String, Object>();
		dtos.put(getClassT().getSimpleName().toLowerCase(), dto);
		return dtos;
	}
	
	protected HashMap<String, Object> onGetApp(Restaurant entity) {
		HashMap<String, Object> dto = super.onGet(entity);
		// menus
		ArrayList<HashMap<String, Object>> menuDtos = new ArrayList<HashMap<String, Object>>();
		for (Menu menu : entity.getMenus()) {
			if (menu.isShowMenu()) {

				HashMap<String, Object> menuDto = new HashMap<String, Object>();
				menuDto.put("id", menu.getId());
				menuDto.put("menuName", menu.getMenuName());
				menuDto.put("menuDescription", menu.getMenuDescription());
				// Submenu
				ArrayList<HashMap<String, Object>> sunmenuDtos = new ArrayList<HashMap<String, Object>>();
/*				for (SubMenu submenu : menu.getSubMenus()) {
					HashMap<String, Object> submenuDto = new HashMap<String, Object>();
					submenuDto.put("id", submenu.getId());
					submenuDto.put("menuSubName", submenu.getMenuSubName());
					submenuDto.put("subMenuDescription",submenu.getSubMenuDescription());

					ArrayList<HashMap<String, Object>> categoryDtos = new ArrayList<HashMap<String, Object>>();
					for (CategoryInfo categoryInfo : submenu.getCategories()) {
						HashMap<String, Object> categoryDto = new HashMap<String, Object>();

						Category category = categoryInfo.getCategory();

						categoryDto.put("id", category.getId());
						categoryDto.put("categoryName", category.getCategoryName());
						categoryDto.put("categoryDescription",
								category.getCategoryDescription());

						// items
						Set<HashMap<String, Object>> itemDtos = new HashSet<HashMap<String, Object>>();//new ArrayList<HashMap<String, Object>>();
						for (ItemInfo itemInfo : categoryInfo.getItems()) {
							HashMap<String, Object> itemDto = new HashMap<String, Object>();
							Item item = itemInfo.getItem();

							itemDto.put("info", itemInfo.getId());
							itemDto.put("id", item.getId());
							itemDto.put("itemName", item.getItemName());
							itemDto.put("itemDescription",
									item.getItemDescription());
							itemDto.put("linkImage", item.getLinkImage());
							itemDto.put("notes", item.getNotes());
							itemDto.put("isVegeterian", item.isVegeterian());
							itemDto.put("spiceLevel", item.getSpiceLevel());
							itemDto.put("availabilityStatus",
									item.getAvailabilityStatus());
							itemDto.put(
									"price",
									(itemInfo.getPrice() != 0) ? itemInfo
											.getPrice() : item.getPrice());
							
							Set<HashMap<String, Object>> sizeDtos = new HashSet<HashMap<String, Object>>();
							for (SizeInfo sizeInfo : item.getSizes()) {
								Size size = sizeInfo.getSize();
								HashMap<String, Object> sizeDto = new HashMap<String, Object>();
								sizeDto.put("id", size.getId());
								sizeDto.put("sizeName", size.getSizeName());
								sizeDto.put("sizeDescription", size.getSizeDescription());
								sizeDto.put("price", sizeInfo.getPrice());
								sizeDtos.add(sizeDto);
							}
							itemDto.put("sizes", sizeDtos);
							
							Set<HashMap<String, Object>> addOnDtos = new HashSet<HashMap<String, Object>>();
							for (ModifierInfo modifierInfo : item.getModifiers()) {
								Modifier modifier = modifierInfo.getModifier();
								for (ItemSizeInfo hashMap : modifier.getItems()) {
									if (hashMap.getItem().getId().equals(item.getId())) {
										for (ModifierAddOn hashMap2 : modifier.getAddOns()) {
											AddOn addon = hashMap2.getAddOn();
											HashMap<String, Object> addOnDto = new HashMap<String, Object>();
											addOnDto.put("id", addon.getId());
											addOnDto.put("addOnName", addon.getAddOnName());
											addOnDto.put("addOnDescription", addon.getAddOnDescription());
//											addOnDto.put("isDefault", hashMap2.isDefault());
											for (SizeInfo hashMap3 : addon.getSizes()) {
												if (hashMap.getSize().getId().equals(hashMap3.getSize().getId())) {
													addOnDto.put("price", hashMap3.getPrice());
												}
											}
											addOnDtos.add(addOnDto);
										}
									}
								}
								
							}
							itemDto.put("addOns", addOnDtos);
							
							
							

							itemDtos.add(itemDto);

						}
						categoryDto.put("items", itemDtos);
						categoryDtos.add(categoryDto);
					}
					submenuDto.put("categories", categoryDtos);
					sunmenuDtos.add(submenuDto);

				}*/
				menuDto.put("submenus", sunmenuDtos);
				menuDtos.add(menuDto);
			
			}
		}
		dto.put("menus", menuDtos);

		return dto;
	}
	
	

	// =================================METHOD=================================//

	@GET
	@ApiOperation(value = "api get all restaurant by admin", notes = "must login admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user"),
			@ApiResponse(code = 400, message = "page not format number"),
			@ApiResponse(code = 400, message = "size not format number")
			})
	public Response getAll(@ApiParam(access = "internal") @Auth User access, @QueryParam("page") String page, @QueryParam("size") String size) {
		if (access.getRole() == UserRole.ADMIN) {
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

			List<Restaurant> entities = this.dao.findAll(iPage, iSize);	
			List<HashMap<String, Object>> dtos = fromEntities(entities);
			LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put(getClassT().getSimpleName().toLowerCase()+'s', dtos);
			return ResourceUtils.asSuccessResponse(Status.OK, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("access denied for user"));
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "api get detail restaurant")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		Restaurant entity = restaurantDao.findByKeyword(id); 
		if(entity != null)
			return ResourceUtils.asSuccessResponse(Status.OK,entity);
		else
			return super.get(access, id);
	}
	
	@GET
	@Path("/{id}/app")
	@ApiOperation(value = "api get detail restaurant")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	public Response getApp(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id){
		
		Restaurant entity = dao.findOne(id);
    	if (entity != null) {
    		HashMap<String, Object> dto = onGetApp(entity);  		
    		return ResourceUtils.asSuccessResponse(Status.OK, dto);
    	} else {
    		return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Cannot found entity"));
    	}
	}
	

	@POST
	@ApiOperation(value = "api add new restaurant", notes = "<pre><code>{"
			+ "<br/>  \"acceptTakeOutOrders\": false,"
			+ "<br/>  \"tax\": \"b5eaf69c-0025-44d8-96e1-ada02a4723f3\","
			+ "<br/>  \"phoneNumber\": \"06666\","
			+ "<br/>  \"webSite\": \"http://abc.com\","
			+ "<br/>  \"linkImage\": \"Res1.jpg\","
			+ "<br/>  \"status\": \"ONLINE\","
			+ "<br/>  \"address\": \"59 hoang viet\","
			+ "<br/>  \"restaurantDescription\": \"restaurent 2\","
			+ "<br/>  \"deliveryPaymentType\": \"DEBIT\","
			+ "<br/>  \"acceptDeliveryOrders\": false,"
			+ "<br/>  \"restaurantName\": \"Res 2\","
			+ "<br/>  \"location\": {"
			+ "<br/>    \"lat\": 10.455,"
			+ "<br/>    \"lng\": 106.676"
			+ "<br/>  },"
			+ "<br/>  \"cuisine\": ["
			+ "<br/>    \"abc\","
			+ "<br/>    \"abc\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 500, message = "Cannot add entity. Error message: ###") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		System.out.println("############" + dto);
		if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
			return super.add(access, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("access denied for user"));
	}

	@PUT
	@ApiOperation(value = "api update restaurant", notes = ""
			+ "<code><pre>{<br/>  \"id\": \"34c192b0-e6e9-4668-b714-d88c9b1476b4\","
			+ "<br/>  \"acceptTakeOutOrders\": false," + "<br/>  \"tax\": \"b5eaf69c-0025-44d8-96e1-ada02a4723f3\","
			+ "<br/>  \"phoneNumber\": \"06666\","
			+ "<br/>  \"webSite\": \"http://abc.com\","
			+ "<br/>  \"linkImage\": \"Res1.jpg\","
			+ "<br/>  \"status\": \"ONLINE\","
			+ "<br/>  \"address\": \"59 hoang viet\","
			+ "<br/>  \"restaurantDescription\": \"restaurent 2\","
			+ "<br/>  \"deliveryPaymentType\": \"DEBIT\","
			+ "<br/>  \"acceptDeliveryOrders\": false,"
			+ "<br/>  \"restaurantName\": \"Res 2\"," 
			+ "<br/>  \"location\": {"
			+ "<br/>    \"lat\": 10.455,"
			+ "<br/>    \"lng\": 106.676"
			+ "<br/>  },"
			+ "<br/>  \"cuisine\": ["
			+ "<br/>    \"abc\","
			+ "<br/>    \"abc\""
			+ "<br/>  ]"
			+ "<br/>}</code></pre>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 500, message = "Cannot update entity. Error message: ###") 
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
				return super.update(access, id, dto);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("access denied for user"));
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "api delete restaurant")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 401, message = "access denied for user") ,
			@ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
			@ApiResponse(code = 404, message = "Cannot found entity")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
	}

	
	private List<HashMap<String, Object>> fromEntitiesSearch(List<Restaurant> entities){
		List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();

		
		
		for (Restaurant entity : entities) {
			HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
			dto.put("id", entity.getId());
			dto.put("name", entity.getName());
			dto.put("description", entity.getDescription());
			dto.put("acceptDelivery", entity.isAccept_delivery());
			dto.put("acceptDineIn", entity.isAccept_dinein());
			dto.put("acceptTakeOut", entity.isAccept_takeout());
			dto.put("location", entity.getLocation() != null ? new LatLng(entity.getLocation().getX(), entity.getLocation().getY()) : "");
			dto.put("address1", entity.getAddress_1());
			dto.put("address2", entity.getAddress_2());
			dto.put("city", entity.getCity());
			dto.put("keyword", entity.getKeyword());
			dto.put("province", entity.getProvince());
			dto.put("postalCode", entity.getPostal_code());
			dto.put("country", entity.getCountry());
			dto.put("phoneNumber", entity.getPhone_number());
			dto.put("networkStatus", entity.getNetworkStatus());
			dto.put("contactPerson", entity.getContactPerson());
			dto.put("webSite", entity.getWebsite());
			dto.put("rating", entity.getRating());
			dto.put("active", entity.isActive());
			dto.put("logo", entity.getLogo());
	//		dto.put("stripe", entity.getStripe());
			dto.put("dineInHours", entity.getDineInHours());
	//		dto.put("acceptDeliveryHours", entity.getAccept_delivery_hours());
	//		dto.put("acceptTakeOutHours", entity.getAccept_takeout_hours());
			dto.put("paymentTypes", entity.getPaymentTypes());
			dtos.add(dto);
		}
		return dtos;
	}
	
	
	
	@GET
	@Path("/searchByLocation/")
	@ApiOperation(value = "api search by location", notes = "http://localhost:30505/api/restaurants/searchByLocation?zone=&location=&type=0/2/4&distance=&cusine=&sorted=ASC/DESC/DIST/RAT<br/><br/><br/>location 10.795769,106.663412<br/><br/><br/><br/>type 0 : delivery<br/>2 : pickup<br/>4 : both (or delivery or pickup)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 400, message = "location is not null"),
			@ApiResponse(code = 400, message = "Format Incorrect ###")
			})
	public Response get(@ApiParam(access = "internal") @Auth User access,
			@ApiParam(required=true) @QueryParam("location") String latlng,
			@QueryParam("type") Integer type,@QueryParam("zone") String zone,
			@QueryParam("distance") double distance,@QueryParam("cusine") String cusine , @QueryParam("sorted") String sorted) {
		
		if (zone == null || zone.length()==0) {
		    throw new WebApplicationException(
		      ResourceUtils.asSuccessResponse(Status.BAD_REQUEST, "zone parameter is mandatory")
		    );
		  }
		if(!Utils.isValidTimeZone(zone)){
			 throw new WebApplicationException(
				      ResourceUtils.asSuccessResponse(Status.BAD_REQUEST, "zone not exist")
				    );
		}
		try {
			distance = distance / 112;
			if (distance <= 0) {
				distance = .25;
			}
			SearchType searchType  = null;
			searchType = type==null?null:SearchType.fromInteger(type);
			if (latlng != null) {
				LatLng location = new LatLng(Double.parseDouble(latlng
						.split(",")[0]),
						Double.parseDouble(latlng.split(",")[1]));
				List<Restaurant> entities = restaurantDao.findDistanceNew(
						searchType, new GeometryFactory()
								.createPoint(new Coordinate(location.getLat(),
										location.getLng())), distance ,cusine, sorted); 
				
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("restaurants", fromEntitiesSearch(entities));
				return ResourceUtils.asSuccessResponse(Status.OK,dto);//fromEntities(entities));
			} else {
				 
				
				List<Restaurant> entities = restaurantDao.findDistanceNew(searchType , cusine ,sorted , zone);
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("restaurants", fromEntitiesSearch(entities));
				return ResourceUtils.asSuccessResponse(Status.OK, dto);
				
				//return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
				//		new ServiceErrorMessage("location is not null"));
			}
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,new ServiceErrorMessage(
					"Format Incorrect " + e));
		}
	}
	
	@GET
	@Path("/searchByDistance/")
	@ApiOperation(value = "api search by distance", notes = "http://localhost:30505/api/restaurants/searchByDistance?location=&distance=<br/><br/><br/>location 10.795769,106.663412<br/>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 400, message = "location is not null"),
			@ApiResponse(code = 400, message = "Format Incorrect ###")
			})
	public Response get(@ApiParam(access = "internal") @Auth User access,
			@ApiParam(required=true) @QueryParam("location") String latlng,
			@QueryParam("distance") double distance) {
		try {
			distance = distance / 1000;
			if (distance <= 0) {
				distance = 25;
			}
			if (latlng != null) {
				LatLng location = new LatLng(Double.parseDouble(latlng
						.split(",")[0]),
						Double.parseDouble(latlng.split(",")[1]));
				List<Restaurant> entities = restaurantDao.findDistance(
						new GeometryFactory().createPoint(new Coordinate(
								location.getLat(), location.getLng())),
						distance, SearchOrderBy.DISTANCE); // 25KM
				LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
				dto.put("restaurants", fromEntities(entities));
				return ResourceUtils.asSuccessResponse(Status.OK,
						dto);
			} else {
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
						new ServiceErrorMessage("location is not null"));
			}
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,new ServiceErrorMessage(
					"Format Incorrect " + e));
		}
	}

	@Inject
	private DeliveryZoneDao deliveryZoneDao;

	@GET
	@Path("/polygon")
	@ApiOperation(value = "api Utils search delivery zone")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	public Response getPolygon(@ApiParam(access = "internal") @Auth User access) {
		List<Restaurant> entities = this.dao.findAll();

		List<HashMap<String, Object>> dtos = new ArrayList<HashMap<String, Object>>();

		for (Restaurant entity : entities) {
			HashMap<String, Object> entitys = fromEntity(entity);
			List<DeliveryZone> dzs = deliveryZoneDao
					.getAllDeliveryZonesByRestaurantId(entity.getId());

			List<HashMap<String, Object>> deliverys = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < dzs.size(); i++) {
				HashMap<String, Object> delivery = new HashMap<String, Object>();

				delivery.put("id", dzs.get(i).getId());
				delivery.put("name", dzs.get(i)
						.getName());
				delivery.put("description", dzs.get(i)
						.getDescription());
				delivery.put("minimum", dzs.get(i).getMinimum());
				delivery.put("fee", dzs.get(i).getFee());
				delivery.put("type", dzs.get(i)
						.getType());
				List<LatLng> coords = new ArrayList<LatLng>();
				for (int j = 0; j < dzs.get(i).getCoordinates().getCoordinates().length; j++) {
					Coordinate coord = dzs.get(i).getCoordinates()
							.getCoordinates()[j];
					LatLng latlng = new LatLng(coord.x, coord.y);
					coords.add(latlng);
				}
				delivery.put("coordinates", coords);
				deliverys.add(delivery);
			}
			entitys.put("deliveryZones", deliverys);

			dtos.add(entitys);
		}
		return ResourceUtils.asSuccessResponse(Status.OK, dtos);

	}
	
	@GET
	@Path("/{restaurant_id}/info")
	@ApiOperation(value = "get detail info restaurant user login")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "restaurant found info")
			})
	public Response getInfo(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id){
		Restaurant res = restaurantDao.findOne(restaurant_id);
		if (res != null) {
			return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(res));
		}
		return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant found info"));
	}
	
	
	
	
	
	/**
	 * {
  "coupons": "id coupons",
  "deliveryAddress": "51 hoang viet",
  "deliveryTime": "Apr 20, 2015 2:07:58 PM",
  "items": [
    {
      "spacialNotes": "Cai gif dos",
      "addOns": [],
      "availabilityStatus": "AVAILABLE",
      "info": "543c42a1-a83e-467f-a96c-fe4b29f4f69f",
      "itemDescription": "Item Utils",
      "itemName": "Item Utils",
      "linkImage": "empty",
      "notes": "Item Utils",
      "sizes": {
        "price": "10.0",
        "sizeDescription": "Medium",
        "sizeName": "Medium",
        "id": "4cafb028-2926-46c8-8c85-d8e4e42f0181"
      },
      "price": 0,
      "quantity": 5,
      "isVegeterian": false,
      "spiceLevel": 2,
      "id": "d37ae2d9-0d8d-4335-9809-c737bedd130c"
    }
  ],
  "orderType": "DELIVERY",
  "tip": 3.6
}
	 * @param itemOrder
	 * @param tip
	 * @param tax
	 * @param discount
	 * @return
	 */
	
	private double calculatorAmount(ArrayList<HashMap<String, Object>> itemOrder, double tip, double tax, double discount){
		double total = 0;
		double subtotal = 0;
		for (HashMap<String, Object> hashMap : itemOrder) {
			HashMap<String, Object> size = (HashMap<String, Object>)hashMap.get("sizes");
			
			double priceSize = Double.parseDouble(size.get("price").toString());
			int quantity = Integer.parseInt(hashMap.get("quantity").toString());
			subtotal += priceSize * quantity;
			List<HashMap<String, Object>> addOns = (List<HashMap<String, Object>>)hashMap.get("addOns");
			double priceAddOn = 0;
			for (HashMap<String, Object> hashMap2 : addOns) {
				priceAddOn += Double.parseDouble(hashMap2.get("price").toString());
			}
			subtotal += priceAddOn;			
		}
		double tipCalculator = (subtotal*tip)/100;
		double taxCalculator = (subtotal*tax)/100;
		double discountCalculator = (subtotal*discount)/100;
		
		total = subtotal + tipCalculator + taxCalculator + discountCalculator;
		return total;
	}	
}

