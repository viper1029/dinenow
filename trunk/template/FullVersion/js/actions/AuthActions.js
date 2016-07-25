import {
    AUTH_SET_TOKEN,
    AUTH_SIGN_IN_SUCCESS,
    AUTH_SIGN_IN_ERROR,
    AUTH_START_SIGN_IN,
    AUTH_END_SIGN_IN
} from "./ActionTypes";
import AuthService from "../api/AuthService";

export function setToken(username, token) {
    return {
        type: AUTH_SET_TOKEN,
        username,
        token,
    };
}

export function loginError(error) {
    return {
        type: AUTH_SIGN_IN_ERROR,
        error
    };
}

export function signInSuccess() {
    return {
        type: AUTH_SIGN_IN_SUCCESS,
    };
}

export function startSignIn() {
    return {
        type: AUTH_START_SIGN_IN,
    };
}

export function endSignIn() {
    return {
        type: AUTH_END_SIGN_IN,
    };
}


export function verifyCredential(username, password) {
    return function (dispatch) {
        dispatch(startSignIn());
        console.log("SignIn Started: ", username);
        AuthService.login(username, password, function (error, data) {
            if (error) {
                console.log("Error: ", error);
                dispatch(loginError(error));
            } else {
                console.log("SignIn Success");
                dispatch(setToken(username, data));
                dispatch(signInSuccess());
            }
            dispatch(endSignIn());
        });
    };
}
