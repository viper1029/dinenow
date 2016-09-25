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

@Path("/restaurants/getRestaurantDetails")
@Api("/restaurants/getRestaurantDetails")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantInfoResource extends AbstractResource<Restaurant> {

  @Inject
  private RestaurantDao restaurantDao;

  @Path("/")
  @ApiOperation(value = "Get menu and restaurant info")
  @GET
  public Response getRestaurantDetails(@QueryParam("restaurantId") String restaurantId) {
    Restaurant restaurant = restaurantDao.get(restaurantId);
    if (restaurant == null) {
      return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Restaurant not found."));
    }
    else {
      Set<Menu> menus = restaurant.getMenus();
      HashMap<String, Object> dto = new HashMap<String, Object>();
      if(menus.size() >= 1) {
        Menu menu = menus.iterator().next();
        dto.put("id", restaurant.getId());
        dto.put("name", restaurant.getName());
        dto.put("description", restaurant.getDescription());
        dto.put("acceptDelivery", restaurant.isAcceptDelivery());
        dto.put("acceptTakeOut", restaurant.isAcceptTakeout());
        dto.put("location", restaurant.getLocation() != null ? new LatLng(restaurant.getLocation().getX(), restaurant.getLocation().getY()) : "");
        dto.put("address1", restaurant.getAddress1());
        dto.put("address2", restaurant.getAddress2());
        dto.put("city", restaurant.getCity());
        dto.put("province", restaurant.getProvince());
        dto.put("postalCode", restaurant.getPostalCode());
        dto.put("country", restaurant.getCountry());
        dto.put("phoneNumber", restaurant.getPhoneNumber());
        dto.put("networkStatus", restaurant.getNetworkStatus());
        dto.put("webSite", restaurant.getWebsite());
        dto.put("rating", restaurant.getRating());
        dto.put("acceptDeliveryHours", restaurant.getAcceptDeliveryHours());
        dto.put("acceptTakeOutHours", restaurant.getAcceptTakeOutHours());
        dto.put("logo", restaurant.getLogo());
        dto.put("menu", menu.toDto());
      }
      return ResourceUtils.asSuccessResponse(Status.OK, dto);
    }
  }

   private HashMap<String, Object> onGet(RestaurantUser entity) {
    HashMap<String, Object> uti = entity.toDto();
    return uti;
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
}

