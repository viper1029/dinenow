package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.Hour;
import com.dinenowinc.dinenow.model.helpers.LatLng;
import com.dinenowinc.dinenow.model.helpers.NetworkStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.envers.Audited;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Restaurant.GetAll", query = "from Restaurant r where status=0") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant extends BaseEntity {

  private static final long serialVersionUID = -524833031492135630L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  private String website;

  private boolean active;

  @Column(name = "contact_person")
  private String contactPerson;

  @Column(columnDefinition = "Decimal(10,1)")
  private double rating;

  @Column(name = "network_status", nullable = false)
  private NetworkStatus networkStatus;

  private String stripe;

  private String cuisine;

  private String timezone;

  @Column(length = 10000000, name = "delivery_hours")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String accept_delivery_hours = "[]";

  @Column(length = 10000000, name = "takeout_hours")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String accept_takeout_hours = "[]";

  @Column(length = 10000000, name = "dinein_hours")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String dineInHours = "[]";

  @Column(name = "accept_delivery", nullable = false)
  private boolean acceptDelivery;

  @Column(name = "accept_takeout", nullable = false)
  private boolean acceptTakeout;

  @Column(name = "accept_dinein", nullable = false)
  private boolean acceptDineIn;

  private Point location;

  @Column(nullable = false)
  private String address_1;

  @Column(nullable = false)
  private String address_2;

  @Column(nullable = false)
  private String city;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String province;

  @Column(nullable = false)
  private String country;

  private String keyword;

  @Column
  private String logo;

  @Transient
  private double distance;

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_deliveryZone")
  private final Set<DeliveryZone> deliveryZone = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_items")
  private final Set<Item> items = new HashSet<>();


  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_categories")
  private final Set<Category> categories = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @Basic(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_menus")
  private final Set<Menu> menus = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_addOns")
  private final Set<Addon> addons = new HashSet<>();


  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_sizes")
  private final Set<Size> sizes = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_modifiers")
  private final Set<Modifier> modifiers = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false, foreignKey = @javax.persistence.ForeignKey(name = "Fk_restaurant_tax"))
  private final Set<Tax> taxes = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_reviews")
  private final Set<Review> reviews = new HashSet<>();

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinTable(name = "restaurant_payment_type", joinColumns = @JoinColumn(name = "id_restaurant"), inverseJoinColumns = @JoinColumn(name = "id_payment_type"))
  //@ForeignKey(name = "Fk_restaurant_paymentType")
  private Set<PaymentType> paymentTypes = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_orders")
  private final Set<Order> orders = new HashSet<>();

  @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_carts")
  private final Set<Cart> carts = new HashSet<>();

  @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "id_restaurant", nullable = false)
  @ForeignKey(name = "Fk_restaurant_closed_days")
  private Set<ClosedDay> closedDay = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "restaurant_restaurant_user", joinColumns = @JoinColumn(name = "id_restaurant"), inverseJoinColumns = @JoinColumn(name = "id_restaurant_user"))
  //@ForeignKey(name = "Fk_restaurant_users")
  private Set<RestaurantUser> users = new HashSet<>();


  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezoneId) {
    this.timezone = timezoneId;
  }

  public String getCuisine() {
    return cuisine;
  }

  public void setCuisine(String cuisine) {
    this.cuisine = cuisine;
  }

  public Restaurant() {
  }

  public Restaurant(String resName, String des, NetworkStatus networkStatus, String phone, String web, String address1, String address2,
      Point location, String imagelink, boolean delivery, boolean takeout, String city, String country
      , String postalCode, String province, String keyword, String createBy, Date createDate) {
    this.name = resName;
    this.description = des;
    this.networkStatus = networkStatus;
    this.phoneNumber = phone;
    this.website = web;
    this.address_1 = address1;
    this.address_2 = address2;
    this.acceptDelivery = delivery;
    this.acceptTakeout = takeout;
    this.location = location;
    this.city = city;
    this.keyword = keyword;
    this.country = country;
    this.postalCode = postalCode;
    this.province = province;
    this.setCreatedDate(createDate);
    this.setCreatedBy(createBy);
  }


  public Set<Tax> getTaxes() {
    return taxes;
  }

  public void addTaxes(Tax t) {
    taxes.add(t);
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }

  public void addModifier(Modifier m) {
    getModifiers().add(m);
  }

  public Set<Size> getSizes() {
    return sizes;
  }

  public void addSizes(Size size) {
    getSizes().add(size);
  }


  public Set<Addon> getAddons() {
    return addons;
  }

  public void addAddOns(Addon addon) {
    getAddons().add(addon);
  }


  public Set<Menu> getMenus() {
    return menus;
  }

  public void addMenu(Menu menu) {
    getMenus().add(menu);
  }

  public Set<Item> getItems() {
    return items;
  }

  public void addItem(Item item) {
    getItems().add(item);
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public void addCategory(Category category) {
    getCategories().add(category);
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void addOrder(Order order) {
    getOrders().add(order);
  }

  public Set<RestaurantUser> getUsers() {
    return users;
  }

  public void addUser(RestaurantUser user) {
    getUsers().add(user);
  }

  public Set<PaymentType> getPaymentTypes() {
    return paymentTypes;
  }

  public void addPaymentTypes(PaymentType paymentTypes) {
    getPaymentTypes().add(paymentTypes);
  }

  public Set<DeliveryZone> getDeliveryZone() {
    return deliveryZone;
  }

  public void addDeliveryZone(DeliveryZone zone) {
    getDeliveryZone().add(zone);
  }


  public Set<ClosedDay> getClosedDay() {
    return closedDay;
  }

  public void setClosedDay(Set<ClosedDay> closedDay) {
    this.closedDay = closedDay;
  }

  public void addClosedDay(ClosedDay day) {
    getClosedDay().add(day);
  }

  public Set<Review> getReviews() {
    return reviews;
  }

  public void addReview(Review review) {
    getReviews().add(review);
  }

  public NetworkStatus getNetworkStatus() {
    return networkStatus;
  }

  public void setNetworkStatus(NetworkStatus networkStatus) {
    this.networkStatus = networkStatus;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }


  public Set<Cart> getCarts() {
    return carts;
  }

  public void addCarts(Cart cart) {
    getCarts().add(cart);
  }

  public ArrayList<Hour> getDineInHours() {
    try {
      if (this.dineInHours == null) {
        this.dineInHours = "[]";
      }
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      ArrayList<Hour> list = mapper.readValue(this.dineInHours, new TypeReference<List<Hour>>() {
      });
      Iterator<Hour> iter = list.iterator();
      while (iter.hasNext()) {
        Hour hour = iter.next();
        System.out.println("::::::::::::::::::::::::" + hour.getFromTime().getDay());
        for (ClosedDay day : getClosedDay()) {
          if (hour.getFromTime().getDay() == day.getDate().getDay() && hour.getFromTime().getMonth() == day.getDate().getMonth() &&
              hour.getFromTime().getYear() == day.getDate().getYear()) {
            System.out.println(":::::::::::::::::::::::cc:" + day.getDate().getDay());
            iter.remove();
            break;
          }
        }
      }
      return list;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  public void setDineInHours(List<Hour> dineInHours) {
    try {
      this.dineInHours = "";
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      this.dineInHours = mapper.writeValueAsString(dineInHours);
    }
    catch (IOException e) {
      this.dineInHours = "";
      e.printStackTrace();
    }
  }

  public ArrayList<Hour> getAcceptDeliveryHours() {
    try {
      if (this.accept_delivery_hours == null) {
        this.accept_delivery_hours = "[]";
      }
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      ArrayList<Hour> list = mapper.readValue(this.accept_delivery_hours, new TypeReference<List<Hour>>() {
      });
      Iterator<Hour> iter = list.iterator();
      while (iter.hasNext()) {
        Hour hour = iter.next();
        System.out.println("::::::::::::::::::::::::" + hour.getFromTime().getDay());
        for (ClosedDay day : getClosedDay()) {
          if (hour.getFromTime().getDay() == day.getDate().getDay() && hour.getFromTime().getMonth() == day.getDate().getMonth() &&
              hour.getFromTime().getYear() == day.getDate().getYear()) {
            System.out.println(":::::::::::::::::::::::cc:" + day.getDate().getDay());
            iter.remove();
            break;
          }
        }
      }
      return list;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  public void setAcceptDeliveryHours(List<Hour> acceptDeliveryHours) {
    try {
      this.accept_delivery_hours = "";
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      this.accept_delivery_hours = mapper.writeValueAsString(acceptDeliveryHours);
    }
    catch (IOException e) {
      this.accept_delivery_hours = "";
      e.printStackTrace();
    }
  }


  public ArrayList<Hour> getAcceptTakeOutHours() {
    try {
      if (this.accept_takeout_hours == null) {
        this.accept_takeout_hours = "[]";
      }
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      mapper.setDateFormat(dateFormat);
      ArrayList<Hour> list = mapper.readValue(this.accept_takeout_hours, new TypeReference<List<Hour>>() {
      });
/*			Iterator<Hour> iter = list.iterator();
      while (iter.hasNext()) {
				Hour hour = iter.next();
				System.out.println("::::::::::::::::::::::::"+hour.getFromTime().getDay());
				for(ClosedDay day : getClosedDay()){
					if(hour.getFromTime().getDay()==day.getDate().getDay() && hour.getFromTime().getMonth()==day.getDate().getMonth() &&
							hour.getFromTime().getYear()==day.getDate().getYear()){
						System.out.println(":::::::::::::::::::::::cc:"+day.getDate().getDay());
						iter.remove();
						break;
					}
				}
			}*/
      return list;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  public void setAcceptTakeOutHours(List<Hour> acceptDeliveryHours) {
    try {
      this.accept_takeout_hours = "";
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      mapper.setDateFormat(dateFormat);
      this.accept_takeout_hours = mapper.writeValueAsString(acceptDeliveryHours);
    }
    catch (IOException e) {
      this.accept_takeout_hours = "";
      e.printStackTrace();
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phone_number) {
    this.phoneNumber = phone_number;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getStripe() {
    return stripe;
  }

  public void setStripe(String stripe) {
    this.stripe = stripe;
  }

  public boolean isAcceptDineIn() {
    return acceptDineIn;
  }

  public void setAcceptDineIn(boolean accept_dinein) {
    this.acceptDineIn = accept_dinein;
  }

  public boolean isAcceptDelivery() {
    return acceptDelivery;
  }

  public void setAcceptDelivery(boolean accept_delivery) {
    this.acceptDelivery = accept_delivery;
  }

  public boolean isAcceptTakeout() {
    return acceptTakeout;
  }

  public void setAcceptTakeout(boolean accept_takeout) {
    this.acceptTakeout = accept_takeout;
  }

  public String getAddress1() {
    return address_1;
  }

  public void setAddress1(String address_1) {
    this.address_1 = address_1;
  }

  public String getAddress2() {
    return address_2;
  }

  public void setAddress2(String address_2) {
    this.address_2 = address_2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postal_code) {
    this.postalCode = postal_code;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("description", this.getDescription());
    dto.put("acceptDelivery", this.isAcceptDelivery());
    dto.put("acceptDineIn", this.isAcceptDineIn());
    dto.put("acceptTakeOut", this.isAcceptTakeout());
    dto.put("location", this.getLocation() != null ? new LatLng(this.getLocation().getX(), this.getLocation().getY()) : "");
    dto.put("address1", this.getAddress1());
    dto.put("address2", this.getAddress2());
    dto.put("city", this.getCity());
    dto.put("keyword", this.getKeyword());
    dto.put("province", this.getProvince());
    dto.put("postalCode", this.getPostalCode());
    dto.put("country", this.getCountry());
    dto.put("phoneNumber", this.getPhoneNumber());
    dto.put("networkStatus", this.getNetworkStatus());
    dto.put("contactPerson", this.getContactPerson());
    dto.put("webSite", this.getWebsite());
    dto.put("rating", this.getRating());
    dto.put("active", this.isActive());
//		dto.put("stripe", entity.getStripe());
    dto.put("dineInHours", this.getDineInHours());
    dto.put("acceptDeliveryHours", this.getAcceptDeliveryHours());
    dto.put("acceptTakeOutHours", this.getAcceptTakeOutHours());
    dto.put("cuisine", this.getCuisine());
    dto.put("paymentTypes", this.getPaymentTypes());
    dto.put("timezone", this.getTimezone());
    dto.put("logo", this.getLogo());
    return dto;
  }

}
