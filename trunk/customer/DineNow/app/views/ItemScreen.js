'use strict';

import React, {Component} from "react";
import {Image, View, StyleSheet, Platform, TouchableOpacity, StatusBar, Text, Picker, TouchableHighlight} from "react-native";
import NumberInput from "../components/NumberInput";
import PopUp from "../components/PopUp";
import Modal from 'react-native-simple-modal';

export default class ItemScreen extends Component {

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
            <View style={{flex: 1}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <View style={{
                        flexDirection: 'row',
                        justifyContent: 'space-between',
                        padding: 10,
                        paddingTop: 15,
                        paddingBottom: 15,
                        backgroundColor: 'white'
                    }}>
                        <Text style={{fontSize: 16, fontWeight: 'bold'}}>
                            Chicken Wings
                        </Text>
                        <Text>
                            $9.99
                        </Text>
                    </View>
                    <View style={{padding: 10, paddingTop: 15, paddingBottom: 15}}>
                        <Text>
                            Item description here
                        </Text>
                    </View>
                    <View style={{padding: 10}}>
                        <Text color='red'>
                            Quantity
                        </Text>
                        <NumberInput/>
                    </View>
                    <View style={{padding: 10}}>
                        <Text color='red'>
                            Size
                        </Text>
                        <Button onPress={()=>{this.setState({
                            modalVisible: true
                        })}}>Open</Button>
                    </View>
                    <Picker
                        selectedValue={this.state.language}
                        onValueChange={(lang) => this.setState({language: lang})}>
                        <Picker.Item label="Java" value="java" />
                        <Picker.Item label="JavaScript" value="js" />
                    </Picker>

                </Image>
                <Modal
                    overlayBackground={'rgba(0, 0, 0, 0.75)'}
                    offset={this.state.offset}
                    open={this.state.modalVisible}
                    modalDidOpen={() => console.log('modal did open')}
                    modalDidClose={() => this.setState({modalVisible: false})}
                    style={{alignItems: 'center'}}
                    modalStyle={{padding: 0, borderRadius: 4}}>
                    <View style={{
                        alignItems: 'center'
                    }}>
                        <Text
                            style={{
                                color: 'black',
                                fontSize: 16,
                                marginBottom: 20,
                                marginTop: 20
                            }}>Content</Text>
                        <Button
                            style={{margin: 0, padding: 0}}
                            onPress={() => {this.setState({modalVisible: false})}}>
                            Okay
                        </Button>
                    </View>
                </Modal>
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
