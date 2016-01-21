package com.dinenowinc.dinenow.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dinenowinc.dinenow.DineNowApplication;
import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.VersionDao;
import com.dinenowinc.dinenow.error.ServiceResult;
import com.dinenowinc.dinenow.model.Admin;
import com.dinenowinc.dinenow.model.AvailabilityStatus;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.CategoryInfo;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.DeliveryZoneType;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.ItemInfo;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.NetworkStatus;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.SubMenu;
import com.dinenowinc.dinenow.model.Tax;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.model.Version;
import com.dinenowinc.dinenow.service.AdminService;
import com.dinenowinc.dinenow.service.CustomerService;
import com.dinenowinc.dinenow.service.RestaurantService;
import com.dinenowinc.dinenow.service.RestaurantUserService;
import com.dinenowinc.dinenow.utils.MD5Hash;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Class to fill the database with some sample entities.
 */
public class DatabaseFiller {

	Logger logger = Logger.getLogger(DatabaseFiller.class);
	
	static DatabaseFiller instance = null;
	
	static Injector injector;
	
	String email;
	String password;
	String firstName;
	String lastName;
	String phone;
	
	
	@Transactional
	public void initData() {
	//	addRestaurantUserMenu();
	//	addCustomer();
		addVersion();
	//	addRestaurantAndDeliveryZone();
		addAdmin();
	}


	public void addAdmin() {
		// TODO Auto-generated method stub
		System.out.println("admin data entering");
		AdminService adminService = injector.getInstance(AdminService.class);

		Admin admin = new Admin();
		admin.setFirstName("Admin");
		admin.setLastName("Admin"); 
		admin.setEmail("admin@admin.com");
		admin.setPassword(MD5Hash.md5Spring("admin@123"));
		admin.setCreatedBy("admin");
		admin.setCreatedDate(new Date());
		
		adminService.createAdmin(admin);
		
		
	}


	public static DatabaseFiller getInstance() {

		if (instance==null) {			
			JpaPersistModule jpaPersistModule = DineNowApplication.createJpaModule();
			injector = Guice.createInjector(jpaPersistModule);
			instance = injector.getInstance(DatabaseFiller.class);
			PersistService persistService = injector.getInstance(PersistService.class);
			persistService.start();
		}
		return instance;
	}	

	
	
/*	private void addRestaurantAndDeliveryZone(){
		RestaurantService restaurantService = injector.getInstance(RestaurantService.class);
		DeliveryZoneDao deliveryZoneDao = injector.getInstance(DeliveryZoneDao.class);
		
		
		
		
		Random r = new Random();
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		GeometryFactory gf = new GeometryFactory();
		
		for (int i = 0; i < 15; i++) {		//Number Restaurant
			double lng = r.nextDouble() * 360 - 180;
			double lat = r.nextDouble() * 180 - 90;
			Date date=new Date();
			Restaurant res = new Restaurant("Restaurant "+i, "Restaurent 1 Description", NetworkStatus.ONLINE,"0987654321", "http://abc.com", "Restaurant " + i +" address","N/A",
					 gf.createPoint(new Coordinate(lat,lng)) , "res1.jpg",false, true,"Ho Chi Minh","Viet Name","700000","Thu duc","MC Thane","trong",date);				
			
			

			for (int k = 0; k < 2; k++) {			//Number Zone
				int randomNumberZone = r.nextInt(2) + 3;				//So Canh Polygon
				Coordinate[] coordinate = new Coordinate[randomNumberZone+1];
				for (int j = 0; j < randomNumberZone; j++) {
					double lngDelivery = lng + (r.nextDouble() * 5 - 2.5);
					double latDelivery = lat + (r.nextDouble() * 5 - 2.5);
					coordinate[j] = new Coordinate(latDelivery, lngDelivery);
				}
				
				coordinate[randomNumberZone] = coordinate[0];
				Polygon polygonR2 = gf.createPolygon(coordinate);
				
				DeliveryZone zoner2 = new DeliveryZone("Zone "+ k, "Zone restaurant"+ i, 12, 3, DeliveryZoneType.CUSTOM, polygonR2,"trong",date);
				
				//res.addDeliveryZone(zoner2);
			}


			
			//restaurants.add(res);				
				
		}
		restaurantService.restaurantDao.saveAll(restaurants);
		
	}*/
	
