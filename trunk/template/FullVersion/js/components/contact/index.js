/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component}from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { Image } from 'react-native';
import {openDrawer} from '../../actions/drawer';
import {popRoute} from '../../actions/route';

import AllContacts from "./allContacts";
import Favourites from "./favourites";
import Recent from "./recent";

import {Container, Header, Title, Content, Text, Button, Icon, Card, CardItem, Thumbnail, View, Tabs } from 'native-base';

import theme from '../../themes/base-theme';
import styles from './styles';

class Contacts extends Component {

    popRoute() {
        this.props.popRoute();
    }

    render() {
        return (
            <Container theme={theme} style={{backgroundColor: '#384850'}}>
              <Image source={require('../../../images/glow2.png')} style={styles.container} >
                <Header>
                    <Button transparent onPress={() => this.popRoute()}>
                        <Icon name="ios-arrow-back" />
                    </Button>
                    <Title>Contacts</Title>
                    <Button transparent onPress={this.props.openDrawer}>
                        <Icon name="ios-menu" />
                    </Button>
                </Header>

                <View >
                    <Tabs >
                      <AllContacts tabLabel="All" />
                      <Favourites tabLabel="Favourites" />
                      <Recent tabLabel="Recent" />
                    </Tabs>
                </View>
              </Image>
            </Container>
        )
    }
}

function bindAction(dispatch) {
    return {
        openDrawer: ()=>dispatch(openDrawer()),
        popRoute: () => dispatch(popRoute())
    }
}

export default connect(null, bindAction)(Contacts);
