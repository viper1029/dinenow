package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Item.GetAll", query = "from Item m")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends AvailabilityEntity {
	
	private static final long serialVersionUID = -8397322080318233174L;
	
	@Column(nullable=false)
	private String name;
	@Column(nullable=false, columnDefinition="TEXT")
	private String description;
	@Column(nullable=false)
	private String notes;

	@Column(nullable=false)
	private boolean is_vegeterian;
	
	@Column(nullable=false)
	private String image;
	
//	@Column(columnDefinition="0 - Unspecified, 1- Medium, 2- Hot, 3 - Extra Hot, Default should be 0")
	private int spice_level = 0;
	
	@Column(nullable=false,unique=true)
	private Integer compositeId;
	
	public Integer getCompositeId() {
		return compositeId;
	}

	public void setCompositeId(String resturantId) {
		final int prime = 31;
        int result = 1;
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((resturantId == null) ? 0 : resturantId.hashCode());
		this.compositeId = result;
	}

	/*private boolean isShowSpice;
	@Column(nullable=false,columnDefinition="Decimal(10,2)")
	private double price;
	@Column(length = 10000000)
	@Lob
	private String keywords = "[]";
	private OrderType orderType;*/

	@OneToMany(cascade = {CascadeType.PERSIST}, fetch= FetchType.LAZY)
    @Basic(fetch = FetchType.LAZY)
    @JoinColumn(name="id_item",nullable=false)
	@ForeignKey(name="Fk_item_orderDetail")
    private final Set<OrderDetail> orderDetails = new HashSet<OrderDetail>();
	
	@OneToMany(cascade = {CascadeType.PERSIST}, fetch= FetchType.LAZY)
    @Basic(fetch = FetchType.LAZY)
    @JoinColumn(name="id_item",nullable=false)
	@ForeignKey(name="Fk_item_cartItem")
    private final Set<CartItem> cartItems = new HashSet<CartItem>();
	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.EAGER)
//    @JoinColumn(name="id_item")
//	private final Set<SizePrice> sizePrices = new HashSet<SizePrice>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER,orphanRemoval=true)
	@JoinColumn(name = "id_item")	
	@ForeignKey(name="Fk_item_sizes")
    private final Set<SizeInfo> sizes = new HashSet<SizeInfo>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER,orphanRemoval=true)
	@JoinColumn(name = "id_item",nullable=false)
	@ForeignKey(name="Fk_item_modifiers")
    private final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();

	public Item() {
	}
	
	public Item(String name, String description,String notes, boolean isvegeterian, 
			int spicelevel, AvailabilityStatus availabilityStatus,String image,String createdBy,Date createdDate) {
		this.name = name;
		this.description = description;
		this.notes = notes;
		this.is_vegeterian = isvegeterian;
		this.spice_level = spicelevel;
		this.image=image;
		this.setAvailabilityStatus(availabilityStatus);
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}
	
	
	public Set<SizeInfo> getSizes() {
		return sizes;
	}
	
	public void addSize(SizeInfo size){
		getSizes().add(size);
    }
    
    public void addAllSize(ArrayList<SizeInfo> sizes){
    	getSizes().clear();
    	getSizes().addAll(sizes);
    }
	
	
	public Set<ModifierInfo> getModifiers() {
		return modifiers;
	}
	
	public void addModifier(ModifierInfo modifier){
		getModifiers().add(modifier);
    }
    
    public void addAllModifier(ArrayList<ModifierInfo> modifiers){
    	getModifiers().clear();
    	getModifiers().addAll(modifiers);
    }
	
	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	
	public void addOrderDetails(OrderDetail order){
		getOrderDetails().add(order);
	}
	
	
	
//	public OrderType getOrderType() {
//		return orderType;
//	}
//
//	public void setOrderType(OrderType orderType) {
//		this.orderType = orderType;
//	}
//	
//	public boolean isShowSpice() {
//		return isShowSpice;
//	}
//
//	public void setShowSpice(boolean isShowSpice) {
//		this.isShowSpice = isShowSpice;
//	}
	
	public Set<CartItem> getCartItems() {
		return cartItems;
	}
	
	public void addCartItems(CartItem cartItem){
		getCartItems().add(cartItem);
	}

	public String getItemName() {
		return name;
	}

	public void setItemName(String itemName) {
		this.name = itemName;
	}
	public String getItemDescription() {
		return description;
	}

	public void setItemDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isVegeterian() {
		return is_vegeterian;
	}

	public void setVegeterian(boolean isVegeterian) {
		this.is_vegeterian = isVegeterian;
	}

	public int getSpiceLevel() {
		return spice_level;
	}

	public void setSpiceLevel(int spiceLevel) {
		this.spice_level = spiceLevel;
	}

	
	public String getLinkImage() {
		return image;
	}

	public void setLinkImage(String linkImage) {
		this.image = linkImage;
	}
	
	
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getItemName());
		dto.put("description", this.getItemDescription());		
		dto.put("notes", this.getNotes());		
		dto.put("availabilityStatus", this.getAvailabilityStatus());
		dto.put("isVegeterian", this.isVegeterian());
		dto.put("spiceLevel", this.getSpiceLevel());
		dto.put("linkImage", this.getLinkImage());	
		return dto;	
	}	
}
