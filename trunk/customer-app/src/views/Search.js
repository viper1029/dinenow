import React, { Component } from 'react';
import { View, Text, StyleSheet,  TextInput} from 'react-native';

import Button from 'apsl-react-native-button';
import {GooglePlacesAutocomplete} from '../vendor/react-native-google-places-autocomplete/GooglePlacesAutocomplete';

const homePlace = {description: 'Home', geometry: {location: {lat: 48.8152937, lng: 2.4597668}}};
const workPlace = {description: 'Work', geometry: {location: {lat: 48.8496818, lng: 2.2940881}}};

export default class Search extends Component {
  constructor(props) {
    super(props);
    this.state = {
      query: '',
    }
  }

  render() {
    return (
      <View style={styles.container}>
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
          textInputContainer: styles.textInputContainer,
          textInput: styles.textInput,
          listView: styles.listView,
          row: styles.row,
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
          predefinedPlaces={[homePlace, workPlace]}
        />
      </View>
    );
  }

  onSearchPress() {
  }
};

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#E0E0E0'
  },
  input: {

  },
  textInputContainer: {
    margin: 15,
    height: 55,
    borderTopWidth: 0,
    borderBottomWidth: 0,
    borderRadius: 2,
    borderWidth: 0,
    backgroundColor: 'white',
  },
  textInput: {
    padding: 0,
    height: 40,
    borderRadius: 2,
    margin: 15,
    alignSelf: 'center',
    backgroundColor: '#FFFFFF',
    fontSize: 16,
  },
  listView: {
    backgroundColor: 'white',
    borderRadius: 10,
    borderWidth: 2,
    margin: 0,
  },
  description: {

  },
  listStyle: {
    borderWidth: 2,
    margin: 0
  },
  row: {
    padding: 13,
    height: 44,
    flexDirection: 'row',
  }
});
