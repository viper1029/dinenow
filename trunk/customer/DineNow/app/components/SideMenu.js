import React, {Component} from 'react';
import {View, Text, StyleSheet, TouchableNativeFeedback} from 'react-native';
import Button from "../components/Button";
import Icon from "react-native-vector-icons/MaterialIcons";
import {Actions as NavActions, ActionConst} from 'react-native-router-flux'

class SideMenu extends Component {

    goToAbout() {
        NavActions.about;
        NavActions.refresh({key: 'drawer', open: value => !value })
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.topContainer}>
                    <Button onPress={
                        () => {NavActions.search, NavActions.refresh({key: 'drawer', open: value => !value })}}
                            background={TouchableNativeFeedback.Ripple('#424242')} style={styles.button}
                            textStyle={styles.text}>
                        <Icon name="search" color={'white'} size={30}/>
                        Find Restaurants
                    </Button>
                    <Button onPress={
                        () => {NavActions.search, NavActions.refresh({key: 'drawer', open: value => !value })}}
                            background={TouchableNativeFeedback.Ripple('#424242')} style={styles.button}
                            textStyle={styles.text}>
                        <Icon name="account-box" color={'white'} size={30}/>
                        Account
                    </Button>
                    <Button onPress={
                        () => {NavActions.search, NavActions.refresh({key: 'drawer', open: value => !value })}}
                            background={TouchableNativeFeedback.Ripple('#424242')} style={styles.button}
                            textStyle={styles.text}>
                        <Icon name="feedback" color={'white'} size={30}/>
                        Send Feedback
                    </Button>
                </View>
                <View style={styles.bottomContainer}>
                    <Button onPress={this.goToAbout.bind(this)}
                            background={TouchableNativeFeedback.Ripple('#424242')} style={styles.button}
                            textStyle={styles.text}>
                        <Icon name="info" color={'white'} size={30}/>
                        About
                    </Button>
                    <Button onPress={
                        () => {NavActions.intro({type: ActionConst.RESET}); NavActions.refresh({key: 'drawer', open: value => !value })}}
                            background={TouchableNativeFeedback.Ripple('#424242')} style={styles.button}
                            textStyle={styles.text}>
                        <Icon name="power-settings-new" color={'white'} size={30}/>
                        Logout
                    </Button>
                </View>
            </View>
        );
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#252A30'
    },
    topContainer: {
        flex:2,
    },
    bottomContainer: {
        flex: 1,
        justifyContent: 'flex-end',
    },
    button: {
        paddingLeft: 10,
        height: null,
        backgroundColor: 'transparent',
        alignSelf: 'stretch',
        borderRadius: 0,
        elevation: 0,
        shadowOffset: {width: 0, height: 0},
        shadowOpacity: 0,
        shadowRadius: 0,
        paddingTop: 15,
        paddingBottom: 15,
        justifyContent: 'flex-start',
    },
    text: {
        color: 'white',
        fontSize: 16,
        marginLeft: 10
    }
});


export default SideMenu;
