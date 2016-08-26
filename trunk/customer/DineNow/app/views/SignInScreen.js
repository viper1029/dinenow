'use strict';

import React, {Component} from "react";
import {Image, StyleSheet, View, Text, Platform, TouchableOpacity, StatusBar} from "react-native";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view'

import {verifyCredential, signInErrorReset} from "../redux/actions/AuthActions";
import Button from "../components/Button";
import Form from "../components/Form";
import NavBar from "../components/NavBar";
import FormInput from "../components/FormInput";
import Modal from 'react-native-simple-modal';
import {Actions as NavActions} from 'react-native-router-flux'

//import NavBar from "../components/NavBar";

const UserValidators = {
    email: {
        title: 'Email',
        validate: [
            {
                validator: 'isLength',
                arguments: [1, 255],
            },
            {
                validator: 'isEmail',
                message: '{TITLE} must be valid',
            }]
    },
    password: {
        title: 'Password',
        validate: [{
            validator: 'isLength',
            arguments: [8, 255],
            message: '{TITLE} is too short',
        }]
    },
};

class SignInScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            offset: 0,
        };
    }

    componentWillReceiveProps(nextProps) {
        console.log("Receiving new Properties: ", nextProps.signedIn);
        if (nextProps.signedIn) {
            NavActions.drawer
        }

        if (nextProps.signInError && this.props.signInError !== nextProps.signInError) {
            console.log(nextProps.signInError);
        }
    }

    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <KeyboardAwareScrollView style={{flex: 1, backgroundColor: 'transparent', padding: 20}}>
                        <Form style={{backgroundColor: 'transparent'}}>
                            <FormInput
                                title='Email'
                                placeholder='Email'
                                autoCorrect={false}
                                autoCapitalize='none'
                                keyboardType='email-address'
                                iconName='email'
                                value={this.state.email}
                                name={UserValidators.email}
                                onChangeText={(text) => {
                                    this.setState({email: text})
                                }}
                            />
                            <FormInput
                                title='Password'
                                placeholder='Password'
                                autoCorrect={false}
                                autoCapitalize='none'
                                secureTextEntry={true}
                                iconName='lock'
                                value={this.state.password}
                                name={UserValidators.password}
                                onChangeText={(text) => {
                                    this.setState({password: text})
                                }}
                            />
                        </Form>
                        <Button isLoading={this.props.signingIn}
                                onPress={ /*() => this.props.verifyCredential(this.state.email, this.state.password)*/NavActions.drawer} // TODO: remove this}
                                style={{marginTop: 40}}>
                            Sign In
                        </Button>
                    </KeyboardAwareScrollView>
                </Image>
                <Modal
                    overlayBackground={'rgba(0, 0, 0, 0.75)'}
                    offset={this.state.offset}
                    open={this.props.signInError != null}
                    modalDidOpen={() => console.log('modal did open')}
                    modalDidClose={() => this.setState({open: false})}
                    style={{alignItems: 'center'}}
                    modalStyle={{padding: 0, borderRadius: 4}}>
                    <View style={{
                        alignItems: 'center'
                    }}>
                        <Text
                            style={{
                                color: 'black',
                                fontSize: 16,
                                marginBottom: 20,
                                marginTop: 20
                            }}>{typeof this.props.signInError === 'string' && this.props.signInError}</Text>
                        <Button
                            style={{margin: 0, padding: 0}}
                            onPress={() => this.props.signInErrorReset()}>
                            Okay
                        </Button>
                    </View>
                </Modal>
            </View>
        )
    }
}

var styles =  StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
        marginTop: (Platform.OS === 'ios') ? 64 : 54
    },
    modal: {
        justifyContent: 'center',
        alignItems: 'center',
        height: 300
    }
});

var mapStateToProps = function (state) {
    return {
        signingIn: state.auth.signingIn,
        signedIn: state.auth.signedIn,
        signInError: state.auth.signInError,
    };
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        verifyCredential,
        signInErrorReset : ()=>dispatch(signInErrorReset)
    }, dispatch);

};

module.exports = connect(mapStateToProps, mapDispatchToProps)(SignInScreen);
