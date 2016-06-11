import React from 'react'
import MaterialIcon from 'react-native-vector-icons/MaterialIcons'
import FontAwesomeIcon from 'react-native-vector-icons/FontAwesome'
import Colors from "../configs/Colors"

export var Icon = ({name, set, size, color, style}) => {
  let SetIcon
  if ( set === 'FontAwesome' ) {
    SetIcon = FontAwesomeIcon
  } else {
    SetIcon = MaterialIcon
  }
  return(
    <SetIcon
      name={name || 'account-circle'}
      size={size || 18}
      color={color || Colors.brown}
      style={[{
        // marginHorizontal: 10,
      }, style]}
    />
  )
}
