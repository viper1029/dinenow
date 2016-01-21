/*
package com.dinenowinc.resources.SecuredResource;

import com.dinenowinc.testing.ContainerPerClassTest;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.annotations.Test;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class AdminLoginTest extends ContainerPerClassTest {

  //@Test
  public void validLogin() {
    final Response response = target().path("auth/login").request()
        .header("Authentication", "Basic YWRtaW5AYWRtaW4uY29tOmFkbWluQDEyMw==")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .post(null);

    JSONObject jsonResponse = (JSONObject) JSONValue.parse(response.readEntity(String.class));
    JSONObject data = (JSONObject) jsonResponse.get("data");
    JSONObject user = (JSONObject) data.get("user");
    String access_token = (String) data.get("access_token");
    String id = (String) user.get("id");
    String email = (String) user.get("email");
    String lastName = (String) user.get("lastName");
    String firstName = (String) user.get("firstName");
    String errorMessage = (String) jsonResponse.get("errorMessage");

    assertEquals(response.getStatus(), 200);
    assertEquals(response.getHeaderString(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);
    assertNotNull(id);
    assertNotNull(email);
    assertNotNull(lastName);
    assertNotNull(firstName);
    assertNotNull(data);
    assertNotNull(access_token);
    assertNull(errorMessage);
  }

  //@Test
  public void missingAuthenticationHeader() {
    final Response response = target().path("auth/login").request()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .post(null);

    JSONObject jsonResponse = (JSONObject) JSONValue.parse(response.readEntity(String.class));
    JSONObject data = (JSONObject) jsonResponse.get("data");
    JSONObject user = (JSONObject) data.get("user");
    String access_token = (String) data.get("access_token");
    String id = (String) user.get("id");
    String email = (String) user.get("email");
    String lastName = (String) user.get("lastName");
    String firstName = (String) user.get("firstName");
    String errorMessage = (String) jsonResponse.get("errorMessage");

    assertEquals(response.getStatus(), 200);
    assertEquals(response.getHeaderString(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);
    assertNotNull(id);
    assertNotNull(email);
    assertNotNull(lastName);
    assertNotNull(firstName);
    assertNotNull(data);
    assertNotNull(access_token);
    assertNull(errorMessage);
  }
}
*/
