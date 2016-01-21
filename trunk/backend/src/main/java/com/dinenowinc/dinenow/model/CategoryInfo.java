package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="menu_category")
@Audited
@NamedQueries({@NamedQuery(name="CategoryInfo.GetAll", query = "from CategoryInfo c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryInfo extends InfoCategory<Category> {
		
	public CategoryInfo()
	{
		setCreatedBy("Auto");
		setCreatedDate(new Date());
	}
	//@OneToOne(cascade = CascadeType.ALL, fetch= FetchType.LAZY)	
    //private final Category category = new Category();
	public void AddOnCategory(Category category) {
		// TODO Auto-generated constructor stub
		setCategory(category);
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER,orphanRemoval=true)
	@JoinColumn(name = "id_category_info",nullable=false)	
	@ForeignKey(name="Fk_categoryInfo_itemInfo")
    private final Set<ItemInfo> items = new HashSet<ItemInfo>();
	public Set<ItemInfo> getItems() {
		return this.items;
	}
	
    
    public void addItem(ItemInfo item){
    	getItems().add(item);
    }
    
    public void addAllItem(ArrayList<ItemInfo> items){
    	getItems().clear();
    	getItems().addAll(items);
    }
    
/*	public void  setItems(Set<ItemInfo> items) {
		this.items = items;
	}*/
	

	
}
