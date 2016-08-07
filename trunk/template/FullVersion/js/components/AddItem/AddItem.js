'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";
import {replaceRoute, popRoute} from "../../actions/route";
import {openDrawer} from "../../actions/drawer";
import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View} from "native-base";
import theme from "../../theme/Theme";
import styles from "./styles";
import NavBar from "../new/NavBar";
import {GooglePlacesAutocomplete} from "../new/GooglePlacesAutoComplete";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";

class AddItem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            offset: 0,
        };
    }

    render() {
        return (
            <View theme={theme} style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.openDrawer()} title="Add Item" leftButton={true}/>
                    <View >
                        <Text>
                            Chicken Wings
                        </Text>
                        <Text>
                            Chicken Wings
                        </Text>
                    </View>
                </Image>
            </View>
        )
    }
}

var mapStateToProps = function (state) {
    return {};
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        popRoute,
        openDrawer,
    }, dispatch);

};


module.exports = connect(mapStateToProps, mapDispatchToProps)(AddItem);
