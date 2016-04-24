package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import com.dinenowinc.dinenow.model.helpers.DeliveryZoneType;
import com.dinenowinc.dinenow.model.helpers.LatLng;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
@Audited
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({ @NamedQuery(name = "DeliveryZone.GetAll", query = "from DeliveryZone c") })

public class DeliveryZone extends BaseEntity {

  private String name;

  private String description;

  private double minimum;

  private double fee;

  private DeliveryZoneType type;

  private Polygon coordinates;

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

  public Polygon getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Polygon coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("name", this.getName());
    dto.put("description", this.getDescription());
    dto.put("minimum", this.getMinimum());
    dto.put("fee", this.getFee());
    dto.put("type", this.getType());
    List<LatLng> coords = new ArrayList<>();
    for (int i = 0; i < this.getCoordinates().getCoordinates().length; i++) {
      Coordinate coord = this.getCoordinates().getCoordinates()[i];
      LatLng latlng = new LatLng(coord.x, coord.y);
      coords.add(latlng);
    }
    dto.put("coordinates", coords);
    return dto;
  }

}
