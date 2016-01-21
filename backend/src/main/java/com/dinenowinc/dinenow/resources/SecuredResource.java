package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.AdminDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Admin;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.service.AdminService;
import com.dinenowinc.dinenow.service.CustomerService;
import com.dinenowinc.dinenow.service.RestaurantUserService;
import com.dinenowinc.dinenow.utils.JavaMail;
import com.dinenowinc.dinenow.utils.MD5Hash;
import com.dinenowinc.dinenow.utils.Utils;
import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Signer;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenClaim;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenHeader;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
@Api("/auth")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class SecuredResource {

  @Inject
  private RestaurantDao restaurantDao;

  @Inject
  private RestaurantUserDao userDao;

  @Inject
  private RestaurantUserService userService;

  @Inject
  private CustomerService customerService;

  @Inject
  private CustomerDao customerDao;

  @Inject
  private AdminService adminService;

  @Inject
  private AdminDao adminDao;

  private final byte[] tokenSecret;

  private static final Logger LOGGER = LoggerFactory.getLogger(SecuredResource.class);

  private static final int EXPIRY_IN_SECONDS = 60 * 1440;

  private static final Pattern CREDENTIALS_PATTERN = Pattern.compile("(.*?):(.*)");

  public SecuredResource() {
    this.tokenSecret = DineNowApplication.authKey;
  }

  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  @POST
  @ApiOperation(value = "login (admin, owner, customer)", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 400, message = "Email cannot be empty."),
      @ApiResponse(code = 400, message = "Email is not in correct format."),
      @ApiResponse(code = 400, message = "Password cannot be empty"),
      @ApiResponse(code = 401, message = "Invalid username or password."),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/login")
  public Response login(@Context HttpHeaders headers) {

    if (headers == null) {
      ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: missing headers."));
    }

    if (headers.getRequestHeader("Authentication") == null) {
      ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: missing Authentication header."));
    }

    String authentication = headers.getRequestHeader("Authentication").toString();
    final String encodedEmailAndPassword = authentication.replaceFirst("Basic ", "");

    String usernameAndPassword = null;
    try {
      byte[] decodedBytes = Base64.decodeBase64(encodedEmailAndPassword);
      usernameAndPassword = new String(decodedBytes, "UTF-8");
    }
    catch (IOException e) {
      LOGGER.error("Exception: could not decode authentication string", e);
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: could not decode authentication string"));
    }

    try {
      Matcher matcher = CREDENTIALS_PATTERN.matcher(usernameAndPassword);
      if (!matcher.matches()) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email or Password cannot be empty"));
      }

      final String email = matcher.group(1);
      String password = matcher.group(2);

      if (email == null || email.length() == 0) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email cannot be empty"));
      }
      else if (!email.matches(EMAIL_PATTERN)) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email is not in correct format"));
      }

      else if (password == null || password.length() == 0) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Password cannot be empty"));
      }
      password = MD5Hash.md5Spring(password);


      // Start login services

      RestaurantUser uLogin = userService.checkCredentials(email, password);

      if (uLogin == null) {
        Admin uAdmin = adminService.checkCredentials(email, password);
        if (uAdmin == null) {
          Customer uCustomer = customerService.checkCredentials(email, password);
          if (uCustomer == null) {

            return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Invalid username or password"));
          }
          else {
            Map<String, Object> data = generateToken(uCustomer.getId(), UserRole.CUSTOMER, uCustomer, null);
            return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
          }
        }
        else { // System.out.println("uAdmin-else");
          Map<String, Object> data = generateToken(uAdmin.getId(), UserRole.ADMIN, uAdmin, null);
          return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
        }

      }
      Restaurant restaurant = restaurantDao.findByIDUser(uLogin.getId());
      //System.out.println(">>>>>>>>>." + restaurant );
      Map<String, Object> data = generateToken(uLogin.getId(), uLogin.getRole(), uLogin, restaurant);
      return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
    }
    catch (Exception e) {
      System.out.println("No Headers Found");
      e.printStackTrace();
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }

  @POST
  @ApiOperation(value = "api forget password", notes = "Insert type,<pre><code>{\"email\":\"1234@gmail.com\"}</code></pre><br/><br/><pre><code>{\"phone\":\"+841674546172\"}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "have an SMS was sent"),
      @ApiResponse(code = 200, message = "have an email was sent"),
      @ApiResponse(code = 400, message = "format must containskey 'phone'"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 400, message = "format must containskey 'email'"),
      @ApiResponse(code = 400, message = "this email does not exist"),
      @ApiResponse(code = 400, message = "format must containskey 'phone' or 'email'"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/forget")
  public Response forget(@QueryParam("type") String type, HashMap<String, Object> information) {
    try {
      if (information.containsKey("phone")) {
        String phone = information.get("phone").toString();
        if (phone == null) {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("format must containskey 'phone'"));
        }
        String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());

        if (type != null && type.equals("resturant")) {
          RestaurantUser uInfo = userDao.getRestaurantUserByPhoneNumber(phone);
          if (uInfo == null) {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("this phone number does not exist"));
          }
          else if (Utils.sendSMSCode("Reset", token, phone)) {
            uInfo.setReset_key(token);
            userDao.update(uInfo);
            return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("OTP sent to your number"));
          }
          else {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("cannot send SMS"));
          }

        }
        else if (Utils.sendSMSCode("Reset", token, phone)) {
          Customer cus = customerDao.getCustomerByPhoneNumber(phone);
          cus.setResetKey(token);
          Date timeReset = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
          cus.setResetKeyTime(timeReset);
          customerDao.update(cus);

          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("OTP sent to your number"));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("cannot send SMS"));
        }
      }
      else {
        if (information.containsKey("email")) {
          String email = information.get("email").toString();
          if (email == null) {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("format must containskey 'email'"));
          }
          if (email != null && email.matches(EMAIL_PATTERN)) {

            if (type != null && type.equals("resturant")) {
              RestaurantUser uInfo = userDao.getRestaurantUserByEmail(email);
              if (uInfo == null) {
                return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("this email does not exist"));
              }
              String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());
              JavaMail blah = new JavaMail(email, "please copy code below to change the password", token);
              uInfo.setReset_key(token);
              userDao.update(uInfo);
            }
            else {
              Customer uInfo = customerService.getUserByEmail(email);
              if (uInfo == null) {
                return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("this email does not exist"));
              }
              String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());
              JavaMail blah = new JavaMail(email, "please copy code below to change the password", token);
              uInfo.setResetKey(token);
              Date timeReset = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
              uInfo.setResetKeyTime(timeReset);
              customerDao.update(uInfo);
            }
            return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Please check your email for instruction on how to rest your password.. "));
          }
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("this email does not exist"));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("format must containskey 'phone' or 'email'"));
        }
      }
    }
    catch (MessagingException m) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: Internet connection failed "));
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "api confirm forget password", notes = "Insert resetKey and type,<br/><br/><pre><code>{\"password\":\"newPassword\"}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "succeeded"),
      @ApiResponse(code = 410, message = "resetKey expired!!!!!"),
      @ApiResponse(code = 400, message = "new password empty or too short"),
      @ApiResponse(code = 400, message = "new password empty or too short"),
      @ApiResponse(code = 400, message = "not found customer"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/forget/confirm")
  public Response confirm(@QueryParam("type") String type, HashMap<String, Object> information) {

    try {
      String passwordNew = information.get("password").toString();
      String resetKey = information.get("resetKey").toString();
      if (type != null && type.equals("resturant")) {
        RestaurantUser cus = userDao.findByResetKey(resetKey);
        if (cus != null) {
          if (passwordNew != null && passwordNew.length() >= 6) {
            cus.setPassword(MD5Hash.md5Spring(passwordNew));
            cus.setReset_key(null);
            userDao.update(cus);
            return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("succeeded"));
          }
          else {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("new password empty or too short"));
          }
        }
        else {
          return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("ResturantUser not found "));
        }
      }
      else {
        Customer cus = customerDao.findByResetKey(resetKey);
        if (cus != null) {
          if (cus.getResetKeyTime().getTime() <= new Date().getTime()) {
            cus.setResetKey(null);
            cus.setResetKeyTime(null);
            customerDao.update(cus);
            return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("resetKey expired!!!!!"));
          }

          if (passwordNew != null && passwordNew.length() >= 6) {
            cus.setPassword(MD5Hash.md5Spring(passwordNew));
            cus.setResetKey(null);
            cus.setResetKeyTime(null);
            customerDao.update(cus);
            return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("succeeded"));
          }
          else {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("new password empty or too short"));
          }
        }
        else {
          return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Customer not found"));
        }
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }





	/*
   * register normal
	 * 
	 * {"firstName":"first",
		"lastName":"last",
		"email":"hien@gmail.com",
		"password":"1234567",
		"phoneNumber":"09864545",
		"socialAccounts":""}
	 */

  @POST
  @ApiOperation(value = "api register for customer", notes = "<pre><code>{"
      + "<br/>  \"firstName\": \"hien\","
      + "<br/>  \"lastName\": \"nguyen\","
      + "<br/>  \"email\": \"hien12@gmail.com\","
      + "<br/>  \"password\": \"123456\","
      + "<br/>  \"phoneNumber\": \"0903\""
      + "<br/>}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 400, message = "list error"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/register")
  public Response resgiterCustomer(HashMap<String, Object> customer) {
    System.out.println("in code");

    try {
      String firstName = customer.get("firstName").toString();
      String lastName = customer.get("lastName").toString();
      String email = customer.get("email").toString();
      String password = customer.get("password").toString();
      String phoneNumber = customer.get("phoneNumber").toString();
      Customer customerInput = new Customer();
      customerInput.setFirstName(firstName);
      customerInput.setLastName(lastName);
      customerInput.setEmail(email);
      customerInput.setPassword(MD5Hash.md5Spring(password));
      customerInput.setPhoneNumber(phoneNumber);
      customerInput.setCreatedBy(firstName);
      customerInput.setCreatedDate(new Date());
      String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());
      System.out.println(token);


      if (Utils.sendSMSCode("Validation", token, phoneNumber)) {

        ServiceResult<Customer> serviceResult = customerService.createNewCustomer(customerInput);
        if (serviceResult.hasErrors()) {
          return ResourceUtils.asFailedResponse(Response.Status.CONFLICT, serviceResult.getErrors());
        }
        int expire_in_seconds = 60 * 1440;
        Map<String, Object> data = generateToken(serviceResult.getResult().getId(), UserRole.CUSTOMER, serviceResult.getResult(), null);
        Customer cus = customerDao.getCustomerByEmail(email);
        cus.setValidationCode(token);
        Date timeValidation = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
        cus.setValidationCodeTime(timeValidation);
        String id_customer_stripe = DineNowApplication.stripe.createCustomer(cus);
        cus.setCustomerStripe(id_customer_stripe);
        customerDao.update(cus);

        return ResourceUtils.asSuccessResponse(Status.OK, data);
      }
      else {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Can't send SMS"));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResourceUtils.asFailedResponse(Response.Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "api validation phone for customer", notes = "id of customer<br/><br/><pre><code>{\"id\":\"88d91bd5-a7bb-48a5-a368-6432ab9387f1\"}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "validation phone..."),
      @ApiResponse(code = 410, message = "validationCode expired!!!!!"),
      @ApiResponse(code = 400, message = "incorrect validation code"),
      @ApiResponse(code = 400, message = "Exception: ###"),
      @ApiResponse(code = 404, message = "not found customer"),
  })
  @Path("/validation")
  public Response validationPhone(@QueryParam("validationCode") String validationCode, @UnwrapValidatedValue HashMap<String, Object> information) {
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    try {
      if (information.get("id") == null) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(String.format("Please provide customer ID", "Please provide customer ID")));
      }
      String idCustomer = information.get("id").toString();
      Customer cus = customerDao.findOne(idCustomer);
      if (cus != null) {
        if (cus.getValidationCode() != null && cus.getValidationCodeTime().getTime() <= new Date().getTime()) {
          cus.setValidationCode(null);
          cus.setValidationCodeTime(null);
          cus.setPhone_number_valid(false);
          customerDao.update(cus);
          return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("validationCode expired!!!!!"));
        }

        if (cus.getValidationCode() != null && validationCode.equals(cus.getValidationCode())) {
          cus.setValidationCode(null);
          cus.setValidationCodeTime(null);
          cus.setPhone_number_valid(true);

          if (!cus.isPhone_point()) {
            cus.setPoint(cus.getPoint() + Utils.DUMMY_POINT);
            cus.setPhone_point(true);
          }
          customerDao.update(cus);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Phone number has been verified."));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("incorrect validation code"));
        }
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("c"));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "api resent validation code", notes = "id of customer<br/><br/><pre><code>{\"id\":\"88d91bd5-a7bb-48a5-a368-6432ab9387f1\"}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "validation code send to phone"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 400, message = "Exception: ###"),
      @ApiResponse(code = 404, message = "not found customer")
  })
  @Path("/resend")
  public Response resendValidationPhone(HashMap<String, Object> information) {
    try {
      String idCustomer = information.get("id").toString();


      Customer cus = customerDao.findOne(idCustomer);

      if (cus != null) {
        String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());

        if (Utils.sendSMSCode("Validation", token, cus.getPhoneNumber())) {
          cus.setValidationCode(token);
          Date timeValidation = DateUtils.addHours(new Date(), DineNowApplication.timeValidationCode);
          cus.setValidationCodeTime(timeValidation);
          customerDao.update(cus);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Validation code send to phone"));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("cannot send SMS"));
        }
      }
      else {
        return ResourceUtils.asSuccessResponse(Status.NOT_FOUND, new ServiceErrorMessage("not found customer"));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "api connect social network", notes = "<pre><code>{"
      + "<br/>  \"firstName\": \"hien\","
      + "<br/>  \"lastName\": \"nguyen\","
      + "<br/>  \"email\": \"amit@gmail.com\","
      + "<br/>  \"socialAccount\": \"google:12345676\""
      + "<br/>}</code></pre>"
      + "<br/>"
      + "<br/>"
      + "<br/>"
      + "<br/>"
      + "social network startwith \"google:123\" or \"facebook:123\"")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 400, message = "list error"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/connectsocial")
  public Response registerSocial(HashMap<String, Object> social) {
    try {
      if (!social.containsKey("socialAccount")) {
        System.out.println("cdscsdcscsd");
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, "socialAccount needed");
      }
      if (!social.containsKey("email")) {
        System.out.println("cdscsdcscsd");
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, "email needed");
      }
      if (!social.containsKey("firstName")) {
        System.out.println("cdscsdcscsd");
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, "firstName needed");
      }
      String socialStartWith = social.get("socialAccount").toString();
      String firstName = social.get("firstName").toString();
      String email = social.get("email").toString();
      Customer customerInput = new Customer();
      customerInput.setFirstName(firstName);
      customerInput.setLastName("_");
      if (social.containsKey("lastName")) {
        String lastName = social.get("lastName").toString();
        customerInput.setLastName(lastName);
      }
      //customerInput.addSocialAccounts(socialStartWith);
      customerInput.setCreatedBy(socialStartWith.substring(0, 6));
      customerInput.setEmail(email); //email need to be present
      customerInput.setPassword(MD5Hash.md5Spring(socialStartWith));
      ServiceResult<Customer> serviceResult = customerService.createNewCustomerFOrSocical(customerInput, socialStartWith);
      if (serviceResult != null && serviceResult.hasErrors()) {
        return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, serviceResult.getErrors());
      }
      int expire_in_seconds = 60 * 1440;
      Map<String, Object> data = generateToken(serviceResult.getResult().getId(), UserRole.CUSTOMER, serviceResult.getResult(), null);
      return ResourceUtils.asSuccessResponse(Status.OK, data);
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }

  }


  @POST
  @ApiOperation(value = "api update number phone for customer", notes = "<pre><code>{"
      + "<br/>  \"id\": \"88d91bd5-a7bb-48a5-a368-6432ab9387f1\","
      + "<br/>  \"phoneNumber\": \"0987654333\","
      + "<br/>}</code></pre>")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 400, message = "not format number phone"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 404, message = "customer not found"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/phone_number")
  public Response connectsocialUpdateNumber(HashMap<String, Object> info) {
    try {
      String id = info.get("id").toString();
      String phoneNumber = info.get("phoneNumber").toString();
      Customer customer = customerDao.findOne(id);
      if (customer != null) {

        HashMap<String, Object> dto = new HashMap<String, Object>();
        dto.put("id", customer.getId());
        dto.put("email", customer.getEmail());
        dto.put("firstName", customer.getFirstName());
        dto.put("lastName", customer.getLastName());

        if (customer.getPhoneNumber().equals(phoneNumber)) {
          dto.put("phoneNumber", customer.getPhoneNumber());
          return ResourceUtils.asSuccessResponse(Status.OK, dto);
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
          PhoneNumber swissNumberProto = phoneUtil.parse(phoneNumber, Locale.getDefault().getCountry());
          if (PhoneNumberUtil.getInstance().isValidNumber(swissNumberProto)) {
            String token = RandomStringUtils.random(6, 0, 0, false, true, null, new SecureRandom());
            if (Utils.sendSMSCode("Validation", token, phoneNumber)) {
              customer.setPhone_number_valid(false);
              ;
              customer.setValidationCode(token);
              Date timeValidation = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
              customer.setValidationCodeTime(timeValidation);
              customer.setPhoneNumber(phoneNumber);
              customerDao.update(customer);
              dto.put("phoneNumber", customer.getPhoneNumber());
              return ResourceUtils.asSuccessResponse(Status.OK, dto);
            }
            else {
              return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("cannot send SMS"));
            }
          }
          else {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("not format number phone"));
          }
        }
        catch (NumberParseException e) {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("not format number phone"));
        }
      }
      else {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("customer not found"));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  private Map<String, Object> generateToken(String id, UserRole role, Object user, Restaurant restaurant) {

    String signedToken = "";
    HashMap<String, Object> rdto = new HashMap<String, Object>();
    if (user instanceof RestaurantUser) {
      RestaurantUser ruser = (RestaurantUser) user;
      rdto.put("id", ruser.getId());
      rdto.put("firstName", ruser.getFirstName());
      rdto.put("lastName", ruser.getLastName());
      rdto.put("email", ruser.getEmail());
      rdto.put("phoneNumber", ruser.getPhone());

      final HmacSHA512Signer signer = new HmacSHA512Signer(tokenSecret);
      final JsonWebToken access_token = JsonWebToken
          .builder()
          .header(JsonWebTokenHeader.HS512())
          .claim(JsonWebTokenClaim
              .builder()
              .param("id", id).param("role", role).param("name", ruser.getFirstName() + ' ' + ruser.getLastName())
              .issuedAt(new DateTime())
              .expiration(new DateTime().plusSeconds(EXPIRY_IN_SECONDS))
              .build()).build();
      signedToken = signer.sign(access_token);
    }
    if (user instanceof Admin) {
      Admin ruser = (Admin) user;
      rdto.put("id", ruser.getId());
      rdto.put("firstName", ruser.getFirstName());
      rdto.put("lastName", ruser.getLastName());
      rdto.put("email", ruser.getEmail());

      final HmacSHA512Signer signer = new HmacSHA512Signer(tokenSecret);
      final JsonWebToken access_token = JsonWebToken
          .builder()
          .header(JsonWebTokenHeader.HS512())
          .claim(JsonWebTokenClaim
              .builder()
              .param("id", id).param("role", role).param("name", ruser.getFirstName() + ' ' + ruser.getLastName())
              .issuedAt(new DateTime())
              .expiration(new DateTime().plusSeconds(EXPIRY_IN_SECONDS))
              .build()).build();
      signedToken = signer.sign(access_token);
    }
    if (user instanceof Customer) {
      Customer ruser = (Customer) user;
      rdto.put("id", ruser.getId());
      rdto.put("firstName", ruser.getFirstName());
      rdto.put("lastName", ruser.getLastName());
      rdto.put("email", ruser.getEmail());
      rdto.put("phoneNumber", ruser.getPhoneNumber());

      final HmacSHA512Signer signer = new HmacSHA512Signer(tokenSecret);
      final JsonWebToken access_token = JsonWebToken
          .builder()
          .header(JsonWebTokenHeader.HS512())
          .claim(JsonWebTokenClaim
              .builder()
              .param("id", id).param("role", role).param("name", ruser.getFirstName() + ' ' + ruser.getLastName())
              .issuedAt(new DateTime())
              .expiration(new DateTime().plusSeconds(EXPIRY_IN_SECONDS))
              .build()).build();
      signedToken = signer.sign(access_token);
    }


    Map<String, Object> data = new HashMap<String, Object>();
    data.put("access_token", signedToken);

    //	System.out.println(signedToken);
    //	data.put("expire_in_seconds", expire_in_seconds);

    data.put("user", rdto);
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
    }/*else {
			data.put("restaurant", null);
		}*/
    System.out.println(data);
    return data;
  }


}
