package com.dinenowinc.dinenow;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.joda.time.Duration;

import com.dinenowinc.dinenow.db.DatabaseFiller;
import com.dinenowinc.dinenow.model.AccessToken;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.model.Version;
import com.dinenowinc.dinenow.resources.AddOnResource;
import com.dinenowinc.dinenow.resources.AddressBookResource;
import com.dinenowinc.dinenow.resources.CartResources;
import com.dinenowinc.dinenow.resources.CategoryResource;
import com.dinenowinc.dinenow.resources.CouponResource;
import com.dinenowinc.dinenow.resources.CustomerOrderResource;
import com.dinenowinc.dinenow.resources.CustomerResource;
import com.dinenowinc.dinenow.resources.DeliveryZoneResource;
import com.dinenowinc.dinenow.resources.ImageResource;
import com.dinenowinc.dinenow.resources.ItemResource;
import com.dinenowinc.dinenow.resources.MenuResource;
import com.dinenowinc.dinenow.resources.ModifierResource;
import com.dinenowinc.dinenow.resources.OrderDetailsResource;
import com.dinenowinc.dinenow.resources.PaymentTypeResource;
import com.dinenowinc.dinenow.resources.RestaurantResource;
import com.dinenowinc.dinenow.resources.RestaurantUserResource;
import com.dinenowinc.dinenow.resources.ReviewResource;
import com.dinenowinc.dinenow.resources.RoleResource;
import com.dinenowinc.dinenow.resources.SecuredResource;
import com.dinenowinc.dinenow.resources.SizeResource;
import com.dinenowinc.dinenow.resources.StripeResource;
import com.dinenowinc.dinenow.resources.SubMenuResource;
import com.dinenowinc.dinenow.resources.TestResource;
import com.github.toastshaman.dropwizard.auth.jwt.JWTAuthProvider;
import com.github.toastshaman.dropwizard.auth.jwt.JsonWebTokenParser;
import com.github.toastshaman.dropwizard.auth.jwt.JsonWebTokenValidator;
import com.github.toastshaman.dropwizard.auth.jwt.exceptions.TokenExpiredException;
import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Verifier;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.parser.DefaultJsonWebTokenParser;
import com.github.toastshaman.dropwizard.auth.jwt.validator.ExpiryValidator;
import com.google.common.base.Optional;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.SwaggerConfig;

/**
 * Hello world!
 *
 */
public class DineNowApplication extends Application<ApplicationConfiguration> {

	public static Integer timeResetKey = 12;
	public static Integer timeValidationCode = 12;
	
	public static Boolean isInitDb = false;
	
	
	public static final String JPA_UNIT =
	// "data";
	"dataMysql";

	public static byte[] authKey;

	private static final JpaPersistModule jpaPersistModule = new JpaPersistModule(JPA_UNIT);
	
	private GuiceBundle<ApplicationConfiguration> guiceBundle;
	PersistService persistService = null;
	
	@Override
	public String getName() {
		return "Dine Now Application Server";
	}

	public static StripeApplication stripe;
	public static void main(String[] args) throws Exception {
		stripe = new StripeApplication();
		//stripe.createPlan();
		
		new DineNowApplication().run(args);
	}

	private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();
	@Override
	public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
		guiceBundle = GuiceBundle.<ApplicationConfiguration> newBuilder()
				.addModule(new DineNowGuiceModule())
				.addModule(jpaPersistModule).enableAutoConfig("com.dinenowinc")
				.setConfigClass(ApplicationConfiguration.class).build();
		
