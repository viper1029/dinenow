'use-strict'

import {RESTAURANT_DETAILS_REQUEST, RESTAURANT_DETAILS_SUCCESS, RESTAURANT_DETAILS_RESULT_SET, RESTAURANT_DETAILS_RESULT_RESET} from "../actions/ActionTypes";

const initialState = {
    requestSent: false,
    responseSuccess: false,
    details: null,
};

function restaurantDetails(state = initialState, action) {
    switch (action.type) {
        case RESTAURANT_DETAILS_REQUEST:
            return {
                ...state,
                requestSent: true
            };
        case RESTAURANT_DETAILS_SUCCESS:
            return {
                ...state,
                responseSuccess: true
            };
        case RESTAURANT_DETAILS_RESULT_SET:
            return {
                ...state,
                details: action.result
            };
        case RESTAURANT_DETAILS_RESULT_RESET:
            return {
                ...state,
                details: null
            };
    }
    return state;
}

module.exports = restaurantDetails;
