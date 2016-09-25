import React, {Component} from 'react'
import {Platform, View, TouchableOpacity, Text, StyleSheet, TouchableHighlight, ListView} from 'react-native'
import Icon from "./Icon"
import Theme from "../styles/Theme";
import Modal from 'react-native-simple-modal';

const data = [
    {
        id: 1,
        name: "Cheese",
    },
    {
        id: 2,
        name: "Chicken",
    }
];

export default class MultiselectPicker extends Component {

    constructor(props) {
        super(props);
        var ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows(data),
            offset: 0,
            modalVisible: true
        };
    }

    _renderRow(rowData, sectionID, rowID, highlightRow:(sectionID:number, rowID:number) => void) {
        return (
            <View>
                <Button style={styles.row} onPress={() => this.handleRestaurantSelection(rowData)}>
                    <View style={{flex: 10}}>
                        <View style={{flex: 1, flexDirection: 'row', alignItems: 'center'}}>
                            <Text style={styles.headerText}>{rowData.name}, </Text>
                        </View>
                    </View>
                </Button>
            </View>
        );
    };

    _renderSeparator(sectionID, rowID, adjacentRowHighlighted) {
        return (
            <View
                key={`${sectionID}-${rowID}`}
                style={{
                    height: adjacentRowHighlighted ? 4 : 1,
                    backgroundColor: adjacentRowHighlighted ? '#3B5998' : '#CCCCCC',
                }}
            />
        );
    }

    render() {
        const {leftButton, rightButton, onLeftPress, children, title, iconStyle, navBarStyle} = this.props
        return (
            <View>
                <Modal
                    overlayBackground={'rgba(0, 0, 0, 0.75)'}
                    offset={this.state.offset}
                    open={this.state.modalVisible}
                    modalDidOpen={() => console.log('modal did open')}
                    modalDidClose={() => this.setState({modalVisible: false})}
                    style={{alignItems: 'center'}}
                    modalStyle={{padding: 0, borderRadius: 4}}>
                    <View style={{
                        alignItems: 'center'
                    }}>
                        <ListView
                            dataSource={this.state.dataSource}
                            renderRow={this._renderRow.bind(this)}
                            renderSeparator={this._renderSeparator}
                        />
                    </View>
                </Modal>
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
        position: 'absolute', top: 0, flex: 1, alignSelf: 'stretch', right: 0, left: 0
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
