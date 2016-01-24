package com.dinenowinc.dinenow.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.UUID;

public class User implements Principal {

  private static final long serialVersionUID = -1936855298814425297L;
  private UUID id;
  private UserRole role;
  private String name;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
