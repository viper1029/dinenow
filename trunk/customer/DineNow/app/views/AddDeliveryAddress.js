'use strict';

import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, TextInput} from "react-native";
import {BackNavBar} from "./../components/NavBars";
import Modal from "./../components/Modal";
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view'
import Form from "../components/Form";
import InlineTextInput from "../components/InlineTextInput";

export default class DeliveryAddressScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            modalVisible: false,
            offset: 0,
            name: "",
            address: "",
            city: "",
            postalCode: "",
        }
    }


    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <View style={{flex: 1, borderWidth: 0}}>
                    <BackNavBar title="Add Address"
                                leftIconName="clear"
                                rightButton={true}
                                rightIconName="check"
                                onRightPress={() => {}}
                    />
                    <Image source={require('../assets/glow2.png')} style={styles.container}>
                        <View style={{padding: 20}}>
                            <TextInput
                                autoCapitalize="words"
                                placeholder="Street Address"
                                underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                placeholderTextColor="grey"
                                style={styles.textInput}
                                onChangeText={(text) => {
                                }}
                                value={this.state.text}
                            />
                            <View style={{flexDirection: 'row'}}>
                                <TextInput
                                    autoCapitalize="words"
                                    placeholder="Apt/Suite/Unit"
                                    underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                    placeholderTextColor="grey"
                                    style={styles.textInput}
                                    onChangeText={(text) => {
                                    }}
                                    value={this.state.text}
                                />
                                <TextInput
                                    autoCapitalize="words"
                                    placeholder="City"
                                    underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                    placeholderTextColor="grey"
                                    style={styles.textInput}
                                    onChangeText={(text) => {
                                    }}
                                    value={this.state.text}
                                />
                            </View>
                            <View style={{flexDirection: 'row'}}>
                                <TextInput
                                    autoCapitalize="words"
                                    placeholder="Province"
                                    underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                    placeholderTextColor="grey"
                                    style={styles.textInput}
                                    onChangeText={(text) => {
                                    }}
                                    value={this.state.text}
                                />
                                <TextInput
                                    autoCapitalize="words"
                                    placeholder="Postal Code"
                                    underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                    placeholderTextColor="grey"
                                    style={styles.textInput}
                                    onChangeText={(text) => {
                                    }}
                                    value={this.state.text}
                                />
                                <TextInput
                                    autoCapitalize="words"
                                    placeholder="Country"
                                    underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                    placeholderTextColor="grey"
                                    style={styles.textInput}
                                    onChangeText={(text) => {
                                    }}
                                    value={this.state.text}
                                />
                            </View>
                            <TextInput
                                autoCapitalize="words"
                                placeholder="Instructions (Optional)"
                                underlineColorAndroid="rgba(182, 182, 182, 0.62)"
                                placeholderTextColor="grey"
                                style={styles.textInput}
                                onChangeText={(text) => {
                                }}
                                value={this.state.text}
                            />
                        </View>
                    </Image>
                </View>
            </View>
        )
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
        marginTop: (Platform.OS === 'ios') ? 64 : 54
    },
    textInput: {
        flex: 1,
        height: 40,
        marginTop: 5,
        color: 'white',
        borderColor: 'rgba(182, 182, 182, 0.62)'
    }
});
