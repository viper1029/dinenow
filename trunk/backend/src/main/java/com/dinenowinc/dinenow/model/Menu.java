package com.dinenowinc.dinenow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Menu.GetAll", query = "from Menu m") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends BaseEntity {

  private static final long serialVersionUID = -977770296392567663L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String notes;

  @Column
  private String hours;

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private final Set<CategoryItem> categories = new HashSet<>();

  public boolean isShowMenu() {
    for (Hour hour : getHours()) {
      Date date_now = new Date();
      if (hour.getFromTime().compareTo(date_now) < 0
          && hour.getToTime().compareTo(date_now) > 0) {
        return true;
      }
    }
    return false;
  }

  public String getMenuName() {
    return name;
  }

  public void setMenuName(String menuName) {
    this.name = menuName;
  }

  public String getMenuDescription() {
    return description;
  }

  public void setMenuDescription(String menuDescription) {
    this.description = menuDescription;
  }

  public Set<CategoryItem> getCategories() {
    return categories;
  }

  public void addCategory(CategoryItem category) {
    getCategories().add(category);
  }

  public void addAllCategory(ArrayList<CategoryItem> categorys) {
    getCategories().clear();
    getCategories().addAll(categorys);
  }

  public List<Hour> getHours() {
    try {
      if (this.hours == null) {
        this.hours = "[]";
      }
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd'T'HH:mm:ss");
      // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      return (mapper.readValue(this.hours,
          new TypeReference<List<Hour>>() {
          }));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<Hour>();
  }

  public void setHours(List<Hour> hours) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      DateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd'T'HH:mm:ss");
      // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      mapper.setDateFormat(dateFormat);
      this.hours = mapper.writeValueAsString(hours);
    }
    catch (IOException e) {
      this.hours = "";
      e.printStackTrace();
    }
  }

  public void addHour(Hour hour) {
    this.getHours().add(hour);
  }

  @Override
  public HashMap<String, Object> toDto() {
    LinkedHashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getMenuName());
    dto.put("description", this.getMenuDescription());
    dto.put("hours", this.getHours());
    return dto;
  }
}
