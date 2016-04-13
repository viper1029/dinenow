package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.dao.VersionDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.Version;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Path("/version")
@Api("/version")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VersionResource extends AbstractResource<Version> {

  @Inject
  private VersionDao versionDao;


  @GET
  @Path("/{platform}/{clientVersion}")
  @ApiOperation("get version information")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "data"),
      @ApiResponse(code = 404, message = "Cannot found entity"),
      @ApiResponse(code = 401, message = "Access denied for user")
  })
  public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("clientVersion") String clientVersion, @PathParam("platform") String platform) {
    List<Version> entities = this.dao.getAll();
    for(Version version : entities) {
      if(version.getVersion().equalsIgnoreCase(clientVersion)) {
        HashMap<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("forceUpdate", version.getForceUpdate());
        dto.put("updateUrl", version.getUpdateUrl());
        ResourceUtils.asSuccessResponse(Status.OK, dto);
        break;
      }
    }
    return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, new ServiceErrorMessage("Error processing request."));
  }
}