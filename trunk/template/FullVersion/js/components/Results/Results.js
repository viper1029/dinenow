'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, Platform, TouchableOpacity, StatusBar, ListView, TouchableHighlight} from "react-native";
import {replaceRoute, popRoute} from "../../actions/route";
import {openDrawer} from "../../actions/drawer";
import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View} from "native-base";
import theme from "../../theme/Theme";
import styles from "./styles";
import NavBar from "../new/NavBar";
import {GooglePlacesAutocomplete} from "../new/GooglePlacesAutoComplete";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";
import Button from "../new/Button";

const data = [
    {
        id: 1,
        name: "Bob's Grill",
        thumbUrl: '',
        cuisine: 'American',
        distance: 10,
        minimumOrder: 15,
        orderType: 'Pickup Only'
    },
    {
        id: 2,
        name: "Fish & Chips",
        thumbUrl: '',
        cuisine: 'Japanese',
        distance: 20,
        minimumOrder: 20,
        orderType: 'Pickup & Delivery'
    }
];

class Results extends Component {

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
                <Button style={styles.row}>
                    <View style={{flex: 4}}>
                        <Image
                            style={{
                                width: 75,
                                height: 75,
                                borderWidth: 1,
                                borderColor: 'black'
                            }}
                            resizeMode={Image.resizeMode.contain}
                            source={require('../../../images/no_image.jpg')}
                        />
                    </View>
                    <View style={{flex: 10}}>
                        <View style={{flex: 1, flexDirection: 'row', alignItems: 'center'}}><Text style={styles.headerText}>{rowData.name}, </Text><Text style={styles.mediumText}>{rowData.cuisine}</Text></View>
                        <Text style={styles.smallText}>Distance: {rowData.distance} km</Text>
                        <Text style={styles.smallText}>Minimum Order: {rowData.minimumOrder}</Text>
                        <Text style={styles.smallText}>{rowData.orderType}</Text>
                    </View>
                </Button>
            </View>
        );
    };

    _renderSeparator(sectionID:number, rowID:number, adjacentRowHighlighted:bool) {
        return (
            <View
                key={`${sectionID}-${rowID}`}
                style={{
                    height: adjacentRowHighlighted ? 4 : 1,
                    backgroundColor: adjacentRowHighlighted ? '#3B5998' : '#CCCCCC',
                }}
            />
        );
    }

    render() {
        return (
            <View theme={theme} style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.openDrawer()} title="Search Results" leftButton={true}/>
                    <View>
                        <ListView
                            dataSource={this.state.dataSource}
                            renderRow={this._renderRow}
                            renderSeparator={this._renderSeparator}
                        />
                    </View>
                </Image>
            </View>
        )
    }
}

var mapStateToProps = function (state) {
    return {};
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        popRoute,
        openDrawer,
    }, dispatch);

};


module.exports = connect(mapStateToProps, mapDispatchToProps)(Results);
