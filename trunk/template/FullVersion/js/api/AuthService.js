import {HOST, LOGIN_URL} from "../config/config";
const base64 = require('base-64');

var AuthService = {
    accountCallback: function (username, callback) {
        return function (error, response) {
            var data = UserService.parseAccount(response);
            console.log(response);
            var data = {
                username: username,
                accessToken: response.access_token,
                refreshToken: response.refresh_token,
            };

            callback(error, data);
        };
    },

    login: function (username, password, callback) {

        var headers = new Headers();
        headers.append("Authentication", "Basic " + base64.encode(username + ":" + password));
        headers.append("Accept", "application/json");

        console.log("HEADERS: ", headers);
        return fetch(HOST + LOGIN_URL, {
            method: "POST",
            headers: headers,
        })
            .then((resp) => resp.json())
            .then((data) => {
                console.log("receive data", data);
                if (data.errors.length > 0) {
                    console.log("ERROR", data.errors);
                    throw data.errors[0].message || '. Unable to login';
                }
                callback(null, data);
            })
            .catch((err) => {
                callback(err);
            })
            .done();
    }
};

export default AuthService;
