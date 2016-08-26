import {
    AUTH_SET_TOKEN,
    AUTH_SIGN_IN_SUCCESS,
    AUTH_SIGN_IN_ERROR,
    AUTH_SIGN_IN_ERROR_RESET,
    AUTH_START_SIGN_IN,
    AUTH_END_SIGN_IN
} from "./ActionTypes";
import {networkErrorReceived} from './NetworkActions'
import API from '../../api/Api'

this.api = API.create();

export function setToken(username, token) {
    return {
        type: AUTH_SET_TOKEN,
        username,
        token,
    };
}

export function signInError(error) {
    return {
        type: AUTH_SIGN_IN_ERROR,
        error
    };
}

export function signInErrorReset() {
    return {
        type: AUTH_SIGN_IN_ERROR_RESET,
        error: null
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
        this.api.signIn(username, password).then(
            (response) => {
                if(!response.ok && response.problem === 'NETWORK_ERROR') {
                    dispatch(networkErrorReceived());
                }
                else if (response.data.errors.length > 0) {
                    console.log("Error: ", response.data.errors);
                    dispatch(signInError(response.data.errors[0].message || 'Unable to login.'));
                }
                else {
                    console.log("SignIn Success");
                    dispatch(setToken(username, data));
                    dispatch(signInSuccess());
                }
                dispatch(endSignIn())
            },
        );
    };
}
