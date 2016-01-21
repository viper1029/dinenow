package com.dinenowinc.dinenow.model;

import java.util.ArrayList;

public class MenuDto {
	private String id;
	private String menuName;
	private String menuDtoDescription;

	private ArrayList<SubMenuDto> subMenus;

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

	public String getMenuDtoDescription() {
		return menuDtoDescription;
	}
	public void setMenuDtoDescription(String description) {
		this.menuDtoDescription = description;
	}

	public ArrayList<SubMenuDto> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(ArrayList<SubMenuDto> menus) {
		this.subMenus = menus;
	}
	
}

