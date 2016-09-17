'use strict';

import React, {Component} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Image, View, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";
import {GooglePlacesAutocomplete} from "../components/GooglePlacesAutoComplete";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";
import {search} from "../redux/actions/SearchActions";
import {Actions as NavActions, ActionConst} from 'react-native-router-flux'

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
        console.log("Receiving new Properties: ", nextProps.searchSuccess);
        if (nextProps.searchSuccess) {
            NavActions.results({type: ActionConst.RESET})
            console.log('go to new screen')
        }

        //if (nextProps.signInError && this.props.signInError !== nextProps.signInError) {
        //    console.log(nextProps.signInError);
        //    alert(nextProps.signInError);
        //}
    }


    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <GooglePlacesAutocomplete
                        enablePoweredByContainer={false}
                        placeholder='Enter Address'
                        minLength={2} // minimum length of text to search
                        autoFocus={false}
                        fetchDetails={true}
                        onPress={(data, details = null) => { // 'details' is provided when fetchDetails = true
                            console.log(data);
                            console.log(details);
                            this.props.search(details.geometry.location.lat, details.geometry.location.lng, 25);
                        }}
                        getDefaultValue={() => {
                            return ''; // text input default value
                        }}
                        query={{
                            // available options: https://developers.google.com/places/web-service/autocomplete
                            key: 'AIzaSyBpwX1y-lK6GoA-KjcY0Y9U6l5lSLplxtE',
                            language: 'en', // language of the results
                           // types: '(address)', // default: 'geocode'
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
                            //types: 'food',
                        }}
                        filterReverseGeocodingByTypes={['locality', 'administrative_area_level_3']} // filter the reverse geocoding results by types - ['locality', 'administrative_area_level_3'] if you want to display only cities
                    />

                </Image>
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

var mapStateToProps = function (state) {
    return {
        searchSuccess: state.search.searchSuccess,
    };
};

var mapDispatchToProps = function (dispatch) {
    return bindActionCreators({
        search
    }, dispatch);

};


module.exports = connect(mapStateToProps, mapDispatchToProps)(Search);
