package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
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
@NamedQueries({ @NamedQuery(name = "Keyword.GetAll", query = "from Keyword t") })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "keyword" }, name = "keyword_uk")
)
public class Keyword extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String keyword;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(final String keyword) {
    this.keyword = keyword;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getId());
    dto.put("keyword", this.getKeyword());
    return dto;
  }
}
