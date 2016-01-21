
package com.dinenowinc.dinenow.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Category.GetAll", query = "from Category c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends BaseEntity{
	
	private static final long serialVersionUID = 691091407346434736L;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false, columnDefinition="TEXT")
	private String description;
	
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
	
//    @ManyToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY)
//	@JoinTable(name = "Category_Item", joinColumns = @JoinColumn(name = "id_category"), inverseJoinColumns = @JoinColumn(name = "id_item"))
//    private final Set<Item> items = new HashSet<Item>();
    
//	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
//	@Basic(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_restaurant", nullable = false)
//	@ForeignKey(name = "Fk_restaurant_categories")
//	private String id_restaurant;
//
//	public String getRestaurantID() {
//		return id_restaurant;
//	}
//
//	public void setRestaurantID(String restaurantID) {
//		this.id_restaurant = restaurantID;
//	}
    
    
    public Category() {
	}
    
    public Category(String name, String des,String createdBy,Date createdDate) {
    	this.name = name;
    	this.description = des;
//    	this.id_restaurant=restaurantID;
    	this.setCreatedBy(createdBy);
    	this.setCreatedDate(createdDate);
	}
    
    
	
	public String getCategoryName() {
		return name;
	}
	public void setCategoryName(String categoryName) {
		this.name = categoryName;
	}
	public String getCategoryDescription() {
		return description;
	}
	public void setCategoryDescription(String description) {
		this.description = description;
	}
//	public Set<Item> getItems() {
//		return items;
//	}
//	public void addItem(Item item){
//		getItems().add(item);
//	}
	
	
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getCategoryName());
		dto.put("description", this.getCategoryDescription());
		return dto;
	}
	
}
