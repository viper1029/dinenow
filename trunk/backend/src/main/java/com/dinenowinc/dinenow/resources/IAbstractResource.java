package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public interface IAbstractResource {
  Response getAll(@Auth User access);

  Response get(@Auth User access, @PathParam("id") String id);

  Response add(@Auth User access, HashMap<String, Object> dto);

  Response update(@Auth User access, @PathParam("id") String id, HashMap<String, Object> dto);

  Response delete(@Auth User access, @PathParam("id") String id);
}
