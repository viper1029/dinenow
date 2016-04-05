var React = require('react-native');
var {
  View,
  Text,
  StyleSheet,
  Dimensions,
  Image
  } = React;

import Swiper from 'react-native-swiper2';
var Modal = require('react-native-modalbox');
var Button = require('../components/common/button');
var SignUp = require('./authentication/signup');
var SignIn = require('./authentication/signin');

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

module.exports = React.createClass({
  render: function () {
    return (
      <Image source={require('../assets/images/login.png')}
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
                text={'Sign up'}
                buttonStyle={styles.signUpButton}
                textStyle={styles.buttonText}
                onPress={this.onSignUpPress}/>
            </View>
            <View style={styles.signUpButtonContainer}>
              <Button
                text={'Sign in'}
                buttonStyle={styles.signInButton}
                textStyle={styles.buttonText}
                onPress={this.onSignInPress}/>
            </View>
          </View>
          <View style={styles.skipButtonContainer}>
            <Button
              text={'Skip'}
              buttonStyle={styles.skipButton}
              textStyle={styles.skipButtonText}
              onPress={this.onSkipPress}/>
          </View>
        </View>
        <Modal style={styles.modal} ref={"signUpModal"} onClosed={this.onClose} onOpened={this.onOpen} onClosingState={this.onClosingState}>
          <SignUp/>
        </Modal>
        <Modal style={styles.modal} ref={"signInModal"} onClosed={this.onClose} onOpened={this.onOpen} onClosingState={this.onClosingState}>
          <SignIn/>
        </Modal>
      </Image>
    );
  },
  onSignUpPress: function () {
    this.refs.signUpModal.open();
  },
  onSignInPress: function () {
    this.refs.signInModal.open();
  },
  onSkipPress: function () {

  }
});

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
    flex: 50
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
    fontWeight: 'bold',
    color: 'white'
  },
  dot: {
    backgroundColor: 'rgba(255,255,255,0.6)',
    width: 12,
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
    justifyContent: 'flex-end',
    marginBottom: 5
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
