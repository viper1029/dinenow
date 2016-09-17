'use strict';

import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, TouchableOpacity, StatusBar, PixelRatio, TouchableHighlight, Text} from "react-native";
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';

export default class AccountScreen extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <TouchableHighlight
                        style={styles.firstRow}
                        underlayColor="#48BBEC"
                        onPress={()=>{}}
                    >
                        <View style={styles.view}>
                            <Text style={styles.rowLabel}>Build Version</Text>
                            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
                        </View>
                    </TouchableHighlight>
                    <TouchableHighlight
                        style={styles.row}
                        underlayColor="#48BBEC"
                        onPress={()=>{}}
                    >
                        <View style={styles.view}>
                            <Text style={styles.rowLabel}>Build Version</Text>
                            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
                        </View>
                    </TouchableHighlight>

                    <TouchableHighlight
                        style={styles.lastRow}
                        underlayColor="#48BBEC"
                        onPress={()=>{}}
                    >
                        <View style={styles.view}>
                            <Text style={styles.rowLabel}>Build Version</Text>
                            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
                        </View>
                    </TouchableHighlight>
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
    },
    firstRow: {

        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 0,
        borderColor: '#d6d7da',
        padding: 10,
        alignItems: 'center',
        alignSelf: 'stretch',
    },
    row: {
        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 1 / PixelRatio.get(),
        borderColor: '#d6d7da',
        padding: 10,
        alignItems: 'center',
        alignSelf: 'stretch',
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
});
