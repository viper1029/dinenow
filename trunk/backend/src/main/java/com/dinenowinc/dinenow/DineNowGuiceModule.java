package com.dinenowinc.dinenow;

import com.dinenowinc.dinenow.dao.AddonDao;
import com.dinenowinc.dinenow.dao.AddressBookDao;
import com.dinenowinc.dinenow.dao.BaseEntityDAOImpl;
import com.dinenowinc.dinenow.dao.CartDao;
import com.dinenowinc.dinenow.dao.CartItemDao;
import com.dinenowinc.dinenow.dao.CategoryDao;
import com.dinenowinc.dinenow.dao.CouponDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.dao.DeliveryZoneDao;
import com.dinenowinc.dinenow.dao.ItemDao;
import com.dinenowinc.dinenow.dao.ItemInfoDao;
import com.dinenowinc.dinenow.dao.MenuDao;
import com.dinenowinc.dinenow.dao.ModifierDao;
import com.dinenowinc.dinenow.dao.OrderDao;
import com.dinenowinc.dinenow.dao.OrderDetailsDao;
import com.dinenowinc.dinenow.dao.PaymentTypeDao;
import com.dinenowinc.dinenow.dao.RestaurantDao;
import com.dinenowinc.dinenow.dao.RestaurantUserDao;
import com.dinenowinc.dinenow.dao.ReviewDao;
import com.dinenowinc.dinenow.dao.RoleDao;
import com.dinenowinc.dinenow.dao.SizeDao;
import com.dinenowinc.dinenow.dao.TaxDao;
import com.dinenowinc.dinenow.dao.VersionDao;
import com.dinenowinc.dinenow.model.Addon;
import com.dinenowinc.dinenow.model.AddressBook;
import com.dinenowinc.dinenow.model.Cart;
import com.dinenowinc.dinenow.model.CartItem;
import com.dinenowinc.dinenow.model.Category;
import com.dinenowinc.dinenow.model.Coupon;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.DeliveryZone;
import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.ItemPrice;
import com.dinenowinc.dinenow.model.Menu;
import com.dinenowinc.dinenow.model.Modifier;
import com.dinenowinc.dinenow.model.Order;
import com.dinenowinc.dinenow.model.OrderDetail;
import com.dinenowinc.dinenow.model.PaymentType;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.RestaurantUser;
import com.dinenowinc.dinenow.model.Review;
import com.dinenowinc.dinenow.model.Role;
import com.dinenowinc.dinenow.model.Size;
import com.dinenowinc.dinenow.model.Tax;
import com.dinenowinc.dinenow.model.Version;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class DineNowGuiceModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(new TypeLiteral<BaseEntityDAOImpl<RestaurantUser, String>>() {}).to(RestaurantUserDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Restaurant, String>>() {}).to(RestaurantDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Customer, String>>() {}).to(CustomerDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Menu, String>>() {}).to(MenuDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Category, String>>() {}).to(CategoryDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Item, String>>() {}).to(ItemDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<DeliveryZone, String>>() {}).to(DeliveryZoneDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Addon, String>>() {}).to(AddonDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Size, String>>() {}).to(SizeDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Modifier, String>>() {}).to(ModifierDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<OrderDetail, String>>() {}).to(OrderDetailsDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Order, String>>() {}).to(OrderDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Coupon, String>>() {}).to(CouponDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Role, String>>() {}).to(RoleDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<PaymentType, String>>() {}).to(PaymentTypeDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<AddressBook, String>>() {}).to(AddressBookDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Version, String>>() {}).to(VersionDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<ItemPrice, String>>() {}).to(ItemInfoDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Tax, String>>() {}).to(TaxDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Review, String>>() {}).to(ReviewDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<Cart, String>>() {}).to(CartDao.class);
		bind(new TypeLiteral<BaseEntityDAOImpl<CartItem, String>>() {}).to(CartItemDao.class);
	}
}
