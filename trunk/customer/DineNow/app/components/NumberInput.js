import React, {Component} from "react";
import {Platform, View, TouchableOpacity, Text, StyleSheet, TouchableHighlight} from "react-native";
import Button from "./Button";
import Icon from "./Icon";

export default class NumberInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            count: 1,
        };
    }

    onChange() {
        this.props.onChange();
    }

    render() {
        return (
            <View style={{flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}>
                <View style={{flex: 0.33}}>
                <Button style={styles.buttonStyle} onPress={() => {
                    this.setState({count: this.state.count > 1 && this.state.count - 1 || 1})
                }}>
                    <Icon name={"remove"}/>
                </Button>
                    </View>
                <View style={{flex: 0.33, alignItems: 'center'}}>
                    <Text color='black' style={styles.textStyle}>
                        {this.state.count}
                    </Text>
                </View>
                <View style={{flex: 0.33}}>
                <Button style={styles.buttonStyle} onPress={() => {
                    this.setState({count: this.state.count + 1})
                }}>
                    <Icon name={"add"}/>
                </Button>
                    </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    buttonStyle: {
        alignSelf: 'flex-start',
        justifyContent: 'center',
        flexDirection: 'row',
        alignItems: 'center',
        borderWidth: 0,
        borderRadius: 4,
        paddingHorizontal: 20,
        paddingVertical: 8,
        backgroundColor: 'lightgrey',
        marginBottom: 0,
        elevation: 2,
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 2},
        shadowOpacity: 0.1,
        shadowRadius: 1.5
    },
    iconStyle: {
        marginRight: 10,
        marginLeft: 10
    },
    textStyle: {
        fontSize: 18
    }
});
