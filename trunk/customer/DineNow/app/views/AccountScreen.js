'use strict';

import React, {Component} from "react";
import {Image, View} from "react-native";
import GeneralStyles from "./../styles/GeneralStyles";
import Row from "../components/TouchableHightlightRow";

export default class AccountScreen extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={GeneralStyles.container}>
                <Image source={require('../assets/glow2.png')} style={GeneralStyles.backgroundImage}>
                    <Row onPress={() => {
                    }} rowText="Edit Profile"/>
                    <Row onPress={() => {
                    }} rowText="Payment Methods"/>
                    <Row onPress={() => {
                    }} rowText="Delivery Address"/>
                </Image>
            </View>
        )
    }
}
