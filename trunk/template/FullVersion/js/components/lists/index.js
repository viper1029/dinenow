/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
import {openDrawer} from '../../actions/drawer';
import {popRoute} from '../../actions/route';
// import CodePush from 'react-native-code-push';
import { Image, View } from 'react-native';

import {Container, Header, Title, Content, Text, Button, Icon, List, ListItem} from 'native-base';

import theme from '../../themes/base-theme';
import styles from './styles';

class Lists extends Component {

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
                    <Title>List</Title>
                    <Button transparent onPress={this.props.openDrawer}>
                        <Icon name="ios-menu" />
                    </Button>
                </Header>

                <Content style={{backgroundColor: 'transparent'}}>
                    <List>
                      <ListItem iconLeft>
                          <Icon name="ios-people"/>
                          <Text >Daily Stand Up</Text>
                          <Text note>10:00 AM</Text>
                      </ListItem>
                      <ListItem iconLeft>
                          <Icon name="ios-flag"/>
                          <Text>Finish list Screen</Text>
                          <Text note>By 2:00 PM</Text>
                      </ListItem>
                      <ListItem iconLeft>
                          <Icon name="ios-restaurant"/>
                          <Text>Lunch Break</Text>
                          <Text note>2:00 PM</Text>
                      </ListItem>
                      <ListItem iconLeft>
                        <Icon name="ios-megaphone"/>
                          <Text>Discussion With Client</Text>
                          <Text note>6:00 PM</Text>
                      </ListItem>
                    </List>
                </Content>
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

export default connect(null, bindAction)(Lists);
