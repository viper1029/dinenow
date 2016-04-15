package com.dinenowinc.dinenow.auth;

import com.dinenowinc.dinenow.Constants;
import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.model.Admin;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Signer;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenClaim;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenHeader;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class TokenGenerator {

  public static Map<String, Object> generateToken(String id, UserRole role, Object user, Restaurant restaurant) {
    String signedToken = "";
    Map<String, Object> data = new HashMap<>();
    HashMap<String, Object> returnObject = new HashMap<>();

    if (user instanceof Customer) {
      signedToken = generateTokenForCustomer(user, returnObject, role);
    }
    else if (user instanceof RestaurantUser) {
      signedToken = generateTokenForRestaurantUser(user, returnObject, role);
    }
    else if (user instanceof Admin) {
      signedToken = generateTokenForAdmin(user, returnObject, role);
    }

    data.put("access_token", signedToken);
    data.put("user", returnObject);
    data.put("role", role);

    if (restaurant != null) {
      HashMap<String, Object> dto = new HashMap<>();
      dto.put("id", restaurant.getId());
      dto.put("name", restaurant.getName());
      dto.put("active", restaurant.isActive());
      dto.put("description", restaurant.getDescription());
      dto.put("acceptDelivery", restaurant.isAcceptDelivery());
      dto.put("acceptTakeOut", restaurant.isAcceptTakeout());
      dto.put("acceptDineIn", restaurant.isAcceptDineIn());
      dto.put("address1", restaurant.getAddress1());
      dto.put("address2", restaurant.getAddress2());
      dto.put("city", restaurant.getCity());
      dto.put("keyword", restaurant.getKeyword());
      dto.put("province", restaurant.getProvince());
      dto.put("postalCode", restaurant.getPostalCode());
      dto.put("country", restaurant.getCountry());
      dto.put("networkStatus", restaurant.getNetworkStatus());
      dto.put("contactPerson", restaurant.getContactPerson());
      dto.put("webSite", restaurant.getWebsite());
      dto.put("stripe", restaurant.getStripe());
      dto.put("phoneNumber", restaurant.getPhoneNumber());
      dto.put("dineInHours", restaurant.getDineInHours());
      dto.put("acceptDeliveryHours", restaurant.getAcceptDeliveryHours());
      dto.put("acceptTakeOutHours", restaurant.getAcceptTakeOutHours());
      dto.put("cuisine", restaurant.getCuisine());

      data.put("restaurant", dto);
    }/*else {
      data.put("restaurant", null);
		}*/
    System.out.println(data);
    return data;
  }

  private static String generateTokenForCustomer(Object user, HashMap<String, Object> returnObject, UserRole role) {
    Customer customer = (Customer) user;
    returnObject.put("id", customer.getId());
    returnObject.put("firstName", customer.getFirstName());
    returnObject.put("lastName", customer.getLastName());
    returnObject.put("email", customer.getEmail());
    returnObject.put("phoneNumber", customer.getPhoneNumber());

    final HmacSHA512Signer signer = new HmacSHA512Signer(DineNowApplication.authKey);
    final JsonWebToken access_token = JsonWebToken
        .builder()
        .header(JsonWebTokenHeader.HS512())
        .claim(JsonWebTokenClaim
            .builder()
            .param("id", customer.getId()).param("role", role).param("name", customer.getFirstName() + ' ' + customer.getLastName()).param("email", customer.getEmail())
            .issuedAt(new DateTime())
            .expiration(new DateTime().plusSeconds(Constants.TOKEN_EXPIRY_IN_SECONDS))
            .build()).build();
    return signer.sign(access_token);
  }

  private static String generateTokenForRestaurantUser(Object user, HashMap<String, Object> returnObject, UserRole role) {
    RestaurantUser restaurantUser = (RestaurantUser) user;
    returnObject.put("id", restaurantUser.getId());
    returnObject.put("name", restaurantUser.getName());
    returnObject.put("email", restaurantUser.getEmail());
    returnObject.put("phoneNumber", restaurantUser.getPhone());

    final HmacSHA512Signer signer = new HmacSHA512Signer(DineNowApplication.authKey);
    final JsonWebToken access_token = JsonWebToken
        .builder()
        .header(JsonWebTokenHeader.HS512())
        .claim(JsonWebTokenClaim
            .builder()
            .param("id", restaurantUser.getId()).param("role", role).param("name", restaurantUser.getName()).param("email", restaurantUser.getEmail())
            .issuedAt(new DateTime())
            .expiration(new DateTime().plusSeconds(Constants.TOKEN_EXPIRY_IN_SECONDS))
            .build()).build();
    return signer.sign(access_token);
  }

  private static String generateTokenForAdmin(Object user, HashMap<String, Object> returnObject, UserRole role) {
    Admin admin = (Admin) user;
    returnObject.put("id", admin.getId());
    returnObject.put("firstName", admin.getFirstName());
    returnObject.put("lastName", admin.getLastName());
    returnObject.put("email", admin.getEmail());

    final HmacSHA512Signer signer = new HmacSHA512Signer(DineNowApplication.authKey);
    final JsonWebToken access_token = JsonWebToken
        .builder()
        .header(JsonWebTokenHeader.HS512())
        .claim(JsonWebTokenClaim
            .builder()
            .param("id", admin.getId()).param("role", role).param("name", admin.getFirstName() + ' ' + admin.getLastName()).param("email", admin.getEmail())
            .issuedAt(new DateTime())
            .expiration(new DateTime().plusSeconds(Constants.TOKEN_EXPIRY_IN_SECONDS))
            .build()).build();
    return signer.sign(access_token);
  }
}
