'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";
import {replaceRoute, popRoute} from "../../actions/route";
import {Container, Header, Title, Content, Text, Icon, InputGroup, Input, View} from "native-base";
import theme from "../../theme/Theme";
import styles from "./styles";
import NavBar from "../new/NavBar";
import {GooglePlacesAutocomplete} from "../new/GooglePlacesAutoComplete";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";

class Search extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            offset: 0,
        };
    }

    componentWillReceiveProps(nextProps) {
        console.log("Receiving new Properties: ", nextProps.signedIn);
        if (nextProps.signedIn) {
            this.props.replaceRoute('home');
        }

        if (nextProps.signInError && this.props.signInError !== nextProps.signInError) {
            console.log(nextProps.signInError);
        }
    }

    render() {
        return (
            <View theme={theme} style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../../../images/glow2.png')} style={styles.container}>
                    <NavBar onLeftPress={() => this.props.popRoute()} title="Search" leftButton={true}/>
                    <GooglePlacesAutocomplete
                        enablePoweredByContainer={false}
                        placeholder='Enter Address'
                        minLength={2} // minimum length of text to search
                        autoFocus={false}
                        fetchDetails={true}
                        onPress={(data, details = null) => { // 'details' is provided when fetchDetails = true
                            console.log(data);
                            console.log(details);
                        }}
                        getDefaultValue={() => {
                            return ''; // text input default value
                        }}
                        query={{
                            // available options: https://developers.google.com/places/web-service/autocomplete
                            key: 'AIzaSyBpwX1y-lK6GoA-KjcY0Y9U6l5lSLplxtE',
                            language: 'en', // language of the results
                            types: '(cities)', // default: 'geocode'
                        }}
                        styles={{
                            description: {
                                fontSize: 16,
                                color: 'black'
                            },
                            predefinedPlacesDescription: {
                                fontSize: 16,
                                color: 'grey'
                            },
                        }}
                        currentLocation={true} // Will add a 'Current location' button at the top of the predefined places list
                        currentLocationLabel="Current location"
                        nearbyPlacesAPI='GooglePlacesSearch' // Which API to use: GoogleReverseGeocoding or GooglePlacesSearch
                        GoogleReverseGeocodingQuery={{
                            // available options for GoogleReverseGeocoding API : https://developers.google.com/maps/documentation/geocoding/intro
                        }}
                        GooglePlacesSearchQuery={{
                            // available options for GooglePlacesSearch API : https://developers.google.com/places/web-service/search
                            rankby: 'distance',
                            types: 'food',
                        }}
                        filterReverseGeocodingByTypes={['locality', 'administrative_area_level_3']} // filter the reverse geocoding results by types - ['locality', 'administrative_area_level_3'] if you want to display only cities
                    />

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
        replaceRoute,
    }, dispatch);

};


module.exports = connect(mapStateToProps, mapDispatchToProps)(Search);
