/* @flow */
'use strict';

import { StyleSheet } from "react-native";

module.exports = StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
    },
    iconContainer: {
      flexDirection: 'row',
      flexWrap: 'wrap',
      justifyContent: 'space-around',
      paddingLeft: 15
    },
    icon: {
        width: 45,
        height: 45,
        justifyContent: 'center'
    }
});
