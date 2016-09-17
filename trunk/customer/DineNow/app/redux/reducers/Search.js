'use-strict'

import {SEARCH_START, SEARCH_SUCCESS, SEARCH_RESULT_SET, SEARCH_RESULT_RESET} from "../actions/ActionTypes";

const initialState = {
    searching: false,
    searchSuccess: false,
    restaurantList: null,
};

function search(state = initialState, action) {
    switch (action.type) {
        case SEARCH_START:
            return {
                ...state,
                searching: true
            };
        case SEARCH_SUCCESS:
            return {
                ...state,
                searchSuccess: true
            };
        case SEARCH_RESULT_SET:
            return {
                ...state,
                restaurantList: action.result
            };
        case SEARCH_RESULT_RESET:
            return {
                ...state,
                restaurantList: null
            };
    }
    return state;
}

module.exports = search;
