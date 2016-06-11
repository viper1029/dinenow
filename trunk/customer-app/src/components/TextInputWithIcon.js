import React, {Component} from 'react';
import {PropTypes, StyleSheet, View, TextInput} from 'react-native';
import {ComponentsStyle} from '../configs/CommonStyles';

export default class TextInputWithIcon extends Component {
  constructor(props) {
    super(props);
  }

  static defaultProps = {
    inputContainerStyle: ComponentsStyle.TextInput.TextInputWithIcon.inputContainerStyle,
    textInputStyle: ComponentsStyle.TextInput.TextInputWithIcon.textInputStyle,
    iconStyle: ComponentsStyle.TextInput.TextInputWithIcon.iconStyle,
    icon: <View style={{marginRight: 30}}/>,
    placeholder: '',
    value: '',
    onChangeText: () => ({})
  };

  render() {
    return (
      <View style={this.props.inputContainerStyle}>
        {this.props.icon}
        <TextInput
          placeholder={this.props.placeholder}
          style={this.props.textInputStyle}
          value={this.props.value}
          onChangeText={this.props.onChangeText}
        />
      </View>
    )
  }
}