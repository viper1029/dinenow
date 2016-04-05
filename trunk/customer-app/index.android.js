import React, { Component, AppRegistry, StyleSheet, Navigator, TouchableOpacity, Image } from 'react-native';
import {Router, Route, Schema, Animations, TabBar} from 'react-native-router-flux'
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';


import {ComponentsStyle} from './src/configs/CommonStyles';
import SideDrawer from './src/components/SideDrawer';

import Intro from './src/views/Intro';
import SignUp from './src/views/Signup';
import SignIn from './src/views/Signin';
import Search from './src/views/Search';
import AboutUs from './src/views/AboutUs';

export default class DineNow extends Component {

  renderMenuButton = () => {
    return (
      <TouchableOpacity
        style={styles.leftButtonContainer}
        onPress={() => this.drawer.open()}
      >
        <MaterialIcons name="menu" size={24}/>
      </TouchableOpacity>
    )
  };

  renderBackButton = () => {
    return (
      <TouchableOpacity
        style={styles.leftButtonContainer}
        onPress={Actions.pop}
      >
        <MaterialIcons name="arrow_back" size={24}/>
      </TouchableOpacity>
    )
  };

  render() {
    return (
      <Router name="root"
              navigationBarStyle={styles.navBar}
              titleStyle={styles.navTitle}
              hideNavBar={true}
              sceneStyle={styles.routerScene}
      >
        <Schema name="default" sceneConfig={Navigator.SceneConfigs.FloatFromRight} />
        <Route name="SignUp" component={AboutUs} initial={true} hideNavBar={false} title="Sign Up" style="default" />
        <Route name="Drawer" hideNavBar={true} type="reset">
          <SideDrawer ref={c => { c ? this.drawer = c.drawer : this.drawer }}>
            <Router name="drawerRoot"
                    navigationBarStyle={styles.navBar}
                    titleStyle={styles.navTitle}
            >
              <Schema
                name="home"
                sceneConfig={Navigator.SceneConfigs.FloatFromRight}
                hideNavBar={false}
                renderLeftButton={this.renderMenuButton}
              />
              <Schema
                name="interior"
                sceneConfig={Navigator.SceneConfigs.FloatFromRight}
                hideNavBar={false}
                renderLeftButton={this.renderBackButton}
              />
              <Route name="search" component={Search} title="Search Screen" schema="home"/>
            </Router>
          </SideDrawer>
        </Route>
      </Router>
    );
  }
};

var styles = StyleSheet.create({
  navBar: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'green',
  },
  navTitle: {
    color: 'white',
  },
  routerScene: {
    paddingTop: Navigator.NavigationBar.Styles.General.NavBarHeight
  },
  leftButtonContainer: {
    paddingLeft: 15,
    paddingRight: 20,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
});

// Show the react component on the screen
AppRegistry.registerComponent('DineNow', function () {
  return DineNow
});
