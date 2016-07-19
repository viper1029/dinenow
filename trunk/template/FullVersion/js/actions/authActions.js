import { AUTH_SET_TOKEN, AUTH_LOGIN_ERROR, AUTH_SET_INFO, AUTH_REMOVE_TOKEN, REGISTRATION_SET_TOKEN} from './actionTypes';

var AuthService = require('../api/AuthService');

export function setToken(username,token) {
  return {
    type: AUTH_SET_TOKEN,
    username,
    token,
  };
}

export function removeToken() {
  return {
    type: AUTH_REMOVE_TOKEN,
  };
}

export function loginError(error) {
  return {
    type: AUTH_LOGIN_ERROR,
    error
  };
}

export function setInfo(username) {
  return {
    type: AUTH_SET_INFO,
    username,
  };
}

export function setRegistrationToken(token) {
  return {
    type: REGISTRATION_SET_TOKEN,
    token
  };
}


export function verifyCredential(username,password){
  return function (dispatch) {
    console.log("Login Started: ", username);
    dispatch(setInfo(username));

    AuthService.login(username, password, function(error,data){
      if (error){
        console.log("Error: ", error);
        dispatch(loginError(error));
      } else {
        console.log("Login Success");
        dispatch(setToken(username,data));
      }

    });

    // get User Roles Info
    // Register Device to server

  };
}
