import React from 'react'
import { Platform, View } from 'react-native'
import Colors from "../configs/Colors"
import NavBar from "./NavBar"

export var BorderedScreen = ({ children, navBar, navBarTitle }) => (
  <View style={{
    flex: 1,
    backgroundColor: Colors.blue,
    paddingTop: (!navBar && Platform.OS === 'ios' ? 20 : 0)
  }}>
    { navBar && <NavBar title={navBarTitle} /> }
    <View style={{
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: Colors.white,
      borderLeftColor: Colors.mediumPink,
      borderRightColor: Colors.mediumPink,
      borderTopColor: (Platform.OS === 'ios' ? Colors.mediumDarkPink : Colors.mediumPink),
      borderBottomColor: (Platform.OS === 'ios' ? Colors.mediumDarkPink : Colors.mediumPink),
      borderWidth: 8,
    }}>
      <View style={{
        borderColor: Colors.lightPink,
        borderWidth: 8,
        flex: 1,
        alignSelf: 'stretch',
        alignItems: 'center',
        justifyContent: 'center',
      }}>
        <View style={{
          borderColor: Colors.mediumLightBeige,
          borderWidth: 8,
          flex: 1,
          alignSelf: 'stretch',
          alignItems: 'center',
          justifyContent: 'center',
        }}>
          {children}
        </View>
      </View>
    </View>
  </View>
)