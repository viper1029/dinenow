package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.PaymentTypeDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.ClosedDay;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.Hour;
import com.dinenowinc.dinenow.model.helpers.LatLng;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import com.dinenowinc.dinenow.model.helpers.OrderType;
import com.dinenowinc.dinenow.model.helpers.SearchOrderBy;
import com.dinenowinc.dinenow.model.helpers.SearchType;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.model.helpers.WeekDayType;
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
import io.dropwizard.auth.Auth;

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

@Path("/restaurants")
@Api("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource extends AbstractResource<Restaurant> {

  @Inject
  private RestaurantDao restaurantDao;

  @Inject
  private ItemDao itemDao;

  @Inject
  PaymentTypeDao paymentTypeDao;

  @Inject
  private OrderDao orderDao;

  @Inject
  private CustomerDao customerDao;

  @Inject
  private OrderDao customerOrderDao;

  @Inject
  RestaurantUserDao restaurantUserDao;

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
      int pageInt;
      int sizeInt;
      try {
        pageInt = parseInteger(page);
        sizeInt = parseInteger(size);
        pageInt = pageInt == -1 ? 1 : pageInt;
        sizeInt = sizeInt == -1 ? 50 : sizeInt;
      }
      catch (Exception e) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Could not parse page or size."));
      }

      List<Restaurant> entities = this.dao.getByPage(pageInt, sizeInt);
      List<HashMap<String, Object>> returnMap = getMapListFromEntities(entities);
      LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
      dto.put("restaurants", returnMap);
      return ResourceUtils.asSuccessResponse(Status.OK, dto);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  private int parseInteger(final String intStr) {
    int pageInt = -1;
    if (intStr != null) {
      pageInt = Integer.parseInt(intStr);
    }
    return pageInt;
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
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      return super.get(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @POST
  @ApiOperation(value = "add new restaurant")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 500, message = "Cannot add entity. Error message: ###")
  })
  @Override
  public Response create(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.OWNER || access.getRole() == UserRole.ADMIN) {
      return super.create(access, inputMap);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

  @Override
  protected Restaurant getEntityForInsertion(HashMap<String, Object> inputMap) {
    Restaurant entity = super.getEntityForInsertion(inputMap);
    entity.setName(inputMap.get("name").toString());
    entity.setDescription(inputMap.get("description").toString());
    entity.setAcceptDelivery(inputMap.containsKey("acceptDelivery") ? Boolean.parseBoolean(inputMap.get("acceptDelivery").toString()) : false);
    entity.setAcceptDineIn(inputMap.containsKey("acceptDineIn") ? Boolean.parseBoolean(inputMap.get("acceptDineIn").toString()) : false);
    entity.setAcceptTakeout(inputMap.containsKey("acceptTakeOut") ? Boolean.parseBoolean(inputMap.get("acceptTakeOut").toString()) : false);
    entity.setAddress1(inputMap.get("address1").toString());
    entity.setAddress2(inputMap.containsKey("address2") ? inputMap.get("address2").toString() : null);
    entity.setCity(inputMap.get("city").toString());
    entity.setKeyword(inputMap.containsKey("keyword") ? inputMap.get("keyword").toString() : null);
    entity.setProvince(inputMap.get("province").toString());
    entity.setCountry(inputMap.get("country").toString());
    entity.setPostalCode(inputMap.get("postalCode").toString());
    entity.setPhoneNumber(inputMap.containsKey("phoneNumber") ? inputMap.get("phoneNumber").toString() : null);
    entity.setWebsite(inputMap.get("webSite").toString());
    entity.setContactPerson(inputMap.containsKey("contactPerson") ? inputMap.get("contactPerson").toString() : null);
    entity.setNetworkStatus(inputMap.containsKey("networkStatus") ? NetworkStatus.valueOf(inputMap.get("networkStatus").toString()) : NetworkStatus.OFFLINE);
//    entity.setContactPerson(inputMap.containsKey("cuisine") ? inputMap.get("cuisine").toString() : null);
//    entity.setContactPerson(inputMap.containsKey("logo") ? inputMap.get("logo").toString() : null);
//    entity.setContactPerson(inputMap.containsKey("stripe") ? inputMap.get("stripe").toString() : null);
    entity.setTimezone(inputMap.containsKey("timeZoneId") ? inputMap.get("timeZoneId").toString() : "UTC");
    HashMap<String, Double> location = (HashMap<String, Double>) inputMap.get("location");
    GeometryFactory gf = new GeometryFactory();
    Point point = gf.createPoint(new Coordinate(location.get("lat"), location.get("lng")));
    entity.setLocation(point);
    if (inputMap.containsKey("paymentTypes")) {
      ArrayList<String> listKeyPaymentTypes = (ArrayList<String>) inputMap.get("paymentTypes");
      for (String key : listKeyPaymentTypes) {
        PaymentType paymentType = paymentTypeDao.get(key);
        entity.addPaymentTypes(paymentType);
      }
    }
    return entity;
  }

  @PUT
  @ApiOperation(value = "api update restaurant")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 500, message = "Cannot update entity. Error message: ###")
  })
  @Path("/{id}")
  @Override
  public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> inputMap) {
    if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      Restaurant restaurant = restaurantDao.get(id);
      if (restaurant != null) {
        return super.update(access, id, inputMap);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found."));
      }
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user."));
  }

  @Override
  protected Restaurant getEntityForUpdate(Restaurant restaurant, HashMap<String, Object> inputMap) {
    restaurant.setName(inputMap.containsKey("name") ? inputMap.get("name").toString() : restaurant.getName());
    restaurant.setDescription(inputMap.containsKey("description") ? inputMap.get("description").toString() : restaurant.getDescription());
    restaurant.setAcceptDelivery(inputMap.containsKey("acceptDelivery") && Boolean.parseBoolean(inputMap.get("acceptDelivery").toString()));
    restaurant.setAcceptDineIn(inputMap.containsKey("acceptDineIn") && Boolean.parseBoolean(inputMap.get("acceptDineIn").toString()));
    restaurant.setAcceptTakeout(inputMap.containsKey("acceptTakeOut") && Boolean.parseBoolean(inputMap.get("acceptTakeOut").toString()));
    restaurant.setAddress1(inputMap.get("address1").toString());
    restaurant.setAddress2(inputMap.get("address2").toString());
    restaurant.setCity(inputMap.get("city").toString());
    restaurant.setKeyword(inputMap.containsKey("keyword") ? inputMap.get("keyword").toString() : restaurant.getKeyword());
    restaurant.setProvince(inputMap.get("province").toString());
    restaurant.setCountry(inputMap.get("country").toString());
    restaurant.setPostalCode(inputMap.get("postalCode").toString());
    restaurant.setPhoneNumber(inputMap.containsKey("phoneNumber") ? inputMap.get("phoneNumber").toString() : null);
    restaurant.setWebsite(inputMap.get("webSite").toString());
    restaurant.setContactPerson(inputMap.get("contactPerson").toString());
    restaurant.setActive(inputMap.containsKey("active") ? Boolean.parseBoolean(inputMap.get("active").toString()) : restaurant.isActive());
    restaurant.setNetworkStatus(inputMap.containsKey("networkStatus") ? NetworkStatus.valueOf(inputMap.get("networkStatus").toString()) : restaurant.getNetworkStatus());
    if (inputMap.containsKey("location")) {
      HashMap<String, Double> location = (HashMap<String, Double>) inputMap.get("location");
      double lat = location.get("lat");
      double lng = location.get("lng");
      GeometryFactory gf = new GeometryFactory();
      Point point = gf.createPoint(new Coordinate(lat, lng));
      restaurant.setLocation(point);
    }
    if (inputMap.containsKey("logo")) {
      restaurant.setLogo(inputMap.get("logo").toString());
    }
    if (inputMap.containsKey("cuisine")) {
      restaurant.setCuisine(inputMap.get("cuisine").toString());
    }
    return restaurant;
  }

  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "api delete restaurant")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = ""),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 500, message = "Cannot delete entity. Error message: ###"),
      @ApiResponse(code = 404, message = "Cannot found entity")
  })
  @Override
  public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
    if (access.getRole() == UserRole.ADMIN) {
      return super.delete(access, id);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("access denied for user"));
  }

  @Path("getRestaurantDetails")
  @ApiOperation(value = "Get menu and restaurant info")
  @GET
  public Response getRestaurantDetails(@QueryParam("status") String restaurantId) {
    Restaurant restaurant = restaurantDao.get(restaurantId);
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
    }
    else {
      List<RestaurantUser> entities = restaurant.getMenus();
      List<HashMap<String, Object>> dtos = new ArrayList<>();
      for (RestaurantUser dto : entities) {
        dtos.add(onGet(dto));
      }
      LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
      dto.put("restaurantusers", dtos);
      return ResourceUtils.asSuccessResponse(Status.OK, dto);
    }
  }

  @SuppressWarnings("Duplicates")
  @Path("/{restaurant_id}/order_details")
  @ApiOperation(value = "Get All Order Details By Restaurant", notes = "status=open/accepted/completed/scheduled/late <br/> from: 2015/01/01 <br/> to: 2015/03/19")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 400, message = "page not format number"),
      @ApiResponse(code = 404, message = "restaurant not found")
  })
  @GET
  public Response getOrderByRestaurantId(@ApiParam(access = "internal") @Auth User access,
      @PathParam("restaurant_id") String restaurant_id, @QueryParam("status") String status,
      @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("page") String page,
      @QueryParam("size") String size) {

    /*int pageInt;
    int sizeInt;
    if (restaurantDao.get(restaurant_id) != null) {
      try {
        pageInt = parseInteger(page);
        sizeInt = parseInteger(size);
        pageInt = pageInt == -1 ? 1 : pageInt;
        sizeInt = sizeInt == -1 ? 50 : sizeInt;
      }
      catch (Exception e) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Could not parse page or size."));
      }

      if (from != null && to != null) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date f = new Date();
        Date t = new Date();
        try {
          f = ft.parse(from);
          t = ft.parse(to);
        }
        catch (ParseException e) {
          System.out.println("Unparseable using " + ft);
        }
        entities = customerOrderDao.getListByRestaurant(restaurant_id, orderstatus, f, t, pageInt, sizeInt);
      }
      else {
        entities = customerOrderDao.getListByRestaurant(restaurant_id, orderstatus, pageInt, sizeInt);
      }
    }
    else {
      // status == null
      if (from != null && to != null) {
        // have from - to
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date f = new Date();
        Date t = new Date();
        try {
          f = ft.parse(from);
          t = ft.parse(to);
          System.out.println(f);
        }
        catch (ParseException e) {
          System.out.println("Unparseable using " + ft);
        }
        entities = customerOrderDao.getListByRestaurant(restaurant_id, f, t, iPage, iSize);
      }
      else {
        entities = customerOrderDao.getListByRestaurant(restaurant_id, iPage, iSize);

      }
    }
    List<HashMap<String, Object>> dtos = new ArrayList<>();
    for (Order dto : entities) {
      dtos.add(onGet(dto));
    }
    LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("orders", dtos);
    return ResourceUtils.asSuccessResponse(Status.OK, dto);
  }

  else

  {
    return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
  }*/
    return null;

}

  private HashMap<String, Object> onGet(Order entity) {
    HashMap<String, Object> uti = entity.toDto();


    List<HashMap<String, Object>> orderdetails = new ArrayList<>();
    Customer cus = customerDao.findByOrder(entity.getId().toString());
    HashMap<String, Object> customer = new HashMap<>();
    customer.put("id", cus.getId());
    customer.put("firstName", cus.getFirstName());
    customer.put("lastName", cus.getLastName());
    customer.put("email", cus.getEmail());
    customer.put("phoneNumber", cus.getPhoneNumber());

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
  @ApiOperation(value = "Get User List By Restaurant-Id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user")
  })
  public Response getUsersByRestaurantID(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
    if (restaurantDao.get(restaurant_id) == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
    }
    else if (access.getRole() == UserRole.ADMIN || access.getRole() == UserRole.OWNER) {
      List<RestaurantUser> entities = restaurantUserDao.getListByRestaurant(restaurant_id);
      List<HashMap<String, Object>> dtos = new ArrayList<>();
      for (RestaurantUser dto : entities) {
        dtos.add(onGet(dto));
      }
      LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
      dto.put("restaurantusers", dtos);
      return ResourceUtils.asSuccessResponse(Status.OK, dto);
    }
    return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Access denied for user"));
  }

  @Path("/{restaurant_id}/aboutus")
  @GET
  public Response getAboutUsByRestaurantId(@PathParam("restaurant_id") String restaurant_id) {
    if (restaurantDao.get(restaurant_id) != null) {
      /*List<Restaurant> entities = null ;
      List<HashMap<String, Object>> dtos = ModelHelpers.getMapListFromEntities(entities);*/
      return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("this is Utilsing for Aboutus"));
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
    }
  }

  @Path("/{restaurant_id}/holiday")
  @ApiOperation(value = "Set time  Dine-In Hours, Accept Delivery, Accept TakeOut")
  @PUT
  public Response setRestaurantHoliday(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto) {
    Restaurant restaurant = restaurantDao.get(restaurant_id);
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
              cday.setDate(Utils.convertTimeZones(restaurant.getTimezone(), "UTC", day.get("date").toString()));
            }
            catch (ParseException e) {
              e.printStackTrace();
              return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
            }
          }
          arrHour.add(cday);
        }
        restaurant.setClosedDay(arrHour);
      }
      dao.update(restaurant);
      return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(restaurant));
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
    }
  }

  @Path("/{restaurant_id}/time")
  @ApiOperation(value = "Set time  Dine-In Hours, Accept Delivery, Accept TakeOut", notes = "")
  @PUT
  public Response setTimeRestaurant(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto) {
    Restaurant restaurant = restaurantDao.get(restaurant_id);
    if (restaurant != null) {

      String timezone = dto.get("timeZoneId").toString();
      System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
      restaurant.setTimezone(timezone);
      if (dto.containsKey("dineInHours")) {
        List<HashMap<String, Object>> dineInHours = (List<HashMap<String, Object>>) dto.get("dineInHours");
        ArrayList<Hour> arrHour = new ArrayList<>();
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
            }
            catch (ParseException e) {
              e.printStackTrace();
              return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
            }
          }
          if (hashMapDineIn.containsKey("toTime")) {
            try {
              Date date = df.parse(hashMapDineIn.get("toTime").toString());
              hour.setToTime(Utils.convertTimeZones(timezone, "UTC", hashMapDineIn.get("toTime").toString()));
            }
            catch (ParseException e) {
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
        ArrayList<Hour> arrHour = new ArrayList<>();
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
            }
            catch (ParseException e) {
              e.printStackTrace();
              return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
            }
          }
          if (hashMapacceptDelivery.containsKey("toTime")) {
            try {
              Date date = df.parse(hashMapacceptDelivery.get("toTime").toString());
              hour.setToTime(Utils.convertTimeZones(timezone, "UTC", hashMapacceptDelivery.get("toTime").toString()));
            }
            catch (ParseException e) {
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
        ArrayList<Hour> arrHour = new ArrayList<>();
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
            }
            catch (ParseException e) {
              e.printStackTrace();
              return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage(e.getMessage()));
            }
          }
          if (hashMapacceptTakeOut.containsKey("toTime")) {
            try {
              Date date = df.parse(hashMapacceptTakeOut.get("toTime").toString());
              hour.setToTime(Utils.convertTimeZones(timezone, Utils.UTC, hashMapacceptTakeOut.get("toTime").toString()));
            }
            catch (ParseException e) {
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
        restaurant.setAcceptDelivery(acceptDelivery);
      }
      if (dto.containsKey("acceptTakeOut")) {
        boolean acceptTakeOut = Boolean.parseBoolean(dto.get("acceptTakeOut").toString());
        restaurant.setAcceptTakeout(acceptTakeOut);
      }
      dao.update(restaurant);
      return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(restaurant));
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
    }
  }

  @Override
  protected HashMap<String, Object> getMapFromEntity(Restaurant entity) {
    HashMap<String, Object> dto = new HashMap<>();
    dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
    return dto;
  }

  @Override
  protected HashMap<String, Object> onGet(Restaurant entity, User access) {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", entity.getId());
    dto.put("name", entity.getName());
    dto.put("description", entity.getDescription());
    dto.put("acceptDelivery", entity.isAcceptDelivery());
    dto.put("acceptDineIn", entity.isAcceptDineIn());
    dto.put("acceptTakeOut", entity.isAcceptTakeout());
    dto.put("location", entity.getLocation() != null ? new LatLng(entity.getLocation().getX(), entity.getLocation().getY()) : "");
    dto.put("address1", entity.getAddress1());
    dto.put("address2", entity.getAddress2());
    dto.put("city", entity.getCity());
    dto.put("keyword", entity.getKeyword());
    dto.put("province", entity.getProvince());
    dto.put("postalCode", entity.getPostalCode());
    dto.put("country", entity.getCountry());
    dto.put("phoneNumber", entity.getPhoneNumber());
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


    LinkedList<HashMap<String, Object>> holidayDtos = new LinkedList<>();
    for (ClosedDay day : entity.getClosedDay()) {
      HashMap<String, Object> closeday = new LinkedHashMap<>();
      closeday.put("id", day.getId());
      closeday.put("date", day.getDate());
      closeday.put("description", day.getDescription());
      holidayDtos.add(closeday);
    }

    dto.put("closedays", holidayDtos);
    dto.put("paymentTypes", entity.getPaymentTypes());
    dto.put("timezone", entity.getTimezone());
    dto.put("logo", entity.getLogo());
    System.out.println(entity.getMenus().size() + "++++++++++++++++++++++");
    // menus
    LinkedList<HashMap<String, Object>> menuDtos = new LinkedList<>();

    for (Menu menu : entity.getMenus()) {
      HashMap<String, Object> menuDto = new LinkedHashMap<>();
      menuDto.put("id", menu.getId());
      menuDto.put("name", menu.getName());
      menuDto.put("description", menu.getDescription());
      // Submenu
      ArrayList<HashMap<String, Object>> sunmenuDtos = new ArrayList<>();
      menuDto.put("submenus", sunmenuDtos);
      menuDtos.add(menuDto);
    }
    dto.put("menus", menuDtos);
    HashMap<String, Object> dtos = new HashMap<>();
    dtos.put(getClassT().getSimpleName().toLowerCase(), dto);
    return dtos;
  }


  private List<HashMap<String, Object>> fromEntitiesSearch(List<Restaurant> entities) {
    List<HashMap<String, Object>> dtos = new ArrayList<>();


    for (Restaurant entity : entities) {
      HashMap<String, Object> dto = new LinkedHashMap<>();
      dto.put("id", entity.getId());
      dto.put("name", entity.getName());
      dto.put("description", entity.getDescription());
      dto.put("acceptDelivery", entity.isAcceptDelivery());
      dto.put("acceptDineIn", entity.isAcceptDineIn());
      dto.put("acceptTakeOut", entity.isAcceptTakeout());
      dto.put("location", entity.getLocation() != null ? new LatLng(entity.getLocation().getX(), entity.getLocation().getY()) : "");
      dto.put("address1", entity.getAddress1());
      dto.put("address2", entity.getAddress2());
      dto.put("city", entity.getCity());
      dto.put("keyword", entity.getKeyword());
      dto.put("province", entity.getProvince());
      dto.put("postalCode", entity.getPostalCode());
      dto.put("country", entity.getCountry());
      dto.put("phoneNumber", entity.getPhoneNumber());
      dto.put("networkStatus", entity.getNetworkStatus());
      dto.put("contactPerson", entity.getContactPerson());
      dto.put("webSite", entity.getWebsite());
      dto.put("rating", entity.getRating());
      dto.put("active", entity.isActive());
      dto.put("logo", entity.getLogo());
      dto.put("distance", entity.getDistance());
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
      @ApiParam(required = true) @QueryParam("location") String latlng,
      @QueryParam("type") Integer type, @QueryParam("zone") String zone,
      @QueryParam("distance") double distance, @QueryParam("cusine") String cusine, @QueryParam("sorted") String sorted) {

    if (zone == null || zone.length() == 0) {
      throw new WebApplicationException(
          ResourceUtils.asSuccessResponse(Status.BAD_REQUEST, "zone parameter is mandatory")
      );
    }
    if (!Utils.isValidTimeZone(zone)) {
      throw new WebApplicationException(
          ResourceUtils.asSuccessResponse(Status.BAD_REQUEST, "zone not exist")
      );
    }
    try {
      distance = distance / 112;
      if (distance <= 0) {
        distance = .25;
      }
      SearchType searchType = null;
      searchType = type == null ? null : SearchType.fromInteger(type);
      if (latlng != null) {
        LatLng location = new LatLng(Double.parseDouble(latlng
            .split(",")[0]),
            Double.parseDouble(latlng.split(",")[1]));
        List<Restaurant> entities = restaurantDao.findDistanceNew(
            new GeometryFactory()
                .createPoint(new Coordinate(location.getLat(),
                    location.getLng())), distance);

        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("restaurants", fromEntitiesSearch(entities));
        return ResourceUtils.asSuccessResponse(Status.OK, dto);//getMapListFromEntities(entities));
      }
      else {


        List<Restaurant> entities = restaurantDao.findDistanceNew(searchType, cusine, sorted, zone);
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("restaurants", fromEntitiesSearch(entities));
        return ResourceUtils.asSuccessResponse(Status.OK, dto);

        //return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
        //		new ServiceErrorMessage("location is not null"));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(
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
  public Response get(@ApiParam(required = true) @QueryParam("location") String latlng,
      @QueryParam("distance") double distance) {
    try {
      if (latlng != null) {
        LatLng location = new LatLng(Double.parseDouble(latlng
            .split(",")[0]),
            Double.parseDouble(latlng.split(",")[1]));
        List<Restaurant> entities = restaurantDao.findDistanceNew(
            new GeometryFactory().createPoint(new Coordinate(
                location.getLat(), location.getLng())),
            distance); // 25KM
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("restaurants", fromEntitiesSearch(entities));
        return ResourceUtils.asSuccessResponse(Status.OK,
            dto);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST,
            new ServiceErrorMessage("location is not null"));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(
          "Format Incorrect " + e));
    }
  }

  @POST
  @Path("/{restaurant_id}/checkout")
  @ApiOperation(value = "api CheckOut for Customer")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "only for customer"),
      @ApiResponse(code = 404, message = "restaurant not found")
  })
  public Response checkOut(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto) {
    try {
      if (access.getRole() == UserRole.CUSTOMER) {
        Restaurant restaurant = restaurantDao.get(restaurant_id);
        if (restaurant != null) {

          Order co = new Order();
          //	co.setOrderStatus(OrderStatus.OPEN);
          co.setOrderType(OrderType.valueOf(dto.get("orderType").toString()));
          //co.setOrderNumber(new Date().getTime());
          co.setTip(Double.parseDouble(dto.get("tip").toString()));
          co.setReceivedTime(new Date());

          if (dto.containsKey("location")) {
            HashMap<String, Double> location = (HashMap<String, Double>) dto.get("location");
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
            ArrayList<HashMap<String, Object>> items = (ArrayList<HashMap<String, Object>>) dto.get("items");
            for (HashMap<String, Object> hashMap : items) {
              OrderDetail od = new OrderDetail();
              od.setQuantity(Integer.parseInt(hashMap.get("quantity").toString()));

              Item item = itemDao.get(hashMap.get("id").toString());
              //ItemPrice itemPrice = itemInfoDao.get(hashMap.get("info").toString());


              HashMap<String, Object> sizes = (HashMap<String, Object>) hashMap.get("sizes");

              /////Must Price is Sizes.........


              //od.setPrice(itemPrice.getPrice());
              od.setNote(hashMap.get("spacialNotes").toString());

              //item.addOrderDetails(od); //TODO}: temp comment
              co.addOrderDetail(od);
            }
          }
          Customer cus = customerDao.get(access.getId().toString());
          cus.addCustomerOrder(co);


          //	double defaultTax = restaurant.getTaxes() == null ? 0.0 : restaurant.getTaxes().getValue();

          //	double amount = calculatorAmount((ArrayList<HashMap<String, Object>>)dto.get("items"), Double.parseDouble(dto.get("tip").toString()), defaultTax, 0);

          if (dto.containsKey("paymentMethod")) {
            HashMap<String, String> paymentMethod = (HashMap<String, String>) dto.get("paymentMethod");
            if (paymentMethod.get("name").equals("Cash")) {


            }
            else {
              //			DineNowApplication.stripe.charge(amount, "usd", co.getPaymentMethod().getCardStripe(), cus.getCustomerStripe(), String.format("Charge for %s", cus.getEmail() != null ? cus.getEmail() : cus.getFullName() +" "+ cus.getLastName()));
            }
          }


          orderDao.save(co);
          customerDao.update(cus);
          return ResourceUtils.asSuccessResponse(Status.OK, co);

        }
        else {
          return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
        }
      }
      else {
        return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("only for customer"));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
    }
  }

  @Path("/{restaurant_id}/search_orders")
  @ApiOperation(value = "Search Order of Restaurant")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 401, message = "access denied for user"),
      @ApiResponse(code = 400, message = "page not format number"),
      @ApiResponse(code = 404, message = "restaurant not found")
  })
  @GET
  public Response searchByOrder(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id,
      @QueryParam("query") String query, @QueryParam("customer_phone") String c_ph, @QueryParam("order_no") long o_no) {
    if (restaurantDao.get(restaurant_id) != null) {
      if (query != null) {
        List<Order> entities = new ArrayList<>();
        entities = customerOrderDao.searchByOrder(restaurant_id, query);
        List<HashMap<String, Object>> dtos = new ArrayList<>();
        for (Order dto : entities) {
          dtos.add(onGet(dto));
        }
        return ResourceUtils.asSuccessResponse(Status.OK, dtos);
      }
      else if (c_ph != null) {
        List<Order> entities = new ArrayList<>();
        entities = customerOrderDao.searchByOrderAndCustomerPhone(restaurant_id, c_ph);
        List<HashMap<String, Object>> dtos = new ArrayList<>();
        for (Order dto : entities) {
          dtos.add(onGet(dto));
        }
        return ResourceUtils.asSuccessResponse(Status.OK, dtos);
      }
      else if (o_no > 0) {
        List<Order> entities = new ArrayList<>();
        entities = customerOrderDao.searchByOrderNo(restaurant_id, o_no);
        List<HashMap<String, Object>> dtos = new ArrayList<>();
        for (Order dto : entities) {
          dtos.add(onGet(dto));
        }
        LinkedHashMap<String, Object> dto = new LinkedHashMap<>();
        dto.put("orders", dtos);
        return ResourceUtils.asSuccessResponse(Status.OK, dto);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("query is not null"));
      }
    }
    else {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant not found"));
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
    List<Restaurant> entities = this.dao.getAll();

    List<HashMap<String, Object>> dtos = new ArrayList<>();

    for (Restaurant entity : entities) {
      HashMap<String, Object> entitys = getMapFromEntity(entity);
      List<DeliveryZone> dzs = deliveryZoneDao
          .getAllDeliveryZonesByRestaurantId(entity.getId());

      List<HashMap<String, Object>> deliverys = new ArrayList<>();

      for (int i = 0; i < dzs.size(); i++) {
        HashMap<String, Object> delivery = new HashMap<>();

        delivery.put("id", dzs.get(i).getId());
        delivery.put("name", dzs.get(i)
            .getName());
        delivery.put("description", dzs.get(i)
            .getDescription());
        delivery.put("minimum", dzs.get(i).getMinimum());
        delivery.put("fee", dzs.get(i).getFee());
        delivery.put("type", dzs.get(i)
            .getType());
        List<LatLng> coords = new ArrayList<>();
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
  public Response getInfo(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id) {
    Restaurant res = restaurantDao.get(restaurant_id);
    if (res != null) {
      return ResourceUtils.asSuccessResponse(Status.OK, getMapFromEntity(res));
    }
    return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("restaurant found info"));
  }


  private double calculatorAmount(ArrayList<HashMap<String, Object>> itemOrder, double tip, double tax, double discount) {
    double total = 0;
    double subtotal = 0;
    for (HashMap<String, Object> hashMap : itemOrder) {
      HashMap<String, Object> size = (HashMap<String, Object>) hashMap.get("sizes");

      double priceSize = Double.parseDouble(size.get("price").toString());
      int quantity = Integer.parseInt(hashMap.get("quantity").toString());
      subtotal += priceSize * quantity;
      List<HashMap<String, Object>> addOns = (List<HashMap<String, Object>>) hashMap.get("addOns");
      double priceAddOn = 0;
      for (HashMap<String, Object> hashMap2 : addOns) {
        priceAddOn += Double.parseDouble(hashMap2.get("price").toString());
      }
      subtotal += priceAddOn;
    }
    double tipCalculator = (subtotal * tip) / 100;
    double taxCalculator = (subtotal * tax) / 100;
    double discountCalculator = (subtotal * discount) / 100;

    total = subtotal + tipCalculator + taxCalculator + discountCalculator;
    return total;
  }
}

