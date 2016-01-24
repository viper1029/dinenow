package com.dinenowinc.dinenow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.SocialAccounts;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.DeletedCard;
import com.stripe.model.DeletedPlan;
//import com.stripe.model.PaymentSource;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.stripe.model.Subscription;
import com.stripe.model.Token;

public class StripeApplication {
	
	//public static final String SECREC_KEY_STRIPE = "sk_test_SYB7bZyCsLeC8BQL54QylBbd"; //Client Test
	
	public static final String SECREC_KEY_STRIPE = "sk_test_xoxQuwIkxhIXgVLNOJ8E5Kpl";

	
	public DeletedPlan deletePlan(String id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Plan plan = Plan.retrieve(id);
		return plan.delete();
	}
	
	
	public Plan createPlan(String id, String interval, String name, String currency, int trial_period_days, int amount) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Map<String, Object> planGold = new HashMap<String, Object>();
		planGold.put("amount", amount);
		planGold.put("interval", interval);
		planGold.put("name", name);
		planGold.put("currency", currency);
		planGold.put("id", id);
		planGold.put("trial_period_days", trial_period_days);
		Stripe.apiKey = SECREC_KEY_STRIPE;
		return Plan.create(planGold);
	}
	
	
	public Plan updatePlan(String id, String name) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Plan plan = Plan.retrieve(id);
		Map<String, Object> planGold = new HashMap<String, Object>();
		planGold.put("name", name);
		return plan.update(planGold);
	}
	
	public List<Plan> getListPlan() throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("limit", 50);
		return Plan.all(planParams).getData();
	}
	
	
	
	
	
	public void createPlan() {
		
		Map<String, Object> planGold = new HashMap<String, Object>();
		planGold.put("amount", 100);
		planGold.put("interval", "month");
		planGold.put("name", "Gold Plan 100$/month");
		planGold.put("currency", "usd");
		planGold.put("id", "gold");
		planGold.put("trial_period_days", 90);
		
		Map<String, Object> planGoldDashboard = new HashMap<String, Object>();
		planGoldDashboard.put("amount", 120);
		planGoldDashboard.put("interval", "month");
		planGoldDashboard.put("name", "Gold Dashboard Plan 120$/month (month for access to management dashboard)");
		planGoldDashboard.put("currency", "usd");
		planGoldDashboard.put("id", "gold_dashboard");
		
		Map<String, Object> planGoldAnalytics  = new HashMap<String, Object>();
		planGoldAnalytics.put("amount", 120);
		planGoldAnalytics.put("interval", "month");
		planGoldAnalytics.put("name", "Gold Analytics Plan 120$/month (month for analytics functionality)");
		planGoldAnalytics.put("currency", "usd");
		planGoldAnalytics.put("id", "gold_analytics");
		
		Map<String, Object> planGoldDashboardAnalytics  = new HashMap<String, Object>();
		planGoldDashboardAnalytics.put("amount", 140);
		planGoldDashboardAnalytics.put("interval", "month");
		planGoldDashboardAnalytics.put("name", "Gold Analytics Plan 140$/month (management dashboard/analytics functionality)");
		planGoldDashboardAnalytics.put("currency", "usd");
		planGoldDashboardAnalytics.put("id", "gold_dashboard_analytics");

		try {
			Stripe.apiKey = SECREC_KEY_STRIPE;
			Plan.create(planGold);
			Plan.create(planGoldDashboard);
			Plan.create(planGoldAnalytics);
			Plan.create(planGoldDashboardAnalytics);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}

		
	}

	/**
	 *  Create SubScription
	 * @param customer_id
	 * @param plan_id gold/gold_dashboard/gold_analytics/gold_dashboard_analytics
	 * @throws APIException 
	 * @throws CardException 
	 * @throws APIConnectionException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticationException 
	 */
	public Subscription createSubScription(String customer_id, String plan_id, String token_cart) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = SECREC_KEY_STRIPE;
			Customer cu = Customer.retrieve(customer_id);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("plan", plan_id);
			params.put("source", token_cart);
			return cu.createSubscription(params);
	}
	
	/**
	 * Update SubScription
	 * @param customer_id
	 * @param subscription_id
	 * @param new_plan
	 * @throws APIException 
	 * @throws CardException 
	 * @throws APIConnectionException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticationException 
	 */
	public Subscription updateSubScription(String customer_id, String subscription_id, String new_plan) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
			Stripe.apiKey = SECREC_KEY_STRIPE;
			Customer cu = Customer.retrieve(customer_id);
			Subscription subscription = cu.getSubscriptions().retrieve(subscription_id);
			Map<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("plan", new_plan);
			return subscription.update(updateParams);
	}
	
	
	public List<Subscription> getSubscription(String customer_id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Customer cu = Customer.retrieve(customer_id);
		Map<String, Object> subscriptionParams = new HashMap<String, Object>();
		subscriptionParams.put("limit", 50);
		List<Subscription> subscriptions = cu.getSubscriptions().all(subscriptionParams).getData();
		return subscriptions;
	}
	
	public void cancelSubcription(String customer_id, String subcription_id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		
		Customer cu = Customer.retrieve(customer_id);
		for(Subscription subscription : cu.getSubscriptions().getData()){
		  if(subscription.getId().equals(subcription_id)){
		    subscription.cancel(null);
		    break;
		  }
		}
	}
	
	
	

	public Plan getPlan(String plan_id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Plan plan =  Plan.retrieve(plan_id);
		return plan;
	}
	
	
	
	
	
	public String createRestaurantUser(RestaurantUser user) {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		try {
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", String.format("Restaurant user for %s", user.getEmail()));
			customerParams.put("email", user.getEmail());
			//customerParams.put("source", "tok_15r25qFvJtkXeNBHhA3Fzdif"); // obtained with Stripe.js
			HashMap<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("isRestaurantUser", true);
			metadata.put("id", user.getId());
			customerParams.put("metadata", metadata);
			
			return Customer.create(customerParams).getId();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String createCustomer(com.dinenowinc.dinenow.model.Customer user) {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		try {
			Map<String, Object> customerParams = new HashMap<String, Object>();
			String info_customer = "";
			for (SocialAccounts account : user.getSocialAccounts()) {
				info_customer += account.getUserName() + "/";
			}
			customerParams.put("description", String.format("Customer user for %s", user.getEmail() != null ? user.getEmail() : info_customer));
			customerParams.put("email", user.getEmail());
			//customerParams.put("source", "tok_15r25qFvJtkXeNBHhA3Fzdif"); // obtained with Stripe.js
			HashMap<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("isRestaurantUser", false);
			metadata.put("id", user.getId());
			customerParams.put("metadata", metadata);
			return Customer.create(customerParams).getId();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateCustomer(String id_customer, String token, String id_token_card) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Customer cu = Customer.retrieve(id_customer);
		Map<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.put("source", token);
		cu.update(updateParams);
		
	}

	/*
	public void deleteCard(String ic_customer, String card_id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Customer cu = Customer.retrieve(ic_customer);
		for(PaymentSource source : cu.getSources().getData()){
		  if(source.getId().equals(card_id)){
		    source.delete();
		    break;
		  }
		}
	}
	
	*/
	
	public void charge(double amount, String currency, String card_id, String customer_id, String description) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		double total_amount = amount*100;
		chargeParams.put("amount", (int)total_amount);
		chargeParams.put("currency", currency);
		//Token token = Token.retrieve(tokenCard);
		chargeParams.put("card", card_id); // obtained with Stripe.js
		chargeParams.put("customer", customer_id);
		chargeParams.put("description", description);
		
		Charge.create(chargeParams);
	}
	
	
	/*
	
	public Card addCardToCustomer(String customer_id, String token_card) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;
		Customer cu = Customer.retrieve(customer_id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", token_card);
		return cu.createCard(params);
	}
	
	public List<PaymentSource> getAllCardToCustomer(String customer_id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
		Stripe.apiKey = SECREC_KEY_STRIPE;

		Customer cu = Customer.retrieve(customer_id);
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("limit", 50);
		cardParams.put("object", "card");
		return cu.getSources().all(cardParams).getData();
	}
	
	*/
	
	
	
	
	
}
