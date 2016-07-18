/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { Image , StyleSheet } from 'react-native';
import {popRoute} from '../../actions/route';
import {pushNewRoute, replaceRoute} from '../../actions/route';

import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View } from 'native-base';

import theme from '../../themes/base-theme';

import Button from 'apsl-react-native-button';
//import {fetchAsJson} from './../../network/CommonUtils';
import styles from './styles';


function login1(email, pass) {
  var params =  {
    method: 'POST',
    headers: {
      'Authentication': 'Basic YWRtaW5AYWRtaW4uY29tOjEyMzQ1Njc4OT=',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      email: email,
      password: pass
    })
  }
  fetch('http://192.168.1.180:30505/api/v1/auth/login', params)
    .then(function(response){
      var message = response.json();
      return message;

     // return response.json();
    }).then((message) => {
    if(message){
      // failed
      console.warn(message);
    }else {
      //passed
      console.warn('passed');
    }
    console.warn('response' + response);
    }

  ).catch((error) => {
        console.warn(error);
        return error;
      })
}

function login(email, pass) {
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
  fetch('http://192.75.243.53:30505/api/v1/auth/login', params)
    .then(function(response) {
      // this.props.navigator.immediatelyResetRouteStack([{ name: 'search'}]);
      return response.json();
    })
    .catch((error) => {
      console.warn(error);
      return error;
    });
}

function fetchAsJson(url, params) {
  return fetch(url, params)
    .then(function(response) {
      return response.json();
    });
}

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = { password: '',
                       email : ''
         };
    }

    popRoute() {
        this.props.popRoute();
    }

  replaceRoute(route) {
    this.props.replaceRoute(route);
  }


  render() {
        return (
            <Container theme={theme} style={{backgroundColor:'#384850'}}>
              <Image source={require('../../../images/glow2.png')} style={styles.container} >
                <Header>
                    <Button transparent onPress={() => this.popRoute()}>
                        <Icon name="ios-arrow-back" />
                    </Button>
                    <Title>Login</Title>
                </Header>

                <Content padder style={{backgroundColor: 'transparent'}}>
                    <View padder>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-mail-open-outline" />
                            <Input placeholder="Email" 
                            value={this.state.email}
                             onChangeText={(email) => this.setState({email})}
                               />
                        </InputGroup>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-unlock-outline" />
                            <Input
                                placeholder="Password"
                                secureTextEntry={true}
                                value={this.state.password}
                                onChangeText={(password) => this.setState({password})}
                            />
                        </InputGroup>
                        <Button rounded block onPress={() => this.login()} style={{backgroundColor: '#fff', marginTop: 20}} textStyle={{color: '#00c497'}}>
                            Login
                        </Button>
                    </View>
                </Content>
              </Image>
            </Container>
        )
    }

    login() {
      var  email = this.state.email;
      var pass = this.state.password;
      var response = login1('1','1', this.replaceRoute('signUp'));
       //this.replaceRoute('home');
     // this.replaceRoute('signUp');

      //console.warn('test' + response);
    }
}

function bindAction(dispatch) {
    return {
        popRoute: () => dispatch(popRoute()),
        replaceRoute: () => dispatch(replaceRoute())
    }
}

export default connect(null, bindAction)(Login);
