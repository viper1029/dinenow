package com.dinenowinc.dinenow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

@Entity
//@Audited
@NamedQueries({@NamedQuery(name="DeliveryZone.GetAll", query = "from DeliveryZone c")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryZone extends BaseEntity{

	private String name;
	private String description;
	private double minimum;
	private double fee;
	private DeliveryZoneType type;
	
	@Lob
	//@Type(type = "org.hibernate.spatial.GeometryType")
	private Polygon coordinates;
	
/*	@Column(nullable=false,columnDefinition="Decimal(10,6)")
	private double lat;
	
	@Column(nullable=false,columnDefinition="Decimal(10,6)")
	private double lng;*/
	
	
	public DeliveryZone() {
	}
	
	public DeliveryZone(String name, String description, double minimum, double fee,DeliveryZoneType type,  Polygon po,String createdBy,Date createdDate) {
		this.name = name;
		this.description = description;
		this.minimum = minimum;
		this.fee = fee;
		this.type = type;
		this.coordinates = po;
		this.setCreatedBy(createdBy);
		this.setCreatedDate(createdDate);
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public DeliveryZoneType getType() {
		return type;
	}

	public void setType(DeliveryZoneType type) {
		this.type = type;
	}
	
	/*public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}*/
	
	//@Type(type = "org.hibernate.spatial.GeometryType")
	public Polygon getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Polygon coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getName());
		dto.put("description", this.getDescription());
		dto.put("minimum", this.getMinimum());
		dto.put("fee", this.getFee());
		dto.put("type", this.getType());
		List<LatLng> coords = new ArrayList<LatLng>();
		for (int i = 0; i < this.getCoordinates().getCoordinates().length; i++) {
			Coordinate  coord = this.getCoordinates().getCoordinates()[i];
			LatLng latlng = new LatLng(coord.x, coord.y);
			coords.add(latlng);
		}
		dto.put("coordinates", coords);
		return dto;
	}
	
}
