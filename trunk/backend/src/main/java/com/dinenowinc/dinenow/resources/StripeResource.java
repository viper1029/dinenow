package com.dinenowinc.dinenow.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.PaymentMethod;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.google.inject.Inject;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.DeletedPlan;
//import com.stripe.model.PaymentSource;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


@Path("/stripe")
@Api("/stripe")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class StripeResource {
	
	@Inject
	private CustomerDao customerDao;
	@Inject
	private RestaurantUserDao restaurantUserDao;
	

	
	
//	@SuppressWarnings("unchecked")
//	@POST
//	@Path("/addCard")
//	@ApiOperation(value="api add card stripe to customer", notes="<pre><code>{"
//			+ "<br/>  \"user\": \"\","
//			+ "<br/>  \"user\": \"tokenStripe\","
//			+ "<br/>  \"paymentMethod\": {"
//			+ "<br/>    \"name\": \"Visa\","
//			+ "<br/>    \"cardStripe\": \"\","
//			+ "<br/>    \"last4\": \"\""
//			+ "<br/>  }"
//			+ "<br/>}<code></pre>")
//	public Response addTokenStripe(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
//		try {
//			if (dto.containsKey("user") && dto.containsKey("paymentMethod")) {
//				Customer customer = customerDao.findOne(dto.get("user").toString());
//				String tokenStripe = dto.get("tokenStripe").toString();
//				if (customer != null) {
//					String name = null;
//					if (((HashMap<String, String>)dto.get("paymentMethod")).containsKey("name")) {
//						name = ((HashMap<String, String>)dto.get("paymentMethod")).get("name");
//					}
//					String cardStripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("cardStripe");
//					String last4Stripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("last4");
//					
//					customer.addCardStrip(new PaymentMethod(name, cardStripe, last4Stripe));
//					
//					customerDao.update(customer);
//					
//					
//					DineNowApplication.stripe.updateCustomer(customer.getCustomerStripe(), tokenStripe, cardStripe);
//					
//					return ResourceUtils.asSuccessResponse(Status.OK, customer);
//				}
//				RestaurantUser restaurantUser = restaurantUserDao.findOne(dto.get("user").toString());
//				if (restaurantUser != null) {
//					String name = null;
//					if (((HashMap<String, String>)dto.get("paymentMethod")).containsKey("name")) {
//						name = ((HashMap<String, String>)dto.get("paymentMethod")).get("name");
//					}
//					String cardStripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("cardStripe");
//					String last4Stripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("last4");
//					
//					restaurantUser.addCardStrip(new PaymentMethod(name, cardStripe, last4Stripe));
//					
//					restaurantUserDao.update(restaurantUser);
//					DineNowApplication.stripe.updateCustomer(restaurantUser.getCustomerStripe(), tokenStripe, cardStripe);
//					return ResourceUtils.asSuccessResponse(Status.OK, restaurantUser);
//				}
//				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//			}
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("input must contain key 'user' and 'paymentMethod'"));
//		} catch (Exception e) {
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
//		}
//	}
	
	

	
//	@DELETE
//	@Path("/deleteCard")
//	@ApiOperation(value="api delete card stripe to customer", notes="<pre><code>{"
//			+ "<br/>  \"user\": \"\","
//			+ "<br/>  \"paymentMethod\": {"
//			+ "<br/>    \"name\": \"Visa\","
//			+ "<br/>    \"cardStripe\": \"\","
//			+ "<br/>    \"last4\": \"\""
//			+ "<br/>  }"
//			+ "<br/>}<code></pre>")
//	public Response deleteTokenStripe(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
//		try {
//			if (dto.containsKey("user") && dto.containsKey("paymentMethod")) {
//				Customer customer = customerDao.findOne(dto.get("user").toString());
//				if (customer != null) {
//					String cardStripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("cardStripe");
//					
//					customer.deleteCardStrip(cardStripe);				
//					customerDao.update(customer);
//					DineNowApplication.stripe.deleteCard(customer.getCustomerStripe(), cardStripe);
//					
//					return ResourceUtils.asSuccessResponse(Status.OK, customer);
//				}
//				RestaurantUser restaurantUser = restaurantUserDao.findOne(dto.get("user").toString());
//				if (restaurantUser != null) {
//					String cardStripe = ((HashMap<String, String>)dto.get("paymentMethod")).get("cardStripe");
//					restaurantUser.deleteTokenStrip(cardStripe);				
//					restaurantUserDao.update(restaurantUser);
//					DineNowApplication.stripe.deleteCard(restaurantUser.getCustomerStripe(), cardStripe);
//					return ResourceUtils.asSuccessResponse(Status.OK, restaurantUser);
//				}
//				
//				return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//			}
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("input must contain key 'user' and 'paymentMethod'"));
//		} catch (Exception e) {
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
//		}
//	}
	
