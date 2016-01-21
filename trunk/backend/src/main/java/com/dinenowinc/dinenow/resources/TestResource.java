package com.dinenowinc.dinenow.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.Map;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.LatLng;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.service.CustomerService;
import com.dinenowinc.dinenow.service.RestaurantUserService;
import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Signer;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenClaim;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenHeader;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/test")
@Api("/test")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class TestResource {
	@Inject
	private RestaurantDao restaurantDao;
	@Inject
	private RestaurantUserService userService;
	@Inject
	private CustomerService customerService;
	private final byte[] tokenSecret;
	public TestResource()
	{
		this.tokenSecret = DineNowApplication.authKey;
	}
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	@POST
	@ApiOperation(value="api login (admin, owner, customer)",notes=""
			+ "<p>owner Res 1 (Nga tu bay hien)</p>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"email\": \"abc@gmail.com\","
			+ "<br/>  \"password\": \"12345678\""
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<p>owner Res 2 (Lang Cha ca)</p>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"email\": \"123@gmail.com\","
			+ "<br/>  \"password\": \"12345678\""
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<p>admin</p>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"email\": \"admin@gmail.com\","
			+ "<br/>  \"password\": \"12345678\""
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<p>Customer</p>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"email\": \"1234@gmail.com\","
			+ "<br/>  \"password\": \"12345678\""
			+ "<br/>}</code></pre>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<br/>"
			+ "<pre><code>{"
			+ "<br/>  \"email\": \"12345@gmail.com\","
			+ "<br/>  \"password\": \"12345678\""
			+ "<br/>}</code></pre>" )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			  @ApiResponse(code = 400, message = "Email cannot be empty"),
			  @ApiResponse(code = 400, message = "Email is not in correct format"),
			  @ApiResponse(code = 400, message = "Password cannot be empty"),
			  @ApiResponse(code = 400, message = "Exception: ###"),
			  @ApiResponse(code = 401, message = "Invalid username or password") 
			})
	@Path("/login")
	public Response login(HashMap<String, Object> dto)  {
		try {
			String email = dto.get("email").toString();
			String password = dto.get("password").toString();

			if(email == null || email.length() == 0)
			{
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email cannot be empty"));
			}
			else
			{
				if(!email.matches(EMAIL_PATTERN))
				{
					return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email is not in correct format"));				
				}			
			}
			
			if(password == null || password.length() == 0)
			{
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Password cannot be empty"));
			}
			int expire_in_seconds = 60 * 1440; //  24h
			RestaurantUser uLogin = userService.checkCredentials(email, password);
			
			if (uLogin == null) {
				Customer uCustomer = customerService.checkCredentials(email, password);
				if (uCustomer == null) {
					return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Invalid username or password"));
				}else {
					Map<String, Object> data = generateToken(uCustomer.getId(), UserRole.CUSTOMER, expire_in_seconds, uCustomer , null);
					return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
				}			
			}
			Restaurant restaurant = restaurantDao.findByIDUser(uLogin.getId());
			Map<String, Object> data = generateToken(uLogin.getId(), uLogin.getRole(), expire_in_seconds, uLogin, restaurant);
			return ResourceUtils.asSuccessResponse(Response.Status.OK, "ok");	
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
		}	
	}

	private Map<String,Object> generateToken(String id, UserRole role,int expire_in_seconds, Object user, Restaurant restaurant){
		final HmacSHA512Signer signer = new HmacSHA512Signer(tokenSecret);
		final JsonWebToken access_token = JsonWebToken
				.builder()
				.header(JsonWebTokenHeader.HS512())
				.claim(JsonWebTokenClaim
						.builder()
						.param("id", id).param("role", role)
						.issuedAt(new DateTime())
						.expiration(new DateTime().plusSeconds(expire_in_seconds))
						.build()).build();
		final String signedToken = signer.sign(access_token);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("access_token", signedToken);
		data.put("expire_in_seconds", expire_in_seconds);
		data.put("user", user);
		data.put("role", role);
		
		if (restaurant != null) {
			HashMap<String, Object> dto = new HashMap<String, Object>();
			dto.put("id", restaurant.getId());
			dto.put("name", restaurant.getName());
			dto.put("active", restaurant.isActive());
			dto.put("description", restaurant.getDescription());
			dto.put("acceptDelivery", restaurant.isAccept_delivery());
			dto.put("acceptTakeOut", restaurant.isAccept_takeout());
			dto.put("acceptDineIn", restaurant.isAccept_dinein());
			dto.put("address1", restaurant.getAddress_1());
			dto.put("address2", restaurant.getAddress_2());
			dto.put("city", restaurant.getCity());
			dto.put("keyword", restaurant.getKeyword());
			dto.put("province", restaurant.getProvince());
			dto.put("postalCode", restaurant.getPostal_code());
			dto.put("country", restaurant.getCountry());
			dto.put("networkStatus", restaurant.getNetworkStatus());
			dto.put("contactPerson", restaurant.getContactPerson());
			dto.put("webSite", restaurant.getWebsite());
			dto.put("stripe", restaurant.getStripe());
			dto.put("phoneNumber", restaurant.getPhone_number());
			dto.put("dineInHours", restaurant.getDineInHours());
			dto.put("acceptDeliveryHours", restaurant.getAcceptDeliveryHours());
			dto.put("acceptTakeOutHours", restaurant.getAcceptTakeOutHours());
			dto.put("cuisine", restaurant.getCuisine());
			
			data.put("restaurant", dto);
		}else {
			data.put("restaurant", null);
		}

		return data;
	}
	
}
