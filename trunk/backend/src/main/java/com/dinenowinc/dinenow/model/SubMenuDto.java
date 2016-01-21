package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubMenuDto {
	private String id;
	private String menuName;
	private String subMenuDtoDescription;

	private ArrayList<CategoryDto> categories;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getSubMenuDtoDescription() {
		return subMenuDtoDescription;
	}
	public void setSubMenuDtoDescription(String description) {
		this.subMenuDtoDescription = description;
	}
	public ArrayList<CategoryDto> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<CategoryDto> categories) {
		this.categories = categories;
	}	
}

