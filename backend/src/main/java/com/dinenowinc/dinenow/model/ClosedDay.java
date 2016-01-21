package com.dinenowinc.dinenow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@Table(name="closed_holiday")
@NamedQueries({@NamedQuery(name="ClosedDay.GetAll", query = "from ClosedDay r where status=0")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClosedDay extends BaseEntity {

	@Column(name="holiday_date" , nullable=false)
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name="holiday_description" , nullable=false)
	private String description;

	
	
	public ClosedDay() {
		super();
		super.setCreatedBy("auto");
		super.setCreatedDate(new Date());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ClosedDay [date=" + date + ", description=" + description + "]";
	}
	
	
}
