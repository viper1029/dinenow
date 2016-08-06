import React from 'react'
import ReactNativeButton from 'apsl-react-native-button'

export default Button = (props) => (
    <ReactNativeButton
        {...props}
        activityIndicatorColor='#fff'
        textStyle={[{
            textAlign: 'center',
            color: '#00c497',
            fontSize: 15,
            fontFamily: 'BoosterNextFY-Black',
            lineHeight: 20,
        }, props.textStyle]}
        disabledStyle={[{
            backgroundColor: 'grey',
            borderColor: 'grey',
        }, props.disabledStyle]}
        style={[{
            alignSelf: 'stretch',
            justifyContent: 'space-around',
            flexDirection: 'row',
            alignItems: 'center',
            borderWidth: 0,
            borderRadius: 4,
            paddingHorizontal: 30,
            paddingVertical: 8,
            backgroundColor: '#fff',
            marginBottom: 0,
            elevation: 2,
            shadowColor: '#000',
            shadowOffset: {width: 0, height: 2},
            shadowOpacity: 0.1,
            shadowRadius: 1.5
        }, props.style]}
    >
        {props.children}
    </ReactNativeButton>
)
