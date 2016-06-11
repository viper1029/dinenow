import React from 'react'
import { Text } from 'react-native'

import Colors from "../configs/Colors"

export var Headline = (props) => (
  <Text style={[{
    fontFamily: 'BoosterNextFY-Black',
    color: Colors.brown,
    fontSize: 20,
    textAlign: 'center',
    marginHorizontal: 20,
    marginBottom: 20,
  }, props.style]}>
    {props.children}
  </Text>
)
