import {
    NETWORK_ERROR_RECEIVED,
    NETWORK_ERROR_CLEAR,
} from "./ActionTypes";

export function networkErrorReceived() {
    return {
        type: NETWORK_ERROR_RECEIVED,
    };
}

export function networkErrorClear() {
    return {
        type: NETWORK_ERROR_CLEAR,
    };
}