//	@GET
//	@Path("/{customer_id}/getCard")
//	@ApiOperation(value="api get all token stripe to customer", notes="")
//	public Response deleteTokenStripe(@ApiParam(access = "internal") @Auth User access, @PathParam("customer_id") String customer_id) {
//			Customer customer = customerDao.findOne(customer_id);
//			if (customer != null) {
//				return ResourceUtils.asSuccessResponse(Status.OK, customer.getCardStrip());
//			}
//			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//	}
	
	
	
	
	

//	@POST
//	@Path("/assign_plan/{restaurant_id}")
//	@ApiOperation(value="api assign plan for restaurant user", notes="<code><pre>{\"planStripe\":\"testIDPLAN\"}</pre></code>")
//	public Response assignPlan(@ApiParam(access = "internal") @Auth User access,@PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
//		try {
//			String plan_id = dto.get("planStripe").toString();
//			RestaurantUser restaurant_user = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
//			if (restaurant_user != null) {
//				Plan plan = DineNowApplication.stripe.getPlan(plan_id);
//				if (plan != null) {
//					restaurant_user.setPlanStripe(plan_id);
//					restaurantUserDao.update(restaurant_user);
//					return ResourceUtils.asSuccessResponse(Status.OK, plan);
//				}else {
//					return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("plan not found"));
//				}
//			}
//			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//		} catch (Exception e) {
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
//		}
//	}
	

	
	
	
//	@POST
//	@Path("/register_plan/{restaurant_id}")
//	@ApiOperation(value="api add plan to restaurant", notes="<code><pre>{"
//			+ "<br/>  \"tokenStripe\":\"\""
//			+ "<br/>}</pre></code>")
//	public Response registerPlan(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id,  HashMap<String, Object> dto){
//		try {
//			String token_id = dto.get("tokenStripe").toString();
//			
//			RestaurantUser customer = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
//			if (customer != null) {
//				
//				if (customer.getSubscriptionsStripe() == null) {
//					if (customer.getPlanStripe() != null) {
//						Subscription mSubscription = DineNowApplication.stripe.createSubScription(customer.getCustomerStripe(), customer.getPlanStripe(), token_id);
//						customer.setSubscriptionsStripe(mSubscription.getId());
//						restaurantUserDao.update(customer);
//						return ResourceUtils.asSuccessResponse(Status.OK, mSubscription);
//					}else {
//						return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not assign plan, plan contact admin to assign plan"));
//					}
//				}else {
//					Subscription mSubscription = DineNowApplication.stripe.updateSubScription(customer.getCustomerStripe(), customer.getSubscriptionsStripe(), customer.getPlanStripe());
//					customer.setSubscriptionsStripe(mSubscription.getId());
//					restaurantUserDao.update(customer);
//					return ResourceUtils.asSuccessResponse(Status.OK, mSubscription);
//				}
//			}
//			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//		} catch (Exception e) {
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
//		}
//	}
	
