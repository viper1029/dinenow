'use-strict'

import {AUTH_SIGN_IN_SUCCESS, AUTH_SIGN_IN_ERROR, AUTH_START_SIGN_IN, AUTH_END_SIGN_IN, AUTH_SIGN_IN_ERROR_RESET} from "../actions/ActionTypes";

const initialState = {
    username: null,
    signingIn: false,
    signedIn: false,
    signInError: null,
};

function auth(state = initialState, action) {
    switch (action.type) {
        case AUTH_START_SIGN_IN:
            return {
                ...state,
                signingIn: true
            };
        case AUTH_END_SIGN_IN:
            return {
                ...state,
                signingIn: false
            };
        case AUTH_SIGN_IN_ERROR:
            return {
                ...state,
                signInError: action.error
            };
        case AUTH_SIGN_IN_SUCCESS:
            return {
                ...state,
                signedIn: true
            }
        case AUTH_SIGN_IN_ERROR_RESET:
            return {
                ...state,
                signInError: null
            }
    }
    return state;
}

module.exports = auth;
