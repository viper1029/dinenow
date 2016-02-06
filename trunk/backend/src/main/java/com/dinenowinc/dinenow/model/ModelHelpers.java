package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelHelpers {	
	
	public static List<HashMap<String, Object>> fromEntities(List<? extends BaseEntity> entities)	{
		List<HashMap<String, Object>> returnMapList = new ArrayList<HashMap<String, Object>>();
		for (BaseEntity entity : entities) {
			returnMapList.add(entity.toDto());
		}
		return returnMapList;
	}
}
