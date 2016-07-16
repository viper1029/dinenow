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

import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View } from 'native-base';

import theme from '../../themes/base-theme';

import Button from 'apsl-react-native-button';

import styles from './styles';

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
      console.log(email+ " " + pass);

    }
}

function bindAction(dispatch) {
    return {
        popRoute: () => dispatch(popRoute())
    }
}

export default connect(null, bindAction)(Login);
