
package com.dinenowinc.dinenow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "Category.GetAll", query = "from Category c") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "description" }, name = "category_uk")
)
public class Category extends BaseEntity {

  private static final long serialVersionUID = 691091407346434736L;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  public String getCategoryName() {
    return name;
  }

  public void setCategoryName(String categoryName) {
    this.name = categoryName;
  }

  public String getCategoryDescription() {
    return description;
  }

  public void setCategoryDescription(String description) {
    this.description = description;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("name", this.getCategoryName());
    dto.put("description", this.getCategoryDescription());
    return dto;
  }

}
