/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
import { bindActionCreators } from 'redux';
// import CodePush from 'react-native-code-push';
import { Image , StyleSheet, Platform } from 'react-native';
import {popRoute} from '../../actions/route';
import {pushNewRoute, replaceRoute} from '../../actions/route';
import { verifyCredential, removeToken } from '../../actions/authActions'

import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View } from 'native-base';

import theme from '../../themes/base-theme';

import Button from 'apsl-react-native-button';
//import {fetchAsJson} from './../../network/CommonUtils';
import styles from './styles';

import * as AuthActions from '../../actions/authActions'

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            email: ''
         };
    }

    popRoute() {
        this.props.popRoute();
    }

  replaceRoute(route) {
    this.props.replaceRoute(route);
  }

  // added for testing
  _login() {
  if (Platform.OS === 'android') {
    //dismissKeyboard();
  }
  console.log("Login using ",this.state.email);
    this.props.verifyCredential(this.state.username, this.state.password);
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
                        <Button rounded block onPress={() => this._login()} style={{backgroundColor: '#fff', marginTop: 20}} textStyle={{color: '#00c497'}}>
                            Login
                        </Button>
                    </View>
                </Content>
              </Image>
            </Container>
        )
    }
}

function bindAction(dispatch) {
    return {
        popRoute: () => dispatch(popRoute()),
        replaceRoute: () => dispatch(replaceRoute()),
    }
}

var mapStateToProps = function(state) {
  var login = false;
  //console.log('mapStateToProps', state.getIn(['currentUser','accessToken']));
  //if (state.getIn(['currentUser','accessToken'])){
//    login = true;
//  }


  return {
    //isLoggedIn: login,
    //loginError: state.getIn(['email','error']),
    //isLoading: state.getIn(['currentUser','isLoading']),
  };
};

var mapDispatchToProps = function (dispatch) {
  return bindActionCreators({
    verifyCredential,
    removeToken,
  }, dispatch);

};

module.exports = connect(mapStateToProps, mapDispatchToProps)(Login);
//export default connect(null, bindAction)(Login);
