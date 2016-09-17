import {
    SEARCH_START,
SEARCH_SUCCESS, SEARCH_RESULT_SET, SEARCH_RESULT_RESET} from "./ActionTypes";
import {networkErrorReceived} from './NetworkActions'
import API from '../../api/Api'

this.api = API.create();

export function searchStart() {
    return {
        type: SEARCH_START,
    };
}

export function searchSuccess() {
    return {
        type: SEARCH_SUCCESS,
    };
}

export function searchResultSet(result) {
    return {
        type: SEARCH_RESULT_SET,
        result
    };
}

export function searchResultReset() {
    return {
        type: SEARCH_RESULT_RESET,
    };
}

export function search(longitude, latitude, radius) {
    return function (dispatch) {
        dispatch(searchStart());
        console.log("Search Started: ", longitude, latitude, radius);
        this.api.searchRestaurants(longitude, latitude, radius).then(
            (response) => {
                if(!response.ok && response.problem === 'NETWORK_ERROR') {
                    dispatch(networkErrorReceived());
                }
                else if (response.data.errors.length > 0) {
                    console.log("Error: ", response.data.errors);
                    dispatch(searchResultReset());
                    //dispatch(signInError(response.data.errors[0].message || 'Unable to login.'));
                }
                else {
                    console.log("SignIn Success");
                    dispatch(searchResultSet(response.data.data.restaurants));
                    //dispatch(setToken(username, data));
                    //dispatch(signInSuccess());
                }
                //dispatch(endSignIn())
                dispatch(searchSuccess());
                console.log(response);
            },
        );
    };
}
