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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Modifier.GetAll", query = "from Modifier m")})
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer" })
public class Modifier extends BaseEntity{
	@Column(nullable=false)
	private String name;
	@Column(nullable=false, columnDefinition="TEXT")
	private String description;
	@Column(nullable=false, name="multiple_selection")
	private boolean is_select_multiple;
	@Column(nullable=false)
	private int min_selection;
	@Column(nullable=false)
	private int max_selection;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER,orphanRemoval=true)
    @JoinColumn(name="id_modifier")	
	@ForeignKey(name="Fk_modifier_addOnInfo")
	private final Set<ModifierAddOn> addOns = new HashSet<ModifierAddOn>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER,orphanRemoval=true)
	@JoinColumn(name = "id_modifier",nullable=false)	
	@ForeignKey(name="Fk_modifier_itemSizeInfo")
    private final Set<ItemSizeInfo> items = new HashSet<ItemSizeInfo>();
//
//
//  @ManyToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY)
//  @JoinTable(name = "Modifier_Item", joinColumns = @JoinColumn(name = "id_modifier"), inverseJoinColumns = @JoinColumn(name = "id_item"))
//  private final Set<Item> items = new HashSet<Item>();
	
	
	
	
//	public Set<Item> getItems() {
//		return items;
//	}
//	public void addItem(Item item){
//		getItems().create(item);
//	}
//
//	public Set<AddOnInfo> getAddons() {
//		return addOns;
//	}
//	
//	public void addAddOn(AddOnInfo addonInfo){
//		getAddons().create(addonInfo);
//	}
	

	public Set<ModifierAddOn> getAddOns() {
		return addOns;
	}
    
    
    public void addAddOn(ModifierAddOn info){
    	getAddOns().add(info);
    }
    
    public void addAllAddOns(ArrayList<ModifierAddOn> infos){
    	getAddOns().clear();
    	getAddOns().addAll(infos);
    }
    
    
	public Set<ItemSizeInfo> getItems() {
		return items;
	}
    
    
    public void addItem(ItemSizeInfo info){
    	getItems().add(info);
    }
    
    public void addAllItems(ArrayList<ItemSizeInfo> infos){
    	getItems().clear();
    	getItems().addAll(infos);
    }
    
	
	
	public String getModifierName() {
		return name;
	}
	public void setModifierName(String modifierName) {
		this.name = modifierName;
	}
	public String getModifierDescription() {
		return description;
	}
	public void setModifierDescription(String modifierDescription) {
		this.description = modifierDescription;
	}
	public boolean isSelectMultiple() {
		return is_select_multiple;
	}
	public void setSelectMultiple(boolean isSelectMultiple) {
		this.is_select_multiple = isSelectMultiple;
	}
	public int getMinSelection() {
		return min_selection;
	}
	public void setMinSelection(int minSelection) {
		this.min_selection = minSelection;
	}
	public int getMaxSelection() {
		return max_selection;
	}
	public void setMaxSelection(int maxSelection) {
		this.max_selection = maxSelection;
	}
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getModifierName());
		dto.put("description", this.getModifierDescription());
		dto.put("isSelectMultiple", this.isSelectMultiple());
		dto.put("minSelection", this.getMinSelection());
		dto.put("maxSelection", this.getMaxSelection());
		return dto;
	}	

}
