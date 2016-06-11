import React from 'react'
import { Text } from 'react-native'

import Colors from "../configs/Colors"

export var Sentence = (props) => (
  <Text style={[{
    color: Colors.brown,
    fontFamily: 'OpenSans',
    fontSize: 12,
  }, props.style]}>
    {props.children}
  </Text>
);
