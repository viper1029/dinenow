/*
import React, { Component, Navigator, StyleSheet} from 'react-native'
import {Router, Route, Schema, Animations, TabBar} from 'react-native-router-flux'

import Search from '../views/Search';
import SideDrawer from '../components/SideDrawer';

export default class MainRouter extends Component {
  render() {
    return (
      <SideDrawer ref={c => { c ? this.drawer = c.drawer : this.drawer }}>
        <Router name='drawerRoot'
          //navigationBarStyle={styles.navBar}
                titleStyle={styles.navTitle}
                navigationBarStyle=''
        >
          <Schema
            name='home'
            sceneConfig={Navigator.SceneConfigs.FloatFromRight}
            hideNavBar={false}
            renderLeftButton={this.renderMenuButton}
          />
          <Schema
            name='interior'
            sceneConfig={Navigator.SceneConfigs.FloatFromRight}
            hideNavBar={false}
            renderLeftButton={this.renderBackButton}
          />
          <Route name="search" component={Search} title="Find Restaurants" schema="home"/>
        </Router>
      </SideDrawer>
    )
  }
}

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
*/
