package com.dinenowinc.dinenow.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class PaymentMethod implements Serializable {

  @JsonProperty("name")
  private String mName;

  @JsonProperty("cardStripe")
  private String mCardStripe;

  @JsonProperty("last4")
  private String mLast4;

  public PaymentMethod() {
  }

  public PaymentMethod(String name, String card, String last4) {
    this.mName = name;
    this.mCardStripe = card;
    this.mLast4 = last4;
  }

  public String getName() {
    return mName;
  }

  public void setName(String mName) {
    this.mName = mName;
  }

  public String getCardStripe() {
    return mCardStripe;
  }

  public void setCardStripe(String card) {
    this.mCardStripe = card;
  }

  public String getLast4() {
    return mLast4;
  }

  public void setLast4(String mLast4) {
    this.mLast4 = mLast4;
  }
}
