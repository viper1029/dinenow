import {
    RESTAURANT_DETAILS_REQUEST,
    RESTAURANT_DETAILS_SUCCESS,
    RESTAURANT_DETAILS_RESULT_SET,
    RESTAURANT_DETAILS_RESULT_RESET} from "./ActionTypes";
import {networkErrorReceived} from './NetworkActions'
import API from '../../api/Api'

this.api = API.create();

export function restaurantDetailsRequest() {
    return {
        type: RESTAURANT_DETAILS_REQUEST,
    };
}

export function restaurantDetailsSuccess() {
    return {
        type: RESTAURANT_DETAILS_SUCCESS,
    };
}

export function restaurantDetailsSet(result) {
    return {
        type: RESTAURANT_DETAILS_RESULT_SET,
        result
    };
}

export function restaurantDetailsReset() {
    return {
        type: RESTAURANT_DETAILS_RESULT_RESET,
    };
}

export function getRestaurantDetails(restaurantId) {
    return function (dispatch) {
        dispatch(restaurantDetailsRequest());
        console.log("Getting restaurant details: ", restaurantId);
        this.api.getRestaurantDetails(restaurantId).then(
            (response) => {
                if(!response.ok && response.problem === 'NETWORK_ERROR') {
                    dispatch(networkErrorReceived());
                }
                else if (response.data.errors.length > 0) {
                    console.log("Error: ", response.data.errors);
                    dispatch(restaurantDetailsReset());
                    //dispatch(signInError(response.data.errors[0].message || 'Unable to login.'));
                }
                else {
                    dispatch(restaurantDetailsSet(response.data.data));
                    //dispatch(setToken(username, data));
                    //dispatch(signInSuccess());
                }
                //dispatch(endSignIn())
                dispatch(restaurantDetailsSuccess());
                console.log(response);
            },
        );
    };
}
