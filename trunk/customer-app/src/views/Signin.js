import React, {Component} from 'react';
import {View, Text, StyleSheet, TextInput} from 'react-native';
import Button from 'apsl-react-native-button';
import {Actions} from 'react-native-router-flux'

export default class SignIn extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: '',
      userName: '',
      firstName: '',
      lastName: '',
      password: ''
    }
  }

  render() {
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
        <Button onPress={this.onSignInPress}>Sign In</Button>
        <Button onPress={this.onSignUpPress}>Sign Up</Button>
      </View>
    );
  }

  onSignInPress(){
    this.logIn();
  }

  onSignUpPress(){
    this.props.navigator.push({name: 'signup'});
  }
};

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