//	@GET
//	@Path("/plan/{restaurant_id}")
//	@ApiOperation(value="api get plan admin assign for restaurant")
//	public Response infoSubscriptions(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id){
//		try {
//			RestaurantUser customer = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
//			if (customer != null) {
//				Plan plan = DineNowApplication.stripe.getPlan(customer.getPlanStripe());
//				return ResourceUtils.asSuccessResponse(Status.OK, plan);
//			}
//			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
//		} catch (Exception e) {
//			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
//		}
//	}
	
	
	//===========================================NEW API===================================================//
	/*
	@POST
	@Path("/addCardToCustomer/{user_id}")
	@ApiOperation(value="api add new card to customer", notes="user_id can is customer_id or restaurant_user_id <br/><code><pre>{"
			+ "<br/>  \"tokenStripe\":\"\""
			+ "<br/>}</pre></code>")
	public Response addCardToCustomer(@ApiParam(access = "internal") @Auth User access, @PathParam("user_id") String user_id, HashMap<String, Object> dto){
		try {
			String token_card = dto.get("tokenStripe").toString();
			Customer customer = customerDao.findOne(user_id);
			if (customer != null) {
				Card card = DineNowApplication.stripe.addCardToCustomer(customer.getCustomerStripe(), token_card);
				return ResourceUtils.asSuccessResponse(Status.OK, card);
			}
			RestaurantUser restaurant_user = restaurantUserDao.findOne(user_id);
			if (restaurant_user != null) {
				Card card = DineNowApplication.stripe.addCardToCustomer(restaurant_user.getCustomerStripe(), token_card);
				return ResourceUtils.asSuccessResponse(Status.OK, card);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	*/
	/*
	@GET
	@Path("/getAllCard/{user_id}")
	@ApiOperation(value="api get list card for", notes="user_id can is customer_id or restaurant_user_id")
	public Response getListCard(@ApiParam(access = "internal") @Auth User access, @PathParam("user_id") String user_id){
		try {
			List<PaymentMethod> listResult = new ArrayList<PaymentMethod>();
			Customer customer = customerDao.findOne(user_id);
			if (customer != null) {
				List<PaymentSource> result = DineNowApplication.stripe.getAllCardToCustomer(customer.getCustomerStripe());
				for (PaymentSource paymentSource : result) {
					Card card = (Card)paymentSource;
					listResult.add(new PaymentMethod(card.getBrand(), card.getId(), card.getLast4()));
				}
				return ResourceUtils.asSuccessResponse(Status.OK, listResult);
			}
			RestaurantUser restaurant_user = restaurantUserDao.findOne(user_id);
			if (restaurant_user != null) {
				List<PaymentSource> result = DineNowApplication.stripe.getAllCardToCustomer(restaurant_user.getCustomerStripe());
				for (PaymentSource paymentSource : result) {
					Card card = (Card)paymentSource;
					listResult.add(new PaymentMethod(card.getBrand(), card.getId(), card.getLast4()));
				}
				return ResourceUtils.asSuccessResponse(Status.OK, listResult);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	@DELETE
	@Path("/deleteCard/{user_id}/{card_id}")
	@ApiOperation(value="api delete card", notes="user_id can is customer_id or restaurant_user_id")
	public Response deletePlan(@ApiParam(access = "internal") @Auth User access,@PathParam("user_id") String user_id, @PathParam("card_id") String card_id) {
		try {
			Customer customer = customerDao.findOne(user_id);
			if (customer != null) {
				//DineNowApplication.stripe.deleteCard(customer.getCustomerStripe(), card_id);
				return ResourceUtils.asSuccessResponse(Status.OK, null);
			}
			RestaurantUser restaurant_user = restaurantUserDao.findOne(user_id);
			if (restaurant_user != null) {
				//DineNowApplication.stripe.deleteCard(restaurant_user.getCustomerStripe(), card_id);
				return ResourceUtils.asSuccessResponse(Status.OK, null);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}	
	}
	
	
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/createPlan")
	@ApiOperation(value="api add plan stripe to customer", notes="<pre><code>{"
			+ "<br/>  \"id\": \"\","
			+ "<br/>  \"interval\": \"\","
			+ "<br/>  \"name\": \"\","
			+ "<br/>  \"currency\": \"\","
			+ "<br/>  \"trialPeriodDays\": 90,"
			+ "<br/>  \"amount\": 100"
			+ "<br/>}<code></pre>")
	public Response createPlan(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		String id = dto.get("id").toString();
		String interval = dto.get("interval").toString();
		String name = dto.get("name").toString();
		String currency = dto.get("currency").toString();
		int trial_period_days = Integer.parseInt(dto.get("trialPeriodDays").toString()); 
		int amount = Integer.parseInt(dto.get("amount").toString());
		try {
			Plan plan = DineNowApplication.stripe.createPlan(id, interval, name, currency, trial_period_days, amount);
			return ResourceUtils.asSuccessResponse(Status.OK, plan);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@PUT
	@Path("/updatePlan/{plan_id}")
	@ApiOperation(value="api update plan stripe to customer", notes="<pre><code>{"	
			+ "<br/>  \"name\": \"\""
			+ "<br/>}<code></pre>")
	public Response updatePlan(@ApiParam(access = "internal") @Auth User access,@PathParam("plan_id") String plan_id, HashMap<String, Object> dto) {
		String name = dto.get("name").toString();
		try {
			Plan plan = DineNowApplication.stripe.updatePlan(plan_id, name);
			return ResourceUtils.asSuccessResponse(Status.OK, plan);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@DELETE
	@Path("/deletePlan/{plan_id}")
	@ApiOperation(value="api delete plan")
	public Response deletePlan(@ApiParam(access = "internal") @Auth User access,@PathParam("plan_id") String plan_id) {
		try {
			DeletedPlan deleteplan = DineNowApplication.stripe.deletePlan(plan_id);
			return ResourceUtils.asSuccessResponse(Status.OK, deleteplan);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	@GET
	@Path("/getListPlan")
	@ApiOperation(value="api get list plan")
	public Response getListPlan(@ApiParam(access = "internal") @Auth User access){
		try {
			List<Plan> listResult = DineNowApplication.stripe.getListPlan();
			return ResourceUtils.asSuccessResponse(Status.OK, listResult);
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	
	
	@POST
	@Path("/assignPlan/{restaurant_id}")
	@ApiOperation(value="api assign Plan for restaurant", notes="<code><pre>{"
			+ "<br/>  \"planStripe\":\"\""
			+ "<br/>}</pre></code>")
	public Response assignPlan(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
		try {
			String plan_id = dto.get("planStripe").toString();
			RestaurantUser restaurant_user = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
			if (restaurant_user != null) {
				Plan plan = DineNowApplication.stripe.getPlan(plan_id);
				if (plan != null) {
					restaurant_user.setPlanStripe(plan_id);
					restaurantUserDao.update(restaurant_user);
					return ResourceUtils.asSuccessResponse(Status.OK, plan);
				}else {
					return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("plan not found"));
				}
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	//Register Plan
	@POST
	@Path("/registerPlan/{restaurant_id}")
	@ApiOperation(value="api register Plan for restaurant", notes="<code><pre>{"
			+ "<br/>  \"tokenStripe\":\"\""
			+ "<br/>}</pre></code>")
	public Response registerPlan(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, HashMap<String, Object> dto){
		try {
			String token_id = dto.get("tokenStripe").toString();
			RestaurantUser restaurant_user = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
			if (restaurant_user != null) {
				Subscription subscription = DineNowApplication.stripe.createSubScription(restaurant_user.getCustomerStripe(), restaurant_user.getPlanStripe(), token_id);
				return ResourceUtils.asSuccessResponse(Status.OK, subscription);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	
	
	@GET
	@Path("/plan/{restaurant_id}")
	@ApiOperation(value="api get plan admin assign for restaurant")
	public Response infoSubscriptions(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id){
		try {
			RestaurantUser customer = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
			if (customer != null) {
				Plan plan = DineNowApplication.stripe.getPlan(customer.getPlanStripe());
				return ResourceUtils.asSuccessResponse(Status.OK, plan);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	
	
	//Get Plan current off restaurant
	
	
	@GET
	@Path("/getSubscriptions/{restaurant_id}")
	@ApiOperation(value="api get subcription")
	public Response getSubscription(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id){
		try {
			RestaurantUser restaurant_user = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
			if (restaurant_user != null) {
				List<Subscription> listResult = DineNowApplication.stripe.getSubscription(restaurant_user.getCustomerStripe());
				return ResourceUtils.asSuccessResponse(Status.OK, listResult);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	@DELETE
	@Path("/cancelSubcription/{restaurant_id}/{subcription_id}")
	@ApiOperation(value="api cancel subcription")
	public Response cancelSubcription(@ApiParam(access = "internal") @Auth User access, @PathParam("restaurant_id") String restaurant_id, @PathParam("subcription_id") String subcription_id){
		try {
			RestaurantUser restaurant_user = restaurantUserDao.getRestaurantUserByRestaurantId(restaurant_id);
			if (restaurant_user != null) {
				DineNowApplication.stripe.cancelSubcription(restaurant_user.getCustomerStripe(), subcription_id);
				restaurant_user.setPlanStripe(null);
				restaurantUserDao.update(restaurant_user);
				return ResourceUtils.asSuccessResponse(Status.OK, null);
			}
			return ResourceUtils.asFailedResponse(Status.NOT_FOUND, new ServiceErrorMessage("user not found"));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage(e.getMessage()));
		}
	}
	
	
	
	
	
	
	//=========================================================================================//
}
