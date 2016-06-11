import React, { Component } from 'react'
import { Text, View } from 'react-native'
import Package from '../../package.json'
/*import Config from 'rect-native-config'*/

import {SimpleScreen} from "../components/SimpleScreen"
import {Sentence} from "../components/Sentence"

export default class About extends Component {
  render() {
    return (
      <View>
        <SimpleScreen headline="Sobre" navBar={true} navBarTitle="Sobre">

        </SimpleScreen>
      </View>

    )
  }
}
