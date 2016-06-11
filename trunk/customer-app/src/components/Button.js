import React, { Component } from 'react';
import {Text, StyleSheet, TouchableHighlight} from 'react-native';

var styles = StyleSheet.create({
  buttonStyle: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#48BBEC',
    borderColor: '#48BBEC',
    borderWidth: 1,
    borderRadius: 3,
    padding: 12,
    margin: 20
  },
  textStyle: {
    flex:1,
    alignSelf: 'center',
    fontSize: 16
  }
});

export default class Button extends Component {
  constructor(props) {
    super(props);
  }

  static defaultProps = {
    buttonStyle: styles.buttonStyle,
    textStyle: styles.textStyle,
    underLayColor: '#99d9f4',
    text: ''
  };

  render() {
    return (
      <TouchableHighlight
        style={this.props.buttonStyle}
        underlayColor={this.props.underLayColor}
        onPress={this.props.onPress}
        >
      <Text style={this.props.textStyle}>{this.props.text}</Text>
      </TouchableHighlight>
    );
  }
};

