package com.dinenowinc.dinenow.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="promo_code_restaurant")
@Audited
@NamedQueries({@NamedQuery(name="PromoCodeRestaurant.GetAll", query = "from PromoCodeRestaurant pcr")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromoCodeRestaurant extends BaseEntity {

	
}
