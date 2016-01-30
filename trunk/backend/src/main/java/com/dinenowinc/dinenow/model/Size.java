package com.dinenowinc.dinenow.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Audited
@NamedQueries({@NamedQuery(name="Size.GetAll", query = "from Size s")})
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "description"}, name="size_uk")
)
public class Size extends BaseEntity{

	private static final long serialVersionUID = -524833031492135630L;
	
	@Column(nullable=false,unique=false)
	private String name;

	@Column(nullable=false,unique=false)
	private String description;
	
	@Column(nullable=false,unique=true)
	private Integer compositeId;


	public Size() {
		//this.setCompositeId();
	}

	public String getSizeName() {
		return name;
	}

	public void setSizeName(String sizeName) {
		this.name = sizeName;
	}

	public String getSizeDescription() {
		return description;
	}

	public void setSizeDescription(String sizeDescription) {
		this.description = sizeDescription;
	}
	
	
		
	public Integer getCompositeId() {
		return compositeId;
	}

	public void setCompositeId(String resturantId) {
		final int prime = 31;
        int result = 1;
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((resturantId == null) ? 0 : resturantId.hashCode());
		this.compositeId = result;
	}

/*	   @Override
	    public boolean equals(final Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        final Size other = (Size) obj;
	        if (name == null) {
	            if (other.name != null)
	                return false;
	        } else if (!name.equals(other.name))
	            return false;
	        return true;
	    }*/
	   
	@Override
	public HashMap<String, Object> toDto() {
		HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
		dto.put("id", this.getId());
		dto.put("name", this.getSizeName());
		dto.put("description", this.getSizeDescription());		
		return dto;
	}
	
}
