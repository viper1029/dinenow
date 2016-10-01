import React from 'react'
import NavBar from './NavBar'
import {Actions as NavActions} from 'react-native-router-flux'

export const DrawerNavBar = (props) => (<NavBar
    onLeftPress={() => {NavActions.refresh({key: 'drawer', open: value => !value })}}
    title={props.title}
    leftButton={true}
    leftIconName="menu"
    iconStyle={{
        fontSize: 32,
    }}
/>)

export const BackNavBarWithAdd = (props) => (<NavBar
    onLeftPress={NavActions.pop}
    onRightPress={() => {props.handleRightPress()}}
    title={props.title}
    leftButton={true}
    rightButton={true}
    leftIconName="menu"
    rightIconName="add"
    iconStyle={{
        fontSize: 32,
    }}
/>)

export const BackNavBar = (props) => (<NavBar
    onLeftPress={NavActions.pop}
    title={props.title}
    leftButton={true}
    leftIconName={props.leftIconName}
    rightButton={props.rightButton}
    rightIconName={props.rightIconName}
    onRightPress={() => {props.onRightPress()}}
    iconStyle={{
        fontSize: 32,
    }}
    navBarStyle={props.navBarStyle}
/>)

export const BackToHomeNavBar = (props) => (<NavBar
    onLeftPress={NavActions.drawer}
    title={props.title}
    leftButton={true}
    iconStyle={{
        fontSize: 32,
    }}
/>)

export const BackNavBarNoShadow = (props) => (<NavBar
    onLeftPress={NavActions.pop}
    title={props.title}
    leftButton={true}
    iconStyle={{
        fontSize: 32,
    }}
    navBarStyle={{shadowRadius: 0,
        shadowOffset: {width: 0, height: 0},
        shadowOpacity: 0,
        elevation: 0,}}
/>)