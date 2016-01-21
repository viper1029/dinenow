package com.dinenowinc.dinenow.resources;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {
	@GET
	@Timed
	public String pong() {
		return "{\"answer\": \""+new DateTime()+"\"}";
	}
	
	@GET
	@Timed
	@Path("/auth")
	public String pongAuthenticated(@Auth Long userId) {
		return String.format("{\"answer\": \"authenticated pong for user\"}");
	}
}
