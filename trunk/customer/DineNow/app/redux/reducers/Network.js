'use-strict'

import {NETWORK_ERROR_RECEIVED, NETWORK_ERROR_CLEAR} from "../actions/ActionTypes";

const initialState = {
    networkError: false,
};

function network(state = initialState, action) {
    switch (action.type) {
        case NETWORK_ERROR_RECEIVED:
            return {
                ...state,
                networkError: true
            };
        case NETWORK_ERROR_CLEAR:
            return {
                ...state,
                networkError: false
            };
    }
    return state;
}

module.exports = network;
