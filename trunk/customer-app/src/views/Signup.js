import React, {Component, View, Text, StyleSheet, TextInput} from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import {Actions} from 'react-native-router-flux'
import {ComponentsStyle} from '../configs/CommonStyles';
import Button from '../components/Button';
import TextInputWithIcon from '../components/TextInputWithIcon';

export default class SignUp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fullName: '',
      email: '',
      password: '',
      phoneNumber: ''
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <TextInputWithIcon
          placeholder="Full Name"
          value={this.state.fullName}
          onChangeText={(text) => this.setState({firstName: text})}
          icon={<MaterialIcons name="person" size={30} style={ComponentsStyle.TextInput.TextInputWithIcon.iconStyle}/>}
        />
        <TextInputWithIcon
          placeholder="Email"
          value={this.state.firstName}
          onChangeText={(text) => this.setState({email: text})}
          icon={<MaterialIcons name="email" size={30} style={ComponentsStyle.TextInput.TextInputWithIcon.iconStyle}/>}
        />
        <TextInputWithIcon
          placeholder="Password"
          value={this.state.password}
          onChangeText={(text) => this.setState({firstName: text})}
          icon={<MaterialIcons name="lock" size={30} style={ComponentsStyle.TextInput.TextInputWithIcon.iconStyle}/>}
        />
        <TextInputWithIcon
          placeholder="Phone Number"
          value={this.state.phoneNumber}
          onChangeText={(text) => this.setState({firstName: text})}
          icon={<MaterialIcons name="phone" size={30} style={ComponentsStyle.TextInput.TextInputWithIcon.iconStyle}/>}
        />
        <Button
          buttonStyle={ComponentsStyle.Buttons.LargeButton.ButtonStyle}
          underLayColor={ComponentsStyle.Buttons.LargeButton.Colors.BLUE_BUTTON_PRESS}
          textStyle={ComponentsStyle.Buttons.LargeButton.TextStyle}
          text={'Sign Up'}
          onPress={Actions.Drawer}/>
      </View>
    );
  }

  onBackPress() {
    //this.props.navigator.pop();
  }

  onSignUpPress() {
  }
};

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    alignSelf: 'stretch'
  }
});
