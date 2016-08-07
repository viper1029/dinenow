'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";
import {replaceRoute, popRoute} from "../../actions/route";
import {verifyCredential, signInErrorReset} from "../../actions/AuthActions";
import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View} from "native-base";
import theme from "../../theme/Theme";
import styles from "./styles";
import Button from "../new/Button";
import NavBar from "../new/NavBar";
import Form from "../new/Form";
import FormInput from "../new/FormInput";
import InlineTextInput from "../new/InlineTextInput";
import Modal from 'react-native-simple-modal';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view'


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
}

class SignIn extends Component {

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
            this.props.replaceRoute('home');
        }

        if (nextProps.signInError && this.props.signInError !== nextProps.signInError) {
            console.log(nextProps.signInError);
        }
    }

    render() {
        return (
            <View theme={theme} style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.popRoute()} title="Sign In" leftButton={true}/>
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
                                onPress={ () => this.props.verifyCredential(this.state.email, this.state.password)}
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

var mapStateToProps = function (state) {
    return {
        signingIn: state.auth.signingIn,
        signedIn: state.auth.signedIn,
        signInError: state.auth.signInError,
    };
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        popRoute,
        replaceRoute,
        verifyCredential,
        signInErrorReset
    }, dispatch);

};

module.exports = connect(mapStateToProps, mapDispatchToProps)(SignIn);
