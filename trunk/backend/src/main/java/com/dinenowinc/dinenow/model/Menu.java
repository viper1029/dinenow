package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ @NamedQuery(name = "Menu.GetAll", query = "from Menu m") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends BaseEntity {

	private static final long serialVersionUID = -977770296392567663L;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, columnDefinition="TEXT")
	private String description;

	@Column(length = 10000000)
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String hours = "[]";
	
	@Column(nullable=false , unique=true)
	private Integer compositeId;

/*	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "menu_submenu", joinColumns = @JoinColumn(name = "id_menu"), inverseJoinColumns = @JoinColumn(name = "id_sub_menu"))
	@ForeignKey(name = "Fk_menu_subMenus")
	private Set<SubMenu> subMenus = new HashSet<SubMenu>();*/
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY,orphanRemoval=true)
    @JoinColumn(name="id_menu",nullable=false ,foreignKey= @ForeignKey(name = "id_menu-menu_category-Fk"))	
	private final Set<CategoryInfo> categories = new HashSet<CategoryInfo>();	
	
//	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	@JoinTable(foreignKey = @ForeignKey(name = "Fk_restaurant_menu"),name = "restaurant_menu", joinColumns = @JoinColumn(name = "id_restaurant"), inverseJoinColumns = @JoinColumn(name = "id_menu"))
//	private Set<Restaurant> restaurant = new HashSet<Restaurant>();
	
	public boolean isShowMenu() {
		for (Hour hour : getHours()) {
			// SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); //
			// the day of the week abbreviated
			// String dayofWeekNow =
			// simpleDateformat.format(date_now).toLowerCase();
			// String dayofWeekMenu =
			// hour.getWeekDayType().toString().toLowerCase();
			//
			//
			//
			// if (dayofWeekMenu.equals(dayofWeekNow) && ) {
			//
			// }
			Date date_now = new Date();
			if (hour.getFromTime().compareTo(date_now) < 0
					&& hour.getToTime().compareTo(date_now) > 0) {
				return true;
			}
		}
		return false;
	}

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
	
	public Menu() {
	}

	public Menu(String name, String des, 
			String createdBy, Date createdDate) {
		this.name = name;
		this.description = des;
//		this.id_restaurant=restaurantID;
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}

	public String getMenuName() {
		return name;
	}

	public void setMenuName(String menuName) {
		this.name = menuName;
	}

/*	public Set<SubMenu> getSubMenus() {
		return subMenus;
	}

	public void addSubMenu(SubMenu submenu) {
		getSubMenus().add(submenu);
	}*/

	public String getMenuDescription() {
		return description;
	}

	public void setMenuDescription(String menuDescription) {
		this.description = menuDescription;
	}
	
	   public Set<CategoryInfo> getCategories() {
			return categories;
		}
	    
/*	    public void setCategories(Set<CategoryInfo> categories) {
			this.categories = categories;
		}*/
	    
	    public void addCategory(CategoryInfo category){
	    	getCategories().add(category);
	    }
	    
	    public void addAllCategory(ArrayList<CategoryInfo> categorys){
	    	getCategories().clear();
	    	getCategories().addAll(categorys);
	    }

	public List<Hour> getHours() {
		try {
			if (this.hours == null) {
				this.hours = "[]";
			}
			ObjectMapper mapper = new ObjectMapper();
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			mapper.setDateFormat(dateFormat);
			return (mapper.readValue(this.hours,
					new TypeReference<List<Hour>>() {
					}));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Hour>();
	}

	public void setHours(List<Hour> hours) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			mapper.setDateFormat(dateFormat);
			this.hours = mapper.writeValueAsString(hours);
		} catch (IOException e) {
			this.hours = "";
			e.printStackTrace();
		}
	}

	public void addHour(Hour hour) {
		this.getHours().add(hour);
	}

	@Override
	public HashMap<String, Object> toDto() {
		LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getMenuName());
		dto.put("description", this.getMenuDescription());
		dto.put("hours", this.getHours());
		return dto;
	}

}
