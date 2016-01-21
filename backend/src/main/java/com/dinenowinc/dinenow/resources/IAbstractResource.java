package com.dinenowinc.dinenow.resources;

import java.util.HashMap;

import io.dropwizard.auth.Auth;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.dinenowinc.dinenow.model.AccessToken;

public interface IAbstractResource {
	Response getAll(@Auth AccessToken access);
	Response get(@Auth AccessToken access,@PathParam("id") String id);
	Response add(@Auth AccessToken access, HashMap<String, Object> dto);
	Response update(@Auth AccessToken access, @PathParam("id") String id, HashMap<String, Object> dto);
	Response delete(@Auth AccessToken access, @PathParam("id") String id);
}
