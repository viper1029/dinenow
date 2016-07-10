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

import {Container, Header, Title, Content, Text, Button, Icon, List, ListItem, InputGroup, Input, Card, CardItem} from 'native-base';


import theme from '../../themes/base-theme';
import styles from './styles';

class Form extends Component {

    popRoute() {
        this.props.popRoute();
    }

    render() {
        return (
            <Container theme={theme} style={{backgroundColor: '#384850'}} >
              <Image source={require('../../../images/glow2.png')} style={styles.container} >
                <Header>
                    <Button transparent onPress={() => this.popRoute()}>
                        <Icon name="ios-arrow-back" />
                    </Button>
                    <Title>Form</Title>
                    <Button transparent onPress={this.props.openDrawer}>
                        <Icon name="ios-menu" />
                    </Button>
                </Header>

                <Content padder style={{backgroundColor: 'transparent'}} >
                  <Card transparent foregroundColor="#000">
                    <CardItem header>
                      <Text>Input with icon
                      </Text>
                    </CardItem>
                    <CardItem>
                      <List>
                        <ListItem>
                          <InputGroup>
                            <Icon name="ios-person" style={{color: '#000'}} />
                            <Input placeholder="EMAIL" />
                          </InputGroup>
                        </ListItem>
                      </List>
                    </CardItem>
                    <CardItem header>
                      <Text>Input
                      </Text>
                    </CardItem>
                    <CardItem>
                      <List>
                        <ListItem>
                          <InputGroup>
                              <Input placeholder="Name" />
                          </InputGroup>
                        </ListItem>
                      </List>
                    </CardItem>
                    <CardItem header>
                      <Text>Input with inline label
                      </Text>
                    </CardItem>
                    <CardItem >
                      <List>
                        <ListItem>
                          <InputGroup >
                              <Input inlineLabel label="ALIAS" placeholder="John Doe" />
                          </InputGroup>
                        </ListItem>
                      </List>
                    </CardItem>
                    <CardItem header>
                      <Text>Input with stacked label
                      </Text>
                    </CardItem>
                    <CardItem>
                      <List>
                        <ListItem>
                          <InputGroup >
                              <Input stackedLabel label="Address Line 1" placeholder="Address" />
                          </InputGroup>
                        </ListItem>
                      </List>
                    </CardItem>
                  </Card>
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

export default connect(null, bindAction)(Form);
