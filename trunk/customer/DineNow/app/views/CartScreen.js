'use strict';

import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, ListView, PixelRatio, TouchableHighlight, Text} from "react-native";
import {BackNavBar} from "./../components/NavBars";
import MaterialIcons from "react-native-vector-icons/MaterialIcons";
import {Actions as NavActions} from "react-native-router-flux";
import TextRow from "../components/TextRow";

const data = [
    {
        itemId: '12345',
        quantity: "5",
        itemName: 'Burger',
        addons: ["Chicken", "Cheese"],
        itemTotal: "$6.99"
    },
    {
        itemId: '12345',
        itemName: 'Pizza',
        quantity: "5",
        addons: ["Chicken", "Cheese"],
        itemTotal: "$9.99"
    },
];

export default class DeliveryAddressScreen extends Component {

    constructor(props) {
        super(props);
        var ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows(data),
        };
    }

    _renderRow(rowData, sectionID, rowID, highlightRow:(sectionID:number, rowID:number) => void) {
        return (
            <View>
                <View style={styles.listRow}>
                    <View style={{flex: 0.8, alignSelf: 'center'}}>

                        <View style={{flexDirection: 'row'}}>
                            <Text style={styles.listText}>{rowData.quantity} </Text>
                            <Text style={styles.listText}>{rowData.itemName}</Text>
                        </View>
                        <Text style={styles.listText}>{rowData.addons}</Text>
                    </View>
                    <View style={{flex: 0.2, alignItems: 'flex-end', alignSelf: 'center'}}>
                        <Text>{rowData.itemTotal}</Text>
                    </View>
                </View>
            </View>
        );
    };

    _renderSeparator(sectionID, rowID, adjacentRowHighlighted) {
        return (
            <View
                key={`${sectionID}-${rowID}`}
                style={{
                    height: adjacentRowHighlighted ? 4 : 1 / PixelRatio.get(),
                    backgroundColor: adjacentRowHighlighted ? '#3B5998' : '#d6d7da',
                }}
            />
        );
    }


    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <View style={{flex: 1, borderWidth: 0}}>
                    <BackNavBar title="Cart"/>
                    <View style={styles.row}>
                        <View style={styles.rowContent}>
                            <Text>Bob's Pizza</Text>
                        </View>
                    </View>
                    <Image source={require('../assets/glow2.png')} style={styles.container}>
                        <ListView
                            dataSource={this.state.dataSource}
                            renderRow={this._renderRow.bind(this)}
                            renderSeparator={this._renderSeparator}
                        />
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
    row: {
        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 0,
        borderColor: '#d6d7da',
        padding: 10,
        alignItems: 'center',
        alignSelf: 'stretch',
        borderBottomWidth: 1 / PixelRatio.get(),
    },
    lastRow: {
        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 1 / PixelRatio.get(),
        borderBottomWidth: 1 / PixelRatio.get(),
        borderColor: '#d6d7da',
        padding: 10,
        alignItems: 'center',
        alignSelf: 'stretch',
    },
    view: {
        flex: 1,
        flexDirection: 'row',
        alignSelf: 'stretch',
        justifyContent: 'center',
        alignItems: 'center',
    },
    arrow: {
        justifyContent: 'flex-end',
        alignSelf: 'flex-end',
        alignItems: 'flex-end',
        color: 'white'
    },
    rowLabel: {
        left: 10,
        fontSize: 15,
        flex: 1,
        color: 'white'
    },
    listRow: {
        flex: 1,
        minHeight: 75,
        flexDirection: 'row',
        justifyContent: 'center',
        padding: 20,
        paddingTop: 0,
        paddingBottom: 0,
        backgroundColor: 'transparent',
        borderRadius: 0,
        paddingHorizontal: 20,
        elevation: 0,
        shadowRadius: 0,
        alignItems: 'flex-start'
    },
    listText: {
        fontSize: 14,
        lineHeight: 16,
        color: 'white'
    }
});
