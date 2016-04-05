var React = require('react-native');
var {
  View,
  Text,
  StyleSheet,
  TextInput
} = React;

var Button = require('../../components/common/button');
var fetchAsJson = require('../../utils/fetchAsJson');

var { createIconSetFromFontello } = require('react-native-vector-icons');
var fontelloConfig = require('../../assets/config.json');
var Icon = createIconSetFromFontello(fontelloConfig);

module.exports = React.createClass({
  getInitialState: function() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      phoneNumber: ''
    };
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Text>Sign Up</Text>
        <Icon name="close" size={30} color="#4F8EF7" />
        <Text style={styles.label}>First name:</Text>
        <TextInput
          style={styles.input}
          value={this.state.firstName}
          onChangeText={(text) => this.setState({firstName: text})}
          />

        <Text style={styles.label}>Last name:</Text>
        <TextInput
          style={styles.input}
          value={this.state.lastName}
          onChangeText={(text) => this.setState({lastName: text})}
          />

        <Text style={styles.label}>Email:</Text>
        <TextInput
          style={styles.input}
          value={this.state.email}
          onChangeText={(text) => this.setState({email: text})}
        />

        <Text style={styles.label}>Password:</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.input}
          value={this.state.password}
          onChangeText={(text) => this.setState({password: text})}
          />

        <Text style={styles.label}>Phone number:</Text>
        <TextInput
          style={styles.input}
          value={this.state.phoneNumber}
          onChangeText={(text) => this.setState({phoneNumber: text})}
        />
        <Button text={'Sign Up'} onPress={this.onSignUpPress} />
        <Button text={'Back'} onPress={this.onBackPress} />
      </View>
    );
  },
  onBackPress: function() {
    this.props.navigator.pop();
  },
  onSignUpPress: function() {
    var params =  {
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
      .then(function(json) {
        this.setState({
          userId : json.data.user.id,
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
