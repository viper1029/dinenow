package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/*@Entity
@Audited
@NamedQueries({@NamedQuery(name="SubMenu.GetAll", query = "from SubMenu m")})*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubMenu extends BaseEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1650171002194604404L;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false,unique=true, columnDefinition="TEXT")
	private String description;
	@Column(nullable=false)
	private String notes;
	
	
/*	@OneToMany(cascade = CascadeType.PERSIST,  fetch= FetchType.LAZY)
    @JoinColumn(name="id_submenu",nullable=false)	
	@ForeignKey(name="Fk_subMenu_categories")
	private Set<CategoryInfo> categories = new HashSet<CategoryInfo>();	*/
	
	public SubMenu() {
	}
	
	public SubMenu(String name, String des) {
		this.name = name;
		this.description = des;
	}

	
 /*   public Set<CategoryInfo> getCategories() {
		return categories;
	}
    
    public void setCategories(Set<CategoryInfo> categories) {
		this.categories = categories;
	}
    
    public void addCategory(CategoryInfo category){
    	getCategories().create(category);
    }
    
    public void addAllCategory(ArrayList<CategoryInfo> categorys){
    	getCategories().clear();
    	getCategories().addAll(categorys);
    }*/
    
    public String getMenuSubName() {
		return name;
	}

	public void setMenuSubName(String menuSubName) {
		this.name = menuSubName;
	}

	public String getSubMenuDescription() {
		return description;
	}

	public void setSubMenuDescription(String subMenuDescription) {
		this.description = subMenuDescription;
	}
	
	public String getSubMenuNotes() {
		return notes;
	}

	public void setSubMenuNotes(String notes) {
		this.notes = notes;
	}
	
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getMenuSubName());
		dto.put("description", this.getSubMenuDescription());
		dto.put("notes", this.getSubMenuNotes());
		return dto;
	}
	
	
	
	
}
