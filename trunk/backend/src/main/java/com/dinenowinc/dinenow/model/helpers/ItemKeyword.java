package com.dinenowinc.dinenow.model.helpers;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.dinenowinc.dinenow.model.Item;
import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({@NamedQuery(name="ItemKeyword.GetAll", query = "from ItemKeyword k")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemKeyword extends BaseEntity {

	private String name;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "item_keyword", joinColumns = @JoinColumn(name = "id_item"), inverseJoinColumns = @JoinColumn(name = "id_keyword"))
	@ForeignKey(name = "Fk_item_keyword")
	private Set<Item> item = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Item> getItem() {
		return item;
	}

	public void setItem(Set<Item> item) {
		this.item = item;
	}
	
}
