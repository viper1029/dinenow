package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.Constants;
import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.auth.TokenGenerator;
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
import com.dinenowinc.dinenow.model.helpers.UserRole;
import com.dinenowinc.dinenow.service.AdminService;
import com.dinenowinc.dinenow.service.CustomerService;
import com.dinenowinc.dinenow.service.RestaurantUserService;
import com.dinenowinc.dinenow.utils.JavaMail;
import com.dinenowinc.dinenow.utils.MD5Hash;
import com.dinenowinc.dinenow.utils.Utils;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.inject.Inject;
import com.twilio.sdk.TwilioRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
  private RestaurantUserDao restaurantUserDao;

  @Inject
  private RestaurantUserService restaurantUserService;

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
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: could not decode authentication string."));
    }

    try {
      Matcher matcher = CREDENTIALS_PATTERN.matcher(usernameAndPassword);
      if (!matcher.matches()) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email or Password cannot be empty."));
      }

      final String email = matcher.group(1);
      String password = matcher.group(2);

      if (email == null || email.length() == 0) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email cannot be empty"));
      }
      else if (!email.matches(EMAIL_PATTERN)) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Email is not in correct format."));
      }

      else if (password == null || password.length() == 0) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Password cannot be empty"));
      }
      password = MD5Hash.md5Spring(password);


      RestaurantUser restaurantUser = restaurantUserService.checkCredentials(email, password);
      if (restaurantUser == null) {
        Admin admin = adminService.checkCredentials(email, password);
        if (admin == null) {
          Customer customer = customerService.checkCredentials(email, password);
          if (customer == null) {
            return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Invalid username or password."));
          }
          else {
            Map<String, Object> data = TokenGenerator.generateToken(customer.getId(), UserRole.CUSTOMER, customer, null);
            return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
          }
        }
        else {
          Map<String, Object> data = TokenGenerator.generateToken(admin.getId(), UserRole.ADMIN, admin, null);
          return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
        }
      }
      Restaurant restaurant = restaurantDao.findByRestaurantUser(restaurantUser.getId());
      Map<String, Object> data = TokenGenerator.generateToken(restaurantUser.getId(), restaurantUser.getRole(), restaurantUser, restaurant);
      return ResourceUtils.asSuccessResponse(Response.Status.OK, data);
    }
    catch (Exception e) {
      LOGGER.debug("Error attempting to login.", e);
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }

  @POST
  @ApiOperation(value = "forgot password", notes = "")
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
  @Path("/forgot")
  public Response forget(HashMap<String, Object> inputMap) {
    try {
      if (inputMap.get("email") == null && inputMap.get("phone") == null) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Reset method not specified."));
      }

      if (StringUtils.isNotBlank((String) inputMap.get("phone"))) {
        String phone = inputMap.get("phone").toString();
        String validationCode = RandomStringUtils.random(Constants.VALIDATION_CODE_LENGTH, 0, 0, false, true, null, new SecureRandom());
        Customer customer = customerDao.getCustomerByPhoneNumber(phone);

        if(customer != null) {
          Utils.sendSMSCode("Reset code: " + validationCode, phone);
          customer.setResetKey(validationCode);
          Date timeReset = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
          customer.setResetKeyTime(timeReset);
          customerDao.update(customer);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Reset code sent to phone."));
        }
        else  {
          RestaurantUser restaurantUser = restaurantUserDao.getRestaurantUserByPhoneNumber(phone);
          if(restaurantUser == null) {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("This phone number is not registered."));
          }
          Utils.sendSMSCode("Reset code: " + validationCode, phone);
          restaurantUser.setResetKey(validationCode);
          restaurantUserDao.update(restaurantUser);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Reset code sent to phone."));
        }
      }
      else if (StringUtils.isNotBlank((String) inputMap.get("email"))) {
        String email = inputMap.get("email").toString();
        String validationCode = RandomStringUtils.random(Constants.VALIDATION_CODE_LENGTH, 0, 0, false, true, null, new SecureRandom());
        if(!email.matches(EMAIL_PATTERN)) {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("The email is not in correct format."));
        }

        Customer customer = customerService.getUserByEmail(email);
        if (customer != null) {
          JavaMail javaMail = new JavaMail(email, "Your reset code: ", validationCode);
          customer.setResetKey(validationCode);
          Date timeReset = DateUtils.addHours(new Date(), DineNowApplication.timeResetKey);
          customer.setResetKeyTime(timeReset);
          customerDao.update(customer);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Please check your email for instruction on how to rest your password."));
        }
        else {
          RestaurantUser restaurantUser = restaurantUserDao.getRestaurantUserByEmail(email);
          if (restaurantUser == null) {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("This email is not registered."));
          }
          JavaMail javaMail = new JavaMail(email, "Your reset code: ", validationCode);
          restaurantUser.setResetKey(validationCode);
          restaurantUserDao.update(restaurantUser);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Please check your email for instruction on how to rest your password."));
        }
      }
    }
    catch (MessagingException m) {
      m.printStackTrace();
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: Internet connection failed "));
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
    return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Encountered error resetting password."));
  }


  @POST
  @ApiOperation(value = "reset password")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "succeeded"),
      @ApiResponse(code = 410, message = "resetKey expired!!!!!"),
      @ApiResponse(code = 400, message = "new password empty or too short"),
      @ApiResponse(code = 400, message = "new password empty or too short"),
      @ApiResponse(code = 400, message = "not found customer"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/forget/confirm")
  public Response confirm(HashMap<String, Object> paramMap) {

    if (paramMap.get("password") == null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Password not provided."));
    }
    if (paramMap.get("resetKey") == null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Reset key not provided."));
    }
    if (paramMap.get("id") == null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Customer id not provided."));
    }

    try {
      String newPassword = paramMap.get("password").toString();
      String resetKey = paramMap.get("resetKey").toString();
      String id = paramMap.get("id").toString();

      if (StringUtils.isBlank(newPassword) || newPassword.length() >= Constants.MINIMUM_PASSWORD_LENGTH) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Password is too short."));
      }

      Customer customer = customerDao.getCustomerByID(id);
      if (customer != null) {
        if (customer.getResetKeyTime().getTime() <= new Date().getTime()) {
          customer.setResetKey(null);
          customer.setResetKeyTime(null);
          customerDao.update(customer);
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("The reset key has expired."));
        }

        if(customer.getResetKey().equalsIgnoreCase(resetKey)) {
          customer.setPassword(MD5Hash.md5Spring(newPassword));
          customer.setResetKey(null);
          customer.setResetKeyTime(null);
          customerDao.update(customer);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Password updated."));
        }
        else {
          return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Reset code is not valid."));
        }
      }
      else {
        RestaurantUser restaurantUser = restaurantUserDao.getRestaurantUserById(id);
        if (restaurantUser == null) {
          return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Could not find user with the requested id."));
        }
        if(restaurantUser.getResetKey().equalsIgnoreCase(resetKey)) { //TODO: Check the time of reset code
          restaurantUser.setPassword(MD5Hash.md5Spring(newPassword));
          restaurantUser.setResetKey(null);
          restaurantUserDao.update(restaurantUser);
          return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Password updated."));
        }
        else {
            return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Reset code is not valid."));
        }
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }

  @POST
  @ApiOperation(value = "create new customer")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 400, message = "list error"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 400, message = "Exception: ###")
  })
  @Path("/register")
  public Response registerCustomer(HashMap<String, Object> inputMap) {
    try {
      Customer customer = new Customer();
      customer.setFirstName(inputMap.get("firstName").toString());
      customer.setLastName(inputMap.get("lastName").toString());
      customer.setEmail(inputMap.get("email").toString());
      customer.setPassword(MD5Hash.md5Spring(inputMap.get("password").toString()));
      customer.setPhoneNumber(inputMap.get("phoneNumber").toString());
      customer.setCreatedBy(customer.getFirstName());
      customer.setCreatedDate(new Date());

      String validationCode = RandomStringUtils.random(Constants.VALIDATION_CODE_LENGTH, 0, 0, false, true, null, new SecureRandom());
      ServiceResult<Customer> serviceResult = customerService.validateCustomer(customer);
      if (serviceResult.hasErrors()) {
        return ResourceUtils.asFailedResponse(Response.Status.CONFLICT, serviceResult.getErrors());
      }
      customerDao.save(customer);

      try {
        Utils.sendSMSCode(Constants.SMS_VALIDATION_MESSAGE + validationCode, customer.getPhoneNumber());
        Customer newCustomer = customerDao.getCustomerByEmail(customer.getEmail());
        newCustomer.setValidationCode(validationCode);
        newCustomer.setValidationCodeTime(DateUtils.addHours(new Date(), DineNowApplication.timeResetKey));
        newCustomer.setCustomerStripe(DineNowApplication.stripe.createCustomer(customer));
        customerDao.update(newCustomer);
      }
      catch (TwilioRestException e) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Error sending SMS to " + customer.getPhoneNumber().substring(1)));
      }
      Map<String, Object> token = TokenGenerator.generateToken(serviceResult.getResult().getId(), UserRole.CUSTOMER, serviceResult.getResult(), null);
      return ResourceUtils.asSuccessResponse(Status.OK, token);
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.INTERNAL_SERVER_ERROR, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "validate phone number")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "validation phone..."),
      @ApiResponse(code = 410, message = "validationCode expired!!!!!"),
      @ApiResponse(code = 400, message = "incorrect validation code"),
      @ApiResponse(code = 400, message = "Exception: ###"),
      @ApiResponse(code = 404, message = "not found customer"),
  })
  @Path("/validation")
  public Response validatePhoneNumber(HashMap<String, Object> inputMap) {

    if (inputMap.get("id") == null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("No customer ID provided."));
    }
    if (inputMap.get("validationCode") == null) {
      return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("No validation code provided."));
    }

    try {
      String customerId = inputMap.get("id").toString();
      String validationCode = inputMap.get("validationCode").toString();
      Customer customer = customerDao.get(customerId);

      if (customer == null) {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Customer does not exist."));
      }

      if (customer.getValidationCode() != null && customer.getValidationCodeTime().getTime() <= new Date().getTime()) {
        customer.setValidationCode(null);
        customer.setValidationCodeTime(null);
        customerDao.update(customer);
        return ResourceUtils.asFailedResponse(Status.GONE, new ServiceErrorMessage("Validation code has expired."));
      }

      if (customer.getValidationCode() != null && validationCode.equalsIgnoreCase(customer.getValidationCode())) {
        customer.setValidationCode(null);
        customer.setValidationCodeTime(null);
        customer.setIsPhoneNumberValid(true);

        if (!customer.isPhonePoint()) {
          customer.setPoint(customer.getPoint() + Constants.POINTS_FOR_PHONE_NUMBER_CONFIRMATION);
          customer.setPhonePoint(true);
        }
        customerDao.update(customer);
        return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Phone number has been verified."));
      }
      else if (customer.getIsPhoneNumberValid()) {
        return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("This phone number has already been verified."));
      }
      else {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Incorrect validation code."));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
    }
  }


  @POST
  @ApiOperation(value = "resent validation code")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "validation code send to phone"),
      @ApiResponse(code = 400, message = "cannot send SMS"),
      @ApiResponse(code = 400, message = "Exception: ###"),
      @ApiResponse(code = 404, message = "not found customer")
  })
  @Path("/resend")
  public Response resendValidationCode(HashMap<String, Object> inputMap) {
    try {
      if (inputMap.get("id") == null) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("No customer ID provided."));
      }

      Customer customer = customerDao.get(inputMap.get("id").toString());
      if (customer == null) {
        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("Customer does not exist."));
      }

      try {
        String validationCode = RandomStringUtils.random(Constants.VALIDATION_CODE_LENGTH, 0, 0, false, true, null, new SecureRandom());
        Utils.sendSMSCode(Constants.SMS_VALIDATION_MESSAGE + validationCode, customer.getPhoneNumber());
        customer.setValidationCode(validationCode);
        customer.setValidationCodeTime(DateUtils.addHours(new Date(), DineNowApplication.timeValidationCode));
        customerDao.update(customer);
        return ResourceUtils.asSuccessResponse(Status.OK, new ServiceErrorMessage("Validation code sent."));
      }
      catch (TwilioRestException e) {
        return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Error sending SMS to " + customer.getPhoneNumber().substring(1)));
      }
    }
    catch (Exception e) {
      return ResourceUtils.asFailedResponse(Response.Status.BAD_REQUEST, new ServiceErrorMessage("Exception: " + e.getMessage()));
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
      Customer customer = customerDao.get(id);
      if (customer != null) {

        HashMap<String, Object> dto = new HashMap<>();
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
            String token = RandomStringUtils.random(Constants.VALIDATION_CODE_LENGTH, 0, 0, false, true, null, new SecureRandom());
            if (Utils.sendSMSCode(Constants.SMS_VALIDATION_MESSAGE + token, phoneNumber)) {
              customer.setIsPhoneNumberValid(false);
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
}
