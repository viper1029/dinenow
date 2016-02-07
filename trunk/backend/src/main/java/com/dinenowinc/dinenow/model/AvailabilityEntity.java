package com.dinenowinc.dinenow.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AvailabilityEntity extends BaseEntity {

  @Column(nullable = false)
  protected AvailabilityStatus availabilityStatus;

  public AvailabilityStatus getAvailabilityStatus() {
    return availabilityStatus;
  }

  public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
  }
}
