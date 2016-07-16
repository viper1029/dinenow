/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { DeviceEventEmitter, Dimensions, Image, Platform, Keyboard, StyleSheet} from 'react-native';
import {pushNewRoute, replaceRoute} from '../../actions/route';

import Swiper from 'react-native-swiper2';
import Button from 'apsl-react-native-button';
import Modal from 'react-native-modalbox';

import {Container, Content, Text, InputGroup, Input, Icon, View } from 'native-base';
import { Col, Row, Grid } from "react-native-easy-grid";

var height = Dimensions.get('window').height;
var dot = <View style={{
            backgroundColor:'rgba(120,120,120,.5)',
            width: 8,
            height: 8,
            borderRadius: 4,
            marginLeft: 3,
            marginRight: 3,
            marginTop: 3,
            marginBottom: 3,
          }}/>;

class Intro extends Component {

  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      visibleHeight: Dimensions.get('window').height,
      scroll: false
    };
  }

  // componentWillMount () {
  //     Keyboard.addListener('keyboardWillShow', this.keyboardWillShow.bind(this))
  //     Keyboard.addListener('keyboardWillHide', this.keyboardWillHide.bind(this))
  // }

  // keyboardWillShow (e) {
  //     let newSize = Dimensions.get('window').height - e.endCoordinates.height
  //     this.setState({scroll: true})
  // }

  // keyboardWillHide (e) {
  //     this.setState({scroll: false})
  // }

  replaceRoute(route) {
    this.props.replaceRoute(route);
  }

  pushNewRoute(route) {
    this.props.pushNewRoute(route);
  }

  render() {
    return (
      <Image source={require('../../../images/login.png')}
             style={styles.container}>
        <View style={styles.header}/>
        <Swiper style={styles.swiperContainer}
                showsButtons={false}
                dot={dot}
                height={height/2}>
          <View style={styles.slide}>
            <Text style={styles.slideText}>Hello Swiper</Text>
          </View>
          <View style={styles.slide}>
            <Text style={styles.slideText}>Beautiful</Text>
          </View>
          <View style={styles.slide}>
            <Text style={styles.slideText}>And simple</Text>
          </View>
        </Swiper>

        <View style={styles.footer}>
          <View style={styles.spacer}></View>
          <View style={styles.buttonContainer}>
            <View style={styles.signUpButtonContainer}>
              <Button
                style={styles.signUpButton}
                textStyle={styles.buttonText}
                onPress={() => this.pushNewRoute('signUp')}>
                Sign Up
              </Button>
            </View>
            <View style={styles.signUpButtonContainer}>
              <Button
                style={styles.signInButton}
                textStyle={styles.buttonText}
                onPress={() => this.pushNewRoute('home')}>
                Sign In
              </Button>
            </View>
          </View>
          <View style={styles.skipButtonContainer}>
            <Button
              style={styles.skipButton}
              textStyle={styles.skipButtonText}
              onPress={() => this.pushNewRoute('signUp')}>
              Skip
            </Button>
          </View>
        </View>
      </Image>
    )
  }
}


function bindActions(dispatch) {
  return {
    replaceRoute: (route)=>dispatch(replaceRoute(route)),
    pushNewRoute: (route)=>dispatch(pushNewRoute(route))
  }
}

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'stretch',
    width: null
  },
  header: {
    flex: 30
  },
  swiperContainer: {
    flex: 50,
    borderColor: 'white',
    borderWidth: 5,
  },
  footer: {
    flex: 20,
    alignSelf: 'stretch'
  },
  buttonContainer: {
    flex: 2,
    flexDirection: 'row',
    alignSelf: 'stretch',
    backgroundColor: 'rgba(0,0,0,0.5)'
  },
  slide: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  slideText: {
    fontSize: 30,
    height: 40,
    fontWeight: 'bold',
    color: 'white'
  },
  dot: {
    backgroundColor: 'rgba(255,255,255,0.6)',
    width: 20,
    height: 12,
    borderRadius: 6,
    marginLeft: 5,
    marginRight: 5,
    marginTop: 3,
    marginBottom: 20,
  },
  spacer: {
    flex: 0.5
  },
  signUpButtonContainer: {
    flex: 1,
    alignSelf: 'stretch',
    justifyContent: 'flex-end',
    marginRight: 5,
    marginLeft: 10
  },
  signInButtonContainer: {
    flex: 1,
    alignSelf: 'stretch',
    justifyContent: 'flex-end',
    marginRight: 10,
    marginLeft: 5,
  },
  skipButtonContainer: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)'
  },
  signUpButton: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#48BBEC',
    borderColor: '#48BBEC',
    borderWidth: 1,
    borderRadius: 5,
    padding: 8
  },
  signInButton: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#48BBEC',
    borderColor: '#48BBEC',
    borderWidth: 1,
    borderRadius: 5,
    padding: 8
  },
  skipButton: {
    flex: 1,
    justifyContent: 'center',
    alignSelf: 'center',
    marginBottom: 5,
    paddingLeft: 40,
    paddingRight: 40,
    borderWidth: 0
  },
  buttonText: {
    alignSelf: 'center',
    fontSize: 16,
    color: 'white'
  },
  skipButtonText: {
    alignSelf: 'center',
    fontSize: 14,
    color: 'white'
  },
  modal: {
    justifyContent: 'center',
    alignItems: 'center'
  },
});


export default connect(null, bindActions)(Intro);
