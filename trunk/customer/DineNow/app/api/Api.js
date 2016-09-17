import apisauce from 'apisauce'
import base64 from 'base-64';
import {networkErrorReceived} from '../redux/actions/NetworkActions';

var handleNetworkError = function(response) {
    console.log(response);
    if(!response.ok && response.problem === 'NETWORK_ERROR') {
            dispatch(networkErrorReceived);
    }
    return response;
};

const create = (baseURL = 'http://192.168.1.180:30505/api/v1') => {
    const api = apisauce.create({
        baseURL,
        timeout: 5000
    });

    const signIn = (username, password) => api.post('/auth/login',
        {userName: username, password: password},
        {headers: {'Authentication': "Basic " + base64.encode(username + ":" + password)}});

    const searchRestaurants = (longitude, latitude, radius) => api.get('/restaurants/searchByDistance', {distance: radius, location: longitude + ',' +latitude});

    return {
        signIn,
        searchRestaurants,
    }
};

export default{
    create
};