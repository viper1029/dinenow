'use strict';
import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, StatusBar, PixelRatio, ScrollView, Dimensions, Text} from "react-native";
import Button from "../components/Button";
import {BackNavBar} from "./../components/NavBars";
import GeneralStyles from "./../styles/GeneralStyles";
import TouchableHightlightRow from "../components/TouchableHightlightRow";
import TextRow from "../components/TextRow";
var height = (Dimensions.get('window').height) - ((Platform.OS === 'ios') ? 64 : 54) - StatusBar.currentHeight;

const data = [
    {
        id: 1,
        name: "Cheese",
    },
    {
        id: 2,
        name: "Chicken",
    }
];

export default class CheckoutScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            size: "regular",
            modalVisible: false,
            offset: 0,
        };
    }

    render() {
        return (
            <View style={GeneralStyles.container}>
                <BackNavBar title="Checkout"/>
                <Image source={require('../assets/glow2.png')} style={GeneralStyles.backgroundImage}>
                    <ScrollView contentContainerStyle={{height: height}}>
                        <TouchableHightlightRow onPress={() => {
                        }} rowText="Delivery Address">
                            <Text style={styles.textSmall}>15046 68 Avenue</Text>
                            <View style={{flexDirection: 'row'}}>
                                <Text style={styles.textSmall}>Surrey, </Text>
                                <Text style={styles.textSmall}>BC, </Text>
                                <Text style={styles.textSmall}>V3S2B7</Text>
                            </View>
                        </TouchableHightlightRow>
                        <TouchableHightlightRow onPress={() => {
                        }} rowText="Payment Method">
                            <Text style={styles.textSmall}>Cash</Text>
                        </TouchableHightlightRow>
                        <TouchableHightlightRow onPress={() => {
                        }} rowText="Tip">
                            <Text style={styles.textSmall}>Please tip on delivery</Text>
                        </TouchableHightlightRow>
                        <TouchableHightlightRow onPress={() => {
                        }} rowText="Coupons">
                            <Text style={styles.textSmall}>None</Text>
                            </TouchableHightlightRow>
                        <TextRow leftText="Subtotal" rightText="$24.99"/>
                        <TextRow leftText="Delivery" rightText="$3.00"/>
                        <TextRow leftText="Discount" rightText="$0.00"/>
                        <TextRow leftText="Tip" rightText="$0.00"/>
                        <TextRow leftText="Tax" rightText="$1.20"/>
                        <TextRow leftTextStyle={{fontSize: 18}}
                                 rightTextStyle={{fontSize: 18}}
                                 leftText="Total" rightText="$28.99"/>
                        <View style={{flex: 1, justifyContent: 'flex-end'}}>
                            <Button style={{height: 60, backgroundColor: '#00c497', borderRadius: 0}}
                                    textStyle={{color: 'white', fontSize: 18, fontWeight: 'bold'}}
                                    onPress={()=> {
                                    }}>Place Order</Button>
                        </View></ScrollView>
                </Image>
            </View>
        )
    }
}

var styles = StyleSheet.create({
    section: {
        padding: 20,
        paddingTop: 10,
        paddingBottom: 10,
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 1 / PixelRatio.get(),
        borderColor: '#d6d7da'
    },
    textSmall: {
        fontSize: 12,
        lineHeight: 14,
        color: 'white'
    },
    textMedium: {
        fontSize: 16,
        lineHeight: 16,
        color: 'white'
    }
});
