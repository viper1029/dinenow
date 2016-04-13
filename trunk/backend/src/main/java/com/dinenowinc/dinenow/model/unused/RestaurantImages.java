/*
package com.dinenowinc.dinenow.model.unused;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="restaurant_image")
@Audited
@NamedQueries({@NamedQuery(name="RestaurantImages.GetAll", query = "from RestaurantImages ri")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantImages implements Serializable{

	@Id @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id = null;
	
	@Id
	@GeneratedValue
	@Column(name="thumb_image",nullable=false)
	private String thumbImages;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbImages() {
		return thumbImages;
	}

	public void setThumbImages(String thumbImages) {
		this.thumbImages = thumbImages;
	}
	
}
*/
