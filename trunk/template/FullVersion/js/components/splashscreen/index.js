'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { Image, View } from 'react-native';


export default class SplashPage extends Component {

    componentWillMount () {
        var navigator = this.props.navigator;
        setTimeout (() => {
            navigator.replace({
                id: 'login',
            });
        }, 1500);
    }
    render () {
        return (
            <Image source={require('../../../images/launchscreen.png')} style={{flex: 1, height: null, width: null}} />


        );
    }
}
