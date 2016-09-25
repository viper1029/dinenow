import {View, TouchableHighlight, Text} from "react-native";
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import React from "react";
import ListStyle from "./../styles/ListStyles";
import Theme from "./../styles/Theme";
import {PixelRatio, StyleSheet} from "react-native";

export default TextRow = (props) => (
    <View style={[styles.row]}>
        <View style={styles.rowContent}>
            <Text style={[styles.leftTextStyle,props.leftTextStyle]}>{props.leftText}</Text>
            <View>{props.children}</View>
            <Text style={[styles.rightTextStyle,props.rightTextStyle]}>{props.rightText}</Text>
        </View>
    </View>
)

styles = StyleSheet.create({
    row: {
        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 0,
        borderColor: '#d6d7da',
        padding: 10,
        paddingTop: 5,
        paddingBottom: 0,
        alignItems: 'center',
        alignSelf: 'stretch'
    },
    rowContent: {
        flex: 1,
        flexDirection: 'row',
        alignSelf: 'stretch',
        justifyContent: 'center',
        alignItems: 'center'
    },
    rightTextStyle: {
        justifyContent: 'flex-end',
        alignSelf: 'flex-end',
        alignItems: 'flex-end',
        fontSize: 15,
        paddingRight: 10,
        color: 'white'
    },
    leftTextStyle: {
        paddingLeft: 10,
        fontSize: 15,
        flex: 1,
        color: 'white'
    }
})
