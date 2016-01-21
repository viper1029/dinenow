package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private ArrayList<String> items = new ArrayList<String>();
	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}	
}

