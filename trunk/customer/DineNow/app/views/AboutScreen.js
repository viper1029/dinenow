'use strict';

import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";

export default class AboutScreen extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>

                </Image>
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
    }
});
