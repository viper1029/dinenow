'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, View, Text, Platform, TouchableOpacity, StatusBar, ListView, TouchableHighlight} from "react-native";
import {getRestaurantDetails} from "../redux/actions/RestaurantDetailsActions";
import Button from "../components/Button"
import {Actions as NavActions, ActionConst} from 'react-native-router-flux'

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

class ResultsScreen extends Component {

    constructor(props) {
        super(props);
        var ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows(props.restaurantList),
        };
    }

    componentWillReceiveProps(nextProps) {
        console.log("Receiving new Properties: ", nextProps.responseSuccess);
        if (nextProps.responseSuccess) {
            NavActions.restaurantDetails();
            console.log('go to new screen')
        }

        //if (nextProps.signInError && this.props.signInError !== nextProps.signInError) {
        //    console.log(nextProps.signInError);
        //    alert(nextProps.signInError);
        //}
    }


    handleRestaurantSelection(restaurant) {
        this.props.getRestaurantDetails(restaurant.id);
    }

    _renderRow(rowData, sectionID, rowID, highlightRow:(sectionID:number, rowID:number) => void) {
        return (
            <View>
                <Button style={styles.row} onPress={() => this.handleRestaurantSelection(rowData)}>
                    <View style={{flex: 4}}>
                        <Image
                            style={{
                                width: 75,
                                height: 75,
                                borderWidth: 1,
                                borderColor: 'black'
                            }}
                            resizeMode={Image.resizeMode.contain}
                            source={require('../assets/no_image.jpg')}
                        />
                    </View>
                    <View style={{flex: 10}}>
                        <View style={{flex: 1, flexDirection: 'row', alignItems: 'center'}}><Text
                            style={styles.headerText}>{rowData.name}, </Text><Text
                            style={styles.mediumText}>{rowData.cuisine}</Text></View>
                        <Text style={styles.smallText}>Distance: {rowData.distance.toFixed(2)} km</Text>
                        <Text style={styles.smallText}>Minimum Order: {rowData.minimumOrder != null && rowData.minimumOrder || '$15'}</Text>
                        <Text style={styles.smallText}>{rowData.acceptDelivery && rowData.acceptTakeOut && 'Pickup & Delivery' || 'Pickup Only'}</Text>
                    </View>
                </Button>
            </View>
        );
    };

    _renderSeparator(sectionID, rowID, adjacentRowHighlighted) {
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
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <View>
                        <ListView
                            dataSource={this.state.dataSource}
                            renderRow={this._renderRow.bind(this)}
                            renderSeparator={this._renderSeparator}
                        />
                    </View>
                </Image>
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
        lineHeight:20,
    },
    smallText: {
        fontSize: 12,
        color: 'black',
        lineHeight:18,
    },
    mediumText: {
        fontSize: 14,
        color: 'black',
        lineHeight:20,
    },
    text: {
        color: 'black'
    },
});

var mapStateToProps = function (state) {
    return {
        restaurantList: state.search.restaurantList,
        responseSuccess: state.restaurantDetails.responseSuccess,
    };
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        getRestaurantDetails
    }, dispatch);

};


module.exports = connect(mapStateToProps, mapDispatchToProps)(ResultsScreen);
