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
        return (
            <Container theme={theme} style={{backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.popRoute()} title="Sign In" leftButton={true}/>
                    <Content padder style={{backgroundColor: 'transparent'}}>
                        <View padder>
                            <InputGroup style={styles.mb20}>
                                <Icon name="ios-mail-open-outline"/>

                                <Input placeholder="Email"
                                       value={this.state.email}
                                       onChangeText={(email) => this.setState({email})}
                                />
                            </InputGroup>
                            <InputGroup style={styles.mb20}>
                                <Icon name="ios-unlock-outline"/>
                                <Input
                                    placeholder="Password"
                                    secureTextEntry={true}
                                    value={this.state.password}
                                    onChangeText={(password) => this.setState({password})}
                                />
                            </InputGroup>
                            <Button isLoading={this.props.signingIn}
                                    onPress={ () => this.props.verifyCredential(this.state.email, this.state.password)}
                                    style={{marginTop: 20}}>
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
