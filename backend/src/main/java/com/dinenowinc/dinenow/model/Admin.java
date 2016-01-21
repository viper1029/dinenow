package com.dinenowinc.dinenow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Admin.GetAll", query = "from Admin u")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin extends BaseEntity{
	
	private static final long serialVersionUID = 2228118945733901398L;
	
	@Column(updatable = false, nullable = false,unique=true)
	private String email;
	@Column(nullable=false)
	private String first_name;
	@Column(nullable=false)
	private String last_name;
	@NotNull
	@JsonIgnore
	private String password;
	

//	public int getNumberOfOrders() {
//		return numberOfOrders;
//	}

//	public void setNumberOfOrders(int numberOfOrders) {
//		this.numberOfOrders = numberOfOrders;
//	}
	
	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("email", this.getEmail());
		dto.put("firstName", this.getFirstName());
		dto.put("lastName", this.getLastName());
		return dto;
	}
	
	/*public Admin(String firstname, String lastname, String email, String password, String created_by){
		this.first_name = firstname;
		this.last_name = lastname;
		this.email= email;
		this.password= password;
		this.setCreatedBy(created_by);
	
		
	}*/
}
