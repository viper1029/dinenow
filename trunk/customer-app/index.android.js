import React, { Component } from 'react';
import { View, AppRegistry, StyleSheet, Navigator, TouchableOpacity, Image } from 'react-native';

import {Actions, Scene, Router} from 'react-native-router-flux'
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import routes from './src/reducers/routes';

import {ComponentsStyle} from './src/configs/CommonStyles';
import SideDrawer from './src/components/SideDrawer';

import Intro from './src/views/Intro';
import SignUp from './src/views/Signup';
import SignIn from './src/views/Signin';
import Search from './src/views/Search';
import AboutUs from './src/views/AboutUs';
import About from './src/views/About';


export default class DineNow extends Component {

  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene key="intro" component={Intro} title="Intro" hideNavBar={true} initial={true} />
          <Scene key="search" component={Search} title="Search" />
        </Scene>
      </Router>
    );
  }
};

var styles = StyleSheet.create({
  routerScene: {
  },
});

// Show the react component on the screen
AppRegistry.registerComponent('DineNow', function () {
  return DineNow
});
