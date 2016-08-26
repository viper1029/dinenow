import React, {Component, PropTypes} from "react";
import Icon from "react-native-vector-icons/MaterialIcons";
import InlineTextInput from "./InlineTextInput";
import Theme from "../styles/Theme";
import {validate} from "validate-model";

export default class FormInput extends Component {
    focus() {
        this.refs.input.focus()
    }

    blur() {
        this.refs.input.blur()
    }

    render() {
        const {iconName, name, value} = this.props
        const {valid, messages} = validate(name, value)
        const message = (messages && messages.length > 0 ? messages[0] : null)
        return (
            <InlineTextInput
                placeholderTextColor='white'
                ref='input' // This is necessary for focus() and blur() implementation to work
                style={{borderTopWidth: 0, borderColor: 'rgba(182, 182, 182, 0.62)', backgroundColor: 'transparent'}}
                titleStyle={{color: 'dimgray'}}
                inputStyle={{color: 'white', backgroundColor: 'transparent', marginLeft: 6}}
                messageStyle={{color: '#f0ad4e'}}
                icon={ <Icon name={iconName} color={'white'} size={30}/> }
                validIcon={ <Icon name='check' size={Theme.iconFontSize} color='green'/> }
                invalidIcon={ <Icon name='clear' size={Theme.iconFontSize} color='#d9534f'/> }
                valid={valid}
                message={message}
                { ...this.props }
            />
        )
    }
}