package com.dinenowinc.dinenow;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;



public class ApplicationConfiguration extends Configuration{
	
	@Valid
	@NotNull
	@JsonProperty
	private String port;
	
	@Valid
	@NotNull
	@JsonProperty
	private String host;
	
	@Valid
	@NotNull
	@JsonProperty
	private Integer timeResetKey;
	
	@Valid
	@NotNull
	@JsonProperty
	private Boolean isInitDb;
	
	
	public Boolean getIsInitDb() {
		return isInitDb;
	}


	public void setIsInitDb(Boolean isInitDb) {
		this.isInitDb = isInitDb;
	}


	public Integer getTimeResetKey() {
		return timeResetKey;
	}


	public void setTimeResetKey(Integer timeResetKey) {
		this.timeResetKey = timeResetKey;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}

	

}