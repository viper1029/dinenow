import {fetchAsJson} from '../Util/CommonUtils';

export function login() {
  var params =  {
    method: 'POST',
    headers: {
      'Authentication': 'Basic YWRtaW5AYWRtaW4uY29tOjEyMzQ1Njc4OTA=',
      'Content-Type': 'application/json',
    }
  };
  var json = FetchAsJson('http://192.168.1.120:30505/api/v1/auth/login', params)
    .then(function(json) {
      this.setState({
        userId : json.user.id,
        firstName : json.user.firstName,
        lastName : json.user.lastName
      });
      this.props.navigator.immediatelyResetRouteStack([{ name: 'search'}]);
    })
    .catch((error) => {
      console.warn(error);
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
  var json = FetchAsJson('http://192.168.1.120:30505/api/v1/auth/register', params)
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