		bootstrap.addBundle(guiceBundle);
		initAuthKey();
		swaggerDropwizard.onInitialize(bootstrap);
	}

	@SuppressWarnings("unused")
	@Override
	public void run(ApplicationConfiguration configuration,Environment environment) throws Exception {
		
		((HttpConnectorFactory) ((DefaultServerFactory) configuration.getServerFactory()).getApplicationConnectors().get(0)).setPort(Integer.parseInt(configuration.getPort()));
		((HttpConnectorFactory) ((DefaultServerFactory) configuration.getServerFactory()).getAdminConnectors().get(0)).setPort(Integer.parseInt(configuration.getPort()) + 1);
		if (configuration.getTimeResetKey() != 0) {
			timeResetKey = configuration.getTimeResetKey();
			timeValidationCode = configuration.getTimeResetKey();
		}
		
		if (configuration.getIsInitDb()) {
			DatabaseFiller.getInstance().initData();
		}
		
		
		
		environment
				.servlets()
				.addFilter(
						"persistFilter",
						guiceBundle.getInjector().getInstance(
								PersistFilter.class))
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),
						true, "/*");
		

		Dynamic filter = environment.servlets().addFilter("CORS",CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_MAX_AGE_HEADER, "3600");
		filter.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM,"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		//filter.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM,"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		//filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
		filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM,"true");
		
		

		//environment.healthChecks().register("Ping health check", new PingHealthCheck(new PingResource()));
		//environment.healthChecks().register("Ping health check", new PingHealthCheck());
		environment.jersey().register(guiceBundle.getInjector().getInstance(OrderDetailsResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(CustomerOrderResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(CartResources.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(ModifierResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(SizeResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(AddOnResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(DeliveryZoneResource.class ));
		environment.jersey().register(guiceBundle.getInjector().getInstance(RestaurantUserResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(CustomerResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(RestaurantResource.class));		
		environment.jersey().register(guiceBundle.getInjector().getInstance(ItemResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(MenuResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(CategoryResource.class));	
		environment.jersey().register(guiceBundle.getInjector().getInstance(SubMenuResource.class));
		environment.jersey().register(createAuthProvider());
		environment.jersey().register(guiceBundle.getInjector().getInstance(SecuredResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(AddressBookResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(StripeResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(CouponResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(RoleResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(PaymentTypeResource.class));
		environment.jersey().register(guiceBundle.getInjector().getInstance(ReviewResource.class));
		
		environment.jersey().register(new ImageResource());
		environment.jersey().register(TestResource.class);
		
		 Version version = DatabaseFiller.getInstance().getLatestVersion();
		 if(version == null)
			 environment.jersey().setUrlPattern("/api/*");
		 else
			 environment.jersey().setUrlPattern("/api/" + version.getVersionName() + "/*");
		
		
		swaggerDropwizard.onRun(configuration, environment, configuration.getHost());
		
		FilterFactory.setFilter(new InternalSwaggerFilter());
		
		
		SwaggerConfig config = ConfigFactory.config();
	    config.setApiVersion("1.0");
	}

	private void initAuthKey() {
		//Random random = new SecureRandom();
		DineNowApplication.authKey = new byte[64];

		//random.nextBytes(DineNowApplication.authKey);
		System.out.println("%$%$%KEY_SECURITY$%$%".getBytes());
		DineNowApplication.authKey = "%$%$%KEY_SECURITY$%$%".getBytes();
		System.out.println("AuthKey initialized!");
	}

	private JWTAuthProvider<AccessToken> createAuthProvider() {
		JsonWebTokenParser tokenParser = new DefaultJsonWebTokenParser();
		final HmacSHA512Verifier tokenVerifier = new HmacSHA512Verifier(
				DineNowApplication.authKey);
		final JsonWebTokenValidator expiryValidator = new ExpiryValidator(Duration.standardSeconds(1));
		JWTAuthProvider<AccessToken> authProvider = new JWTAuthProvider<AccessToken>(new Authenticator<JsonWebToken, AccessToken>() {

			public Optional<AccessToken> authenticate(JsonWebToken credentials) throws AuthenticationException {
				try {
					expiryValidator.validate(credentials);
				} catch (TokenExpiredException e) {
					HashMap<String, Object> entity = new HashMap<String, Object>();
	        		entity.put("errorMessage", "token expired");
	        		entity.put("data", null);
					throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(entity).type(MediaType.APPLICATION_JSON).build());
				}
				Object user_id = credentials.claim().getParameter("id");
				Object role = credentials.claim().getParameter("role");
				Object name =credentials.claim().getParameter("name");
				
				if (user_id != null && role != null) {
					System.out.println("::::::::::<<<<<<<<<<<<<<<"+user_id);
					AccessToken userAccess = new AccessToken();
					userAccess.setId(UUID.fromString(user_id.toString()));
					userAccess.setRole(UserRole.valueOf(role.toString()));
					userAccess.setName(name.toString());
					return Optional.of(userAccess);
				}
				return Optional.absent();
			}
		},tokenParser, tokenVerifier, "realm");
		return authProvider;
	}

	public static JpaPersistModule createJpaModule() {
		return jpaPersistModule;
	}

}

