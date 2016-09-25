import {View, TouchableHighlight, Text} from "react-native";
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import React from "react";
import ListStyle from "./../styles/ListStyles";
import Theme from "./../styles/Theme";

export default Row = (props) => (
    <TouchableHighlight
        style={[ListStyle.row, props.touchableStyle]}
        underlayColor={Theme.rowUnderLayColor}
        onPress={props.onPress}
    >
        <View style={[ListStyle.rowContent, props.rowContentStyle]}>
            <Text style={[ListStyle.rowLabel, props.rowLabelStyle]}>{props.rowText}</Text>
            <View>{props.children}</View>
            <MaterialIcons name="chevron-right" size={Theme.rowIconFontSize} style={ListStyle.arrow}/>
        </View>
    </TouchableHighlight>
)
