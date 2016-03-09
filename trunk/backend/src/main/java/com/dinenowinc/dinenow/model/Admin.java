package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ @NamedQuery(name = "Admin.GetAll", query = "from Admin u") })
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = { "email" }, name = "admin_uk")
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin extends BaseEntity {

  private static final long serialVersionUID = 2228118945733901398L;

  @Column(updatable = false, nullable = false, unique = true)
  private String email;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @NotNull
  @JsonIgnore
  private String password;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public HashMap<String, Object> toDto() {
    HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
    dto.put("id", this.getId());
    dto.put("email", this.getEmail());
    dto.put("firstName", this.getFirstName());
    dto.put("lastName", this.getLastName());
    return dto;
  }
}
