/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { Image } from 'react-native';
import {popRoute} from '../../actions/route';

import {Container, Header, Title, Content, Text, Button, Icon, InputGroup, Input, View } from 'native-base';

import theme from '../../themes/base-theme';
import styles from './styles';

class SignUp extends Component {

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
                    <Title>SignUp</Title>
                </Header>

                <Content padder style={{backgroundColor: 'transparent'}}>
                    <View padder>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-person" />
                            <Input placeholder="Name" />
                        </InputGroup>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-mail-open-outline" />
                            <Input placeholder="Email" />
                        </InputGroup>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-unlock-outline" />
                            <Input
                                placeholder="Password"
                                secureTextEntry={true}
                            />
                        </InputGroup>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-calendar-outline" />
                            <Input placeholder="Birthday"/>
                        </InputGroup>
                        <InputGroup style={styles.mb20}>
                            <Icon name="ios-transgender" />
                            <Input placeholder="Gender"/>
                        </InputGroup>
                        <Button rounded block style={{backgroundColor: '#fff', marginTop: 20}} textStyle={{color: '#00c497'}}>
                            Save and Continue
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
        popRoute: () => dispatch(popRoute())
    }
}

export default connect(null, bindAction)(SignUp);
