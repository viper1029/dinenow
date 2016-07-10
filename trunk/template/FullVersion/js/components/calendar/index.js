/**
 * Created by kylefang on 4/28/16.
 * @flow
 */

'use strict';
//Currently using it as playground

import React, {Component} from 'react';
import {connect} from 'react-redux';
// import CodePush from 'react-native-code-push';
import { Image, View } from 'react-native';
import {openDrawer} from '../../actions/drawer';
import {popRoute} from '../../actions/route';

import {Container, Header, Title, Content, Text, Button, Icon, List, ListItem, Footer} from 'native-base';
import FooterComponent from "./../footer";
import CalendarPicker from 'react-native-calendar-picker';

import theme from '../../themes/base-theme';
import styles from './styles';

class Calendar extends Component {

    constructor(props) {
        super(props);
        this.state= {
            date: new Date(),
        };
    }

    onDateChange (date) {
        this.setState({ date: date });
    }

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
                    <Title>Calendar</Title>
                    <Button transparent onPress={this.props.openDrawer}>
                        <Icon name="ios-menu" />
                    </Button>
                </Header>

                <Content padder style={{backgroundColor: 'transparent'}}>
                    <CalendarPicker
                        selectedDate={this.state.date}
                        onDateChange={this.onDateChange.bind(this)} />
                      <Text style={{marginTop: 5, alignSelf: 'center'}} >
                      Date:  { this.state.date.toString().substr(4,12) }
                    </Text>
                </Content>
                <Footer>
                  <FooterComponent />
                </Footer>
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

export default connect(null, bindAction)(Calendar);
