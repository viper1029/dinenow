'use strict';

import React, {Component} from "react";
import {
    Image,
    View,
    StyleSheet,
    Platform,
    TextInput,
    ListView,
    PixelRatio,
    TouchableHighlight,
    Text
} from "react-native";
import {BackNavBar} from "./../components/NavBars";
import ListStyle from "./../styles/ListStyles";
import Modal from "./../components/Modal";
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view'
import Form from "../components/Form";
import InlineTextInput from "../components/InlineTextInput";
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import {Actions as NavActions, ActionConst} from 'react-native-router-flux'
import TouchableHightlightRow from "../components/TouchableHightlightRow";

const data = [
    {
        address1: '15046 68 Ave',
        address2: "",
        city: 'Surrey',
        province: 'BC',
        postalCode: 'V3S2B7',
        country: 'Canada',
    },
    {
        address1: '15043 68 Ave',
        address2: "",
        city: 'Surrey',
        province: 'BC',
        postalCode: 'V3S2B7',
        country: 'Canada',
    }
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
                <TouchableHighlight
                    onPress={()=> { }}
                    onLongPress={()=>{this.setState({editMode: true})}}
                >
                    <View style={styles.listRow}>
                        <View style={{flex: 0.8, alignSelf: 'center'}}>
                            <Text style={styles.listText}>{rowData.address1}</Text>
                            <View style={{flexDirection: 'row'}}>
                                <Text style={styles.listText}>{rowData.city}, </Text>
                                <Text style={styles.listText}>{rowData.province}, </Text>
                                <Text style={styles.listText}>{rowData.postalCode}</Text>
                            </View>
                            <Text style={styles.listText}>{rowData.country}</Text>
                        </View>
                        <View style={{flex: 0.2, alignItems: 'flex-end', alignSelf: 'center'}}>
                            <View>
                                <TouchableHighlight
                                    style={{padding: 5, borderRadius: 4}}
                                    underlayColor="#48BBEC"
                                    onPress={() => {
                                        NavActions.addAddress()
                                    }}
                                >
                                    <MaterialIcons name="mode-edit" size={30} style={styles.arrow}/>
                                </TouchableHighlight>
                                <TouchableHighlight
                                    style={{padding: 5, borderRadius: 4}}
                                    underlayColor="#48BBEC"
                                    onPress={() => {
                                        NavActions.addAddress()
                                    }}
                                >
                                    <MaterialIcons name="delete" size={30} style={styles.arrow}/>
                                </TouchableHighlight>
                            </View>
                        </View>
                    </View>
                </TouchableHighlight>
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
                    <BackNavBar title={this.props.navBarTitle || "Delivery Address"}/>
                    <Image source={require('../assets/glow2.png')} style={styles.container}>
                        <TouchableHighlight
                            style={ListStyle.row}
                            underlayColor="#48BBEC"
                            onPress={() => {
                                NavActions.addAddress()
                            }}
                        >
                            <View style={styles.view}>
                                <MaterialIcons name="add" size={30} style={styles.arrow}/>
                                <Text style={styles.rowLabel}>Add Address</Text>
                            </View>
                        </TouchableHighlight>
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
        height: 100,
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