	  private void addVersion() {
	    	Version v = new Version();
	    	v.setVersionName("v1");
	    	v.setCreatedBy("dineNow");
	    	v.setCreatedDate(new Date());
	    	VersionDao versionDao = injector.getInstance(VersionDao.class);
	    	versionDao.save(v);
		}
	
/*	private void addRestaurantUserMenu(){
		RestaurantService restaurantService = injector.getInstance(RestaurantService.class);
		
		
		if (restaurantService.getUserByEmail("123@gmail.com") == null){
			
			GeometryFactory gf = new GeometryFactory();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			Restaurant r1 = new Restaurant("Res 1", "restaurent 1", NetworkStatus.ONLINE,"06666", "http://abc.com", "Nga 4 Bay Hien","N/A", 
					 gf.createPoint(new Coordinate(10.792808,106.653497)) , "res1.jpg",true,false,"Ho Chi Minh","Viet Name","700000","Thu duc","MC Noida","trong",date);
			Restaurant r2 = new Restaurant("Res 2", "restaurent 2", NetworkStatus.ONLINE,"06666", "http://abc.com", "Lang Cha Ca","N/A",
					 gf.createPoint(new Coordinate(10.800227,106.660824)), "res2.jpg",true,true,"Ho Chi Minh","Viet Name","700000","Thu duc","MC Delhi","trong",date);
			Restaurant r3 = new Restaurant("Res 3", "restaurent 3", NetworkStatus.ONLINE,"06666", "http://abc.com", "Pham Van Hai","N/A",
					 gf.createPoint(new Coordinate(10.789099, 106.660705)), "res3.jpg", false,true,"Ho Chi Minh","Viet Name","700000","Thu duc","MC Pune","trong",date);

			
			RestaurantUserService restaurantUserService = injector.getInstance(RestaurantUserService.class);
			
			RestaurantUser rAdmin = new RestaurantUser();
			email = "admin@gmail.com";
			password = MD5Hash.md5Spring("12345678");
			firstName = "resuser1";
			lastName = "resuser1";
			phone = "123456789";
			
			rAdmin.setEmail(email);
			rAdmin.setPassword(password);
			rAdmin.setRole(UserRole.OWNER);
			rAdmin.setFirstName(firstName);
			rAdmin.setLastName(lastName);
			rAdmin.setPhone(phone);
			rAdmin.setRegisteredDate(date);
			rAdmin.setCreatedBy("trong");
			rAdmin.setCreatedDate(date);
			
			
			String id_customer_striper_Admin = DineNowApplication.stripe.createRestaurantUser(rAdmin);
			rAdmin.setCustomerStripe(id_customer_striper_Admin);
			
			
			
			RestaurantUser ru = new RestaurantUser();
			email = "123@gmail.com";
			password = MD5Hash.md5Spring("12345678");
			firstName = "resuser2";
			lastName = "resuser2";
			phone = "123456789";
			
			ru.setEmail(email);
			ru.setPassword(password);
			ru.setRole(UserRole.OWNER);
			ru.setFirstName(firstName);
			ru.setLastName(lastName);
			ru.setPhone(phone);
			ru.setRegisteredDate(date);
			ru.setCreatedBy("trong");
			ru.setCreatedDate(date);
			
			String id_customer_stripe_ru = DineNowApplication.stripe.createRestaurantUser(ru);
			ru.setCustomerStripe(id_customer_stripe_ru);
			
			RestaurantUser ru1 = new RestaurantUser();
			email = "tanuj.oberoi@udaantechnologies.com";
			password = MD5Hash.md5Spring("12345678");
			firstName = "resuser3";
			lastName = "resuser3";
			phone = "123456789";
			
			ru.setEmail(email);
			ru.setPassword(password);
			ru.setRole(UserRole.OWNER);
			ru.setFirstName(firstName);
			ru.setLastName(lastName);
			ru.setPhone(phone);
			ru.setRegisteredDate(date);
			ru.setCreatedBy("trong");
			ru.setCreatedDate(date);
			
			String id_customer_stripe_ru1 = DineNowApplication.stripe.createRestaurantUser(ru1);
			ru1.setCustomerStripe(id_customer_stripe_ru1);
			
			ru.addRestaurant(r1);
			//r1.addUser(ru);
			
			
			Taxe taxe1 = new Taxe("Taxe", 5,"trong",date);
			Taxe taxe2 = new Taxe("Taxe2", 10,"trong",date);
			
			r2.addTaxe(taxe1);
			r2.addTaxe(taxe2);
			
			ru1.addRestaurant(r2);
			//r2.addUser(ru1);
			
			rAdmin.addRestaurant(r1);
			//r1.addUser(rAdmin);
			
			rAdmin.addRestaurant(r2);
			//r2.addUser(rAdmin);
			
			rAdmin.addRestaurant(r3);
			//r3.addUser(rAdmin);
			
			
			Menu u1 = new Menu("menu name 1", "menu for description","trong",date);
			Menu u2 = new Menu("menu name 2", "menu for description","trong",date);
			Menu u3 = new Menu("menu name 3", "menu for description","trong",date);
			Menu u4 = new Menu("menu name 4", "menu for description","trong",date);
			
			Category cate1 = new Category("Appetizers", "des: khai vi","trong",date);
			Category cate2 = new Category("Desserts", "des: trang mieng","trong",date);
			Category cate3 = new Category("Entrees", "des: mon chinh","trong",date);
			
			
			
			
			Item item1 = new Item("item 1", "item test 1", "notes test 1", true, 3, AvailabilityStatus.AVAILABLE,"http://media.lamsao.com//Resources/Data/News/Auto/huongptp/201301/huongptp20131151413686_1.jpg","trong",date);
			Item item2 = new Item("item 2", "item test 2", "notes test 2", false, 3, AvailabilityStatus.AVAILABLE,"http://media.lamsao.com//Resources/Data/News/Auto/huongptp/201301/huongptp20131151413686_1.jpg","trong",date);
			Item item3 = new Item("item 3", "item test 3", "notes test 3", true, 3, AvailabilityStatus.PAUSED, "http://media.lamsao.com//Resources/Data/News/Auto/huongptp/201301/huongptp20131151413686_1.jpg","trong",date);

			
			CategoryInfo cInfo = new CategoryInfo();
			Set<ItemInfo> itemsinfo1 = new HashSet<ItemInfo>();
			itemsinfo1.add(new ItemInfo(item1,"trong",date));
			itemsinfo1.add(new ItemInfo(item2,"trong",date));
			itemsinfo1.add(new ItemInfo(item3,"trong",date));
			cInfo.setCategory(cate1);
			cInfo.setItems(itemsinfo1);
			cInfo.setCreatedBy("trong");
			cInfo.setCreatedDate(date);
			
			Set<CategoryInfo> categoryinfo = new HashSet<CategoryInfo>();
			categoryinfo.add(cInfo);
			
			SubMenu sub1 = new SubMenu();
			sub1.setMenuSubName("Sub Name 1");
			sub1.setSubMenuDescription("sub menu des");
			sub1.setCategories(categoryinfo);
			sub1.setCreatedDate(date);
			sub1.setCreatedBy("trong");
			sub1.setSubMenuNotes("N/A");
			u1.addSubMenu(sub1);
			
			r1.addItem(item1);
			r1.addItem(item2);
			r1.addItem(item3);
			
			r1.addCategory(cate1);
			
			r1.addSubMenu(sub1);
			
			
			r1.addMenu(u1);
			r1.addMenu(u2);
			r2.addMenu(u3);
			r3.addMenu(u4);
			
			
			
			Coordinate[] coordinate1 = new Coordinate[6];
			coordinate1[0] = new Coordinate(10.794653, 106.650159);
			coordinate1[1] = new Coordinate(10.790838, 106.648721);
			coordinate1[2] = new Coordinate(10.788372, 106.652948);
			coordinate1[3] = new Coordinate(10.796529, 106.654579);
			coordinate1[4] = new Coordinate(10.792335, 106.657562);
			coordinate1[5] = new Coordinate(10.794653, 106.650159);
			
			Polygon polygonR1 = gf.createPolygon(coordinate1);
			
			DeliveryZone zoner1 = new DeliveryZone("Zone 1", "Zone restaurant 1", 12, 3, DeliveryZoneType.CUSTOM, polygonR1,"trong",date);
			
			
			Coordinate[] coordinate2 = new Coordinate[4];
			coordinate2[0] = new Coordinate(10.803211, 106.660051);
			coordinate2[1] = new Coordinate(10.796909, 106.658613);
			coordinate2[2] = new Coordinate(10.800302, 106.667475);
			coordinate2[3] = new Coordinate(10.803211, 106.660051);
			
			
			Polygon polygonR2 = gf.createPolygon(coordinate2);
			
			DeliveryZone zoner2 = new DeliveryZone("Zone 2", "Zone restaurant 2", 12, 3, DeliveryZoneType.CUSTOM, polygonR2,"trong",date);

			
			
			Coordinate[] coordinate3 = new Coordinate[4];
			coordinate3[0] = new Coordinate(10.790122, 106.664750);
			coordinate3[1] = new Coordinate(10.791639, 106.658957);
			coordinate3[2] = new Coordinate(10.786027, 106.659327);
			coordinate3[3] = new Coordinate(10.790122, 106.664750);
			
			Polygon polygonR3 = gf.createPolygon(coordinate3);
			
			DeliveryZone zoner3 = new DeliveryZone("Zone 3", "Zone restaurant 3", 12, 3, DeliveryZoneType.CUSTOM, polygonR3,"trong",date);
			
			
			r1.addDeliveryZone(zoner1);
			r2.addDeliveryZone(zoner2);
			r3.addDeliveryZone(zoner3);
			
			
			
			
			
			ServiceResult<Restaurant> result = restaurantService
					.createNewRestaurant(r1);
			if (result.hasErrors()) {
				throw new RuntimeException(result.getErrors().toString());
			}

			result = restaurantService.createNewRestaurant(r2);
			if (result.hasErrors()) {
				throw new RuntimeException(result.getErrors().toString());
			}

			result = restaurantService.createNewRestaurant(r3);
			if (result.hasErrors()) {
				throw new RuntimeException(result.getErrors().toString());
			}
		}

	}
	
	private void addCustomer(){
		CustomerService customerService = injector.getInstance(CustomerService.class);


		
		Customer customer = new Customer();
		//"First Name", "LastName", "123@gmail.com", "12345678", "095465444", "51 hoang viet", "facebook:123456789"
		customer.setFirstName("First Name");
		customer.setLastName(lastName);
		customer.setEmail(email);
		customer.setPassword("12345678");
		customer.setPhoneNumber("099738232323");

		Customer customer1 = new Customer("First Name", "LastName", "1234@gmail.com", "12345678", "095465444", "51 hoang viet", "google:123456789");
		Customer customer2 = new Customer("First Name", "LastName", "12345@gmail.com", "12345678", "095465444", "51 hoang viet", "facebook:0987654321");
		
		customerService.createNewCustomer(customer);
		customerService.createNewCustomer(customer1);
		customerService.createNewCustomer(customer2);
		
		
	}
	*/
	
	
//    private void addTasks() {
//        TaskService taskService = injector.getInstance(TaskService.class);
//        LanguageDao languageDao = injector.getInstance(LanguageDao.class);
//        Task t1 = new Task("task 1");
//        t1.setDescription("This is task 1");
//        t1.setLanguage(languageDao.findByExactName("english"));
//
//        SubTask sub1 = new SubTask();
//        sub1.setDescription("this a subtask 1");
//        t1.addSubTask(sub1);
//
//        ServiceResult<Task> taskServiceResult = taskService.createNewTask(t1);
//        if (taskServiceResult.hasErrors()) {
//            throw new RuntimeException(taskServiceResult.getErrors().toString());
//        }
//
//        Task t2 = new Task("task 2");
//        t2.setDescription("This is task 2");
//        t2.setLanguage(languageDao.findByExactName("english"));
//        taskServiceResult = taskService.createNewTask(t2);	9
//        if (taskServiceResult.hasErrors()) {
//            throw new RuntimeException(taskServiceResult.getErrors().toString());
//        }
//
//    }
	
	public Version getLatestVersion(){
		VersionDao versionDao = injector.getInstance(VersionDao.class);
    	return versionDao.getVersion();
	}
}
