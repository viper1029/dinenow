package com.dinenowinc.dinenow.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;


@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -4330728962247937946L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id", updatable = false, nullable = false)
  private String id = null;

  @JsonIgnore
  @Column(nullable = false)
  private Date created_date;

  @JsonIgnore
  private Date modified_date;

  @JsonIgnore
  @Column(nullable = false)
  private String created_by;

  @JsonIgnore
  private String modified_by;

  @JsonIgnore
  private int status;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreatedDate() {
    DateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
    return dateFormat.format(this.created_date);
  }

  public void setCreatedDate(Date createdDate) {
    this.created_date = createdDate;
  }

  public Date getModifiedDate() {
    return modified_date;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modified_date = modifiedDate;
  }

  public String getCreatedBy() {
    return created_by;
  }

  public void setCreatedBy(String createdBy) {
    this.created_by = createdBy;
  }

  public String getModifiedBy() {
    return modified_by;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modified_by = modifiedBy;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BaseEntity)) {
      return false;
    }
    final BaseEntity other = (BaseEntity) obj;
    if (this.id != null && other.id != null) {
      if (this.getClass().equals(other.getClass()) && this.id == other.id) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public HashMap<String, Object> toDto() {
    throw new NotImplementedException("toDto not yet implemented");
  }
}
