'use strict';

import React, {Component} from "react";
import {Image, View, Text, StyleSheet, Platform, TouchableOpacity, StatusBar} from "react-native";
import ScrollableTabView, { ScrollableTabBar } from 'react-native-scrollable-tab-view';
import { TabViewAnimated, TabViewPage, TabBarTop } from 'react-native-tab-view';
import Accordion from 'react-native-collapsible/Accordion';
import CollapsibleList from './../components/CollapsibleList'
import {BackNavBar} from "./../components/NavBars";

const SECTIONS = [
    {
        title: 'First',
        content: 'Lorem ipsum...',
    },
    {
        title: 'Second',
        content: 'Lorem ipsum...',
    }
];

export default class RestaurantDetails extends Component {

    state = {
        index: 0,
        routes: [
            { key: '1', title: 'Menu' },
            { key: '2', title: 'Info' },
        ],
    };

    constructor(props) {
        super(props);

    }

    _handleChangeTab = (index) => {
        this.setState({ index });
    };

    _renderHeader = (props) => {
        return (
            <TabBarTop
                {...props}
                pressColor='rgba(0, 0, 0, .2)'
                indicatorStyle={styles.indicator}
                style={styles.tabbar}
            />
        );
    };

    _renderScene = ({ route }) => {
        switch (route.key) {
            case '1':
                return <View style={[ styles.page, { backgroundColor: '#ff4081' } ]}>
                    <CollapsibleList />
                    </View>;
            case '2':
                return <View style={[ styles.page, { backgroundColor: '#673ab7' } ]} />;
            default:
                return null;
        }
    };

    _renderPage = (props) => {
        return <TabViewPage {...props} renderScene={this._renderScene} />;
    };

    render() {
        return (
            <View style={{flex: 1, backgroundColor: '#384850'}}>
                <BackNavBar title="Delivery Address" navBarStyle={{shadowRadius: 0,
                    shadowOffset: {width: 0, height: 0},
                    shadowOpacity: 0,
                    elevation: 0,}}/>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <TabViewAnimated
                        style={{flex:1}}
                        navigationState={this.state}
                        renderScene={this._renderPage}
                        renderHeader={this._renderHeader}
                        onRequestChangeTab={this._handleChangeTab}
                    />
                </Image>
            </View>
        )
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
        marginTop: (Platform.OS === 'ios') ? 64 : 54
    },
    page: {
        flex: 1
    },
    tabbar: {
        backgroundColor: '#00c497',
    },
    indicator: {
        backgroundColor: '#ffeb3b',
    },
});
