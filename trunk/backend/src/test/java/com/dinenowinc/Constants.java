package com.dinenowinc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

public class Constants {

  public final static String ROOT_API_URL = "http://localhost/api/v1";
  public final static int API_PORT = 30505;

  public final static String ADMIN_EMAIL = "admin@gmail.com";
  public final static String ADMIN_PASSWORD = "12345678";

  public final static String OWNER_EMAIL = "123@gmail.com";
  public final static String OWNER_PASSWORD = "12345678";

  public final static String CUSTOMER_EMAIL = "1234@gmail.com";
  public final static String CUSTOMER_PASSWORD = "12345678";

  public final static String LOGIN_URL = ROOT_API_URL + "/auth/login";

}