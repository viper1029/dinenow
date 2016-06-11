import React, { Component } from 'react';
import { View, PropTypes, Text } from 'react-native';

import Button from './Button';
import { Actions } from 'react-native-router-flux'

export default class SideDrawerContent extends Component {
  static contextTypes = {
    drawer: PropTypes.object.isRequired
  };

  render() {
    const { drawer } = this.context;
    return (
      <View>
        <Text>Home</Text>
        <Text>Profile</Text>
        <Button onPress={() => { drawer.close(); Actions.Home.call() }}>{'Home'}</Button>
        <Button onPress={() => { drawer.close(); Actions.Screen1.call() }}>{'Profile'}</Button>
      </View>
    )
  }
}

SideDrawerContent.propTypes = {
  drawer: PropTypes.object
};
