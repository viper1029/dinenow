import {fetchAsJson} from './CommonUtils';

export function login(email, pass) {
  var params =  {
    method: 'POST',
    headers: {
      'asdfasf': 'Basic YWRtaW5AYWRtaW4uY29tOjEyMzQ1Njc4OTA=',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      email: email,
      password: pass
    })
  }
  var json = fetchAsJson('http://localhost:30505/api/v1/auth/login', params)
    .then(function(json) {
      this.setState({
        userId : json.user.id,
        firstName : json.user.firstName,
        lastName : json.user.lastName
      });
     // this.props.navigator.immediatelyResetRouteStack([{ name: 'search'}]);
     return json;
    })
    .catch((error) => {
      console.warn(error);
      return error;
    });
}

export function register() {
  var params = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      firstName: this.state.firstName,
      lastName: this.state.lastName,
      email: this.state.email,
      password: this.state.password,
      phoneNumber: this.state.phoneNumber
    })
  };
  var json = fetchAsJson('http://192.168.1.120:30505/api/v1/auth/register', params)
    .then(function (json) {
      this.setState({
        userId: json.data.user.id,
        firstName: json.user.firstName,
        lastName: json.user.lastName
      });
      this.props.navigator.immediatelyResetRouteStack([{name: 'search'}]);
    })
    .catch((error) => {
      console.warn(error);
    });
}