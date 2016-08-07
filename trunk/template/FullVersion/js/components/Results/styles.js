/* @flow */
'use strict';

import { StyleSheet } from "react-native";

export default StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
    },
    mb20: {
        marginBottom: 20,
        borderBottomColor: "rgba(182, 182, 182, 0.62)"
    },
    row: {
        flex:1,
        height: 100,
        flexDirection: 'row',
        justifyContent: 'center',
        padding: 10,
        backgroundColor: '#F6F6F6',
        borderRadius: 0,
        paddingHorizontal: 10,
        alignItems: 'flex-start'
    },
    thumb: {
        width: 64,
        height: 64,
    },
    headerText: {
        fontSize: 18,
        color: 'black',
        marginBottom: 10,
    },
    smallText: {
        fontSize: 12,
        color: 'black',
        lineHeight:18,
    },
    mediumText: {
        fontSize: 14,
        color: 'black',
        lineHeight:18,
    },
    text: {
        color: 'black'
    },
});
