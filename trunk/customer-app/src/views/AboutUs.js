import React, {Component } from 'react';
import { View, Text, StyleSheet, TextInput, Switch, PixelRatio, TouchableHighlight} from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import {Actions} from 'react-native-router-flux'
import {ComponentsStyle} from '../configs/CommonStyles';
import TextInputWithIcon from '../components/TextInputWithIcon';

export default class AboutUs extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fullName: '',
      email: '',
      password: '',
      phoneNumber: ''
    }
  }

  render() {
    return (
      <View style={[styles.container, this.props.style]}>
        <TouchableHighlight
          style={styles.firstRow}
          underlayColor="#48BBEC"
          onPress={()=>{}}
        >
          <View style={styles.view}>
            <Text style={styles.rowLabel}>Build Version</Text>
            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
          </View>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.row}
          underlayColor="#48BBEC"
          onPress={()=>{}}
        >
          <View style={styles.view}>
            <Text style={styles.rowLabel}>Build Version</Text>
            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
          </View>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.lastRow}
          underlayColor="#48BBEC"
          onPress={()=>{}}
        >
          <View style={styles.view}>
            <Text style={styles.rowLabel}>Build Version</Text>
            <MaterialIcons name="chevron-right" size={30} style={styles.arrow}/>
          </View>
        </TouchableHighlight>
      </View>
    );
  }
};

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    alignSelf: 'stretch',
    backgroundColor: '#EFEFF4'
  },
  firstRow: {

    flexDirection: 'row',
    backgroundColor: 'white',
    borderRadius: 0,
    borderWidth: 0,
    borderTopWidth: 0,
    borderColor: '#d6d7da',
    padding: 10,
    alignItems: 'center',
    alignSelf: 'stretch',
  },
  row: {
    flexDirection: 'row',
    backgroundColor: 'white',
    borderRadius: 0,
    borderWidth: 0,
    borderTopWidth: 1 / PixelRatio.get(),
    borderColor: '#d6d7da',
    padding: 10,
    alignItems: 'center',
    alignSelf: 'stretch',
  },
  lastRow: {
    flexDirection: 'row',
    backgroundColor: 'white',
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
  },
  rowLabel: {
    left: 10,
    fontSize: 15,
    flex: 1,
  },
});
