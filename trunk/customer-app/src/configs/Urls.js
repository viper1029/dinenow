var API_URL = "http://192.168.1.120:30505/api/v1/";

export const Endpoints = {
  LOGIN: API_URL + "auth/login",
  REGISTER: API_URL + "auth/register"
};

export const HttpHeaders = {
  'Accept': 'application/json',
  'Content-Type': 'application/json'
};