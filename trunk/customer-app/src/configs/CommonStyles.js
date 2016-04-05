'use strict';
import React, { Platform } from 'react-native'

export var AppStyle = {
  Colors: {
    PRIMARY: "#e73b3b",
    SECONDARY: "#efefef",
  },
  Dimensions: {
    NAVIGATION_TEXT: "12px",
    VIEW_TITLE: "20px",
    VIEW_SUBTITLE: "16px"
  }
};

export var ComponentsStyle = {
  NavBar: {
    NavigationBarStyle: {
      backgroundColor: "#e73b3b"
    },
    TitleStyle: {
      color: '#58BBEC',
      alignItems: 'center',
      alignSelf: 'center',
      justifyContent: 'center'
    },
    ButtonIconStyle: {
      tintColor: '#48BBEC',
      marginTop: (Platform.OS === 'ios' ? 12 : 18),
      marginLeft: 12
    },
    ButtonTextStyle: {
      color: '#58BBEC'
    }
  },
  Buttons: {
    LargeButton: {
      Colors: {
        BLUE: '#48BBEC',
        BLUE_BUTTON_PRESS: '#99d9f4',
        BLUE_BUTTON_OUTLINE: '#48BBEC'
      },
      ButtonStyle: {
        alignItems: 'center',
        alignSelf: 'stretch',
        backgroundColor: '#48BBEC',
        borderColor: '#48BBEC',
        borderWidth: 1,
        borderRadius: 3,
        padding: 12,
        margin: 20
      },
      TextStyle: {
        fontSize: 16
      }
    }
  },
  TextInput: {
    TextInputWithIcon: {
      inputContainerStyle: {
        flexDirection: 'row',
        alignSelf: 'stretch',
        alignItems: 'center'
      },
      textInputStyle: {
        flex: 1,
        marginRight: 30,
        height: 50,
        borderColor: 'gray',
        borderWidth: 1,
        margin: 3,
      },
      iconStyle: {
        marginLeft: 15,
        marginRight: 15
      }
    }
  }
};