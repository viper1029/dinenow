'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, Platform, TouchableOpacity} from "react-native";
import {replaceRoute, popRoute} from "../../actions/route";
import {verifyCredential} from "../../actions/AuthActions";
import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View} from "native-base";
import theme from "../../theme/Theme";
import styles from "./styles";
import Button from "../new/Button";
import NavBar from "../new/NavBar";
import Form from "../new/Form";
import FormInput from "../new/FormInput";
import InlineTextInput from "../new/InlineTextInput";

class SignIn extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
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
        const { name, email, password } = this.state
        const nameValid = (name && name.length > 0 ? true : false)
        const emailValid = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email)
        const passwordValid = (password && password.length >= 8 ? true : false)
        return (
            <Container theme={theme} style={{backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.popRoute()} title="Sign In" leftButton={true}/>
                    <Content padder style={{backgroundColor: 'transparent'}}>
                        <View padder>
                            <Form style={{ backgroundColor: 'transparent'}}>
                                <FormInput
                                    placeholder='Email'
                                    autoCorrect={false}
                                    autoCapitalize='none'
                                    keyboardType='email-address'
                                    iconName='email'
                                    value={email}
                                    valid={emailValid}
                                    message={email && !emailValid ? 'Please enter a valid email address' : null}
                                    onChangeText={(text) => { this.setState({email: text}) }}
                                />
                                <FormInput
                                    title='Password'
                                    placeholder='Password'
                                    autoCorrect={false}
                                    autoCapitalize='none'
                                    secureTextEntry={true}
                                    iconName='lock'
                                    value={password}
                                    valid={passwordValid}
                                    message={password && !passwordValid ? 'Password too short' : null}
                                    onChangeText={(text) => { this.setState({password: text}) }}
                                />
                            </Form>
                            <Button isLoading={this.props.signingIn}
                                    onPress={ () => this.props.verifyCredential(this.state.email, this.state.password)}
                                    style={{marginTop: 40}}>
                                Sign In
                            </Button>
                        </View>
                    </Content>
                </Image>
            </Container>
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
    }, dispatch);

};

module.exports = connect(mapStateToProps, mapDispatchToProps)(SignIn);
