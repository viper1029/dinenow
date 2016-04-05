var React = require('react-native');
var {
  View,
  Text,
  StyleSheet,
  TextInput
} = React;

var Button = require('../../components/common/button');
var fetchAsJson = require('../../utils/fetchAsJson');

module.exports = React.createClass({
  getInitialState: function() {
    return {
      userId: '',
      userName: '',
      firstName: '',
      lastName: '',
      password: ''
    };
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Text>Sign In</Text>

        <Text style={styles.label}>Username:</Text>
        <TextInput
          style={styles.input}
          value={this.state.username}
          onChangeText={(text) => this.setState({userName: text})}
          />

        <Text style={styles.label}>Password:</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.input}
          value={this.state.password}
          onChangeText={(text) => this.setState({password: text})}
          />
        <Button text={'Sign In'} onPress={this.onSignInPress} />
        <Button text={'Sign Up'} onPress={this.onSignUpPress} />
      </View>
    );
  },
  onSignInPress: function(){
    this.logIn();
  },
  onSignUpPress(){
    this.props.navigator.push({name: 'signup'});
  },
  logIn: function() {
  var params =  {
  method: 'POST',
  headers: {
    'Authentication': 'Basic YWRtaW5AYWRtaW4uY29tOjEyMzQ1Njc4OTA=',
    'Content-Type': 'application/json',
  }
};
    var json = fetchAsJson('http://192.168.1.120:30505/api/v1/auth/login', params)
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
});

var styles = StyleSheet.create({
  container : {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  input: {
    padding: 4,
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 5,
    margin: 5,
    width: 200,
    alignSelf: 'center'
  },
  label: {
    fontSize: 18
  }
});
