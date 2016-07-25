import React, {Component} from 'react'
import {Platform, View, TouchableOpacity, Text, StyleSheet, TouchableHighlight} from 'react-native'
import Icon from "./Icon"
import Theme from "../../theme/Theme";

export default class NavBar extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {leftButton, rightButton, onLeftPress, children, title} = this.props
        return (
            <View style={styles.navBar}>
                <View style={styles.leftContainer}>
                    {leftButton &&
                    <TouchableHighlight underlayColor={'grey'} onPress={onLeftPress} style={styles.button}>
                        <View>
                            <Icon name="keyboard-arrow-left" style={{
                                color: 'black',
                                fontSize: 36,
                            }}/>
                        </View>
                    </TouchableHighlight>
                    }
                </View>

                <View style={styles.titleContainer}>
                    {title &&
                    <Text style={styles.title}>{title}</Text>
                    }
                </View>

                <View style={styles.rightContainer}>
                    {rightButton &&
                    <Text>Right</Text>
                    }
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    navBar: {
        marginTop: (Platform.OS == 'ios' ? 20 : 0),
        backgroundColor: Theme.toolbarDefaultBg,
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 2},
        shadowOpacity: 0.1,
        shadowRadius: 1.5,
        elevation: 2,
        height: Theme.toolbarHeight,
        padding: 2,
        paddingTop: 3,
        paddingBottom: 3,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    leftContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignSelf: 'stretch',
        alignItems: 'center',
        marginLeft: 2,
    },
    titleContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignSelf: 'stretch',
    },
    rightContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        alignSelf: 'stretch',
        marginRight: 2,
    },
    button: {
        backgroundColor: Theme.mainGreen,
        paddingRight: 3,
        paddingLeft: 3,
        margin: 0,
        borderRadius: 4,
        alignSelf: 'stretch',
        alignItems: 'center',
        justifyContent: 'center',
    },
    title: {
        fontFamily: 'BoosterNextFY-Black',
        fontSize: 18,
        //lineHeight: (Platform.OS === 'ios' ? 12 : 20),//
        color: 'white',
        fontWeight: 'bold',
    }
});
