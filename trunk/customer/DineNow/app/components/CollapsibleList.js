import React, {Component} from "react";
import {
    Image,
    StyleSheet,
    View,
    Text,
    Platform,
    TouchableOpacity,
    StatusBar,
    ListView,
    TouchableHighlight
} from "react-native";
var Accordion = require('react-native-accordion');

const data = [
    {
        title: "Pizza",
        content: [{Name:"Cheese Pizza", Price:11},
            {Name:"Veggie Pizza", Price:15}]
    },
    {
        title: "Drinks",
        content: [{Name:"Cheese Pizza", Price:11},
            {Name:"Veggie Pizza", Price:15}]
    },
    {
        title: "Burgers",
        content: [{Name:"Cheese Pizza", Price:11},
            {Name:"Veggie Pizza", Price:15}]
    }
];

export default class CollapsibleList extends Component {
    constructor(props) {
        super(props);
        var ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows(data),
        };
    }

    render() {
        return (
            <View style={{flex: 1}}>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={this._renderRow.bind(this)}
                />
            </View>
        );
    }

    _renderHeader(rowData) {
        return (
            <View style={{
                paddingTop: 15,
                paddingRight: 15,
                paddingLeft: 15,
                paddingBottom: 15,
                borderBottomWidth: 1,
                borderBottomColor: '#a9a9a9',
                backgroundColor: '#f9f9f9',
                flex: 1, height: 40
            }}>
                <Text>{rowData.title}</Text>
            </View>
        );
    }

    _renderContent(rowData) {
        return (
                rowData.content.map(function(item, index) {
                return(
                <View style={{
                    backgroundColor: '#31363D'
                }}
                key={index}>
                <Text style={{
                    paddingTop: 15,
                    paddingRight: 15,
                    paddingBottom: 15,
                    paddingLeft: 15,
                    color: '#fff',
                }}>{item.Name}</Text>
                </View>)
            })
        )
    }

    _renderRow(rowData) {
        return (
            <Accordion
                header={this._renderHeader(rowData)}
                content={<View>{this._renderContent(rowData)}</View>}
                duration={50}
                underlayColor="blue"
                easing="easeOutCubic"
            />
        );
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
    mb20: {
        marginBottom: 20,
        borderBottomColor: "rgba(182, 182, 182, 0.62)"
    },
    row: {
        flex: 1,
        height: 50,
        flexDirection: 'row',
        justifyContent: 'center',
        padding: 10,
        backgroundColor: '#F6F6F6',
        borderRadius: 0,
        paddingHorizontal: 10,
        alignItems: 'flex-start'
    },
    rowExpanded: {
        flex: 1,
        height: 150,
        flexDirection: 'row',
        justifyContent: 'center',
        padding: 10,
        backgroundColor: '#F6F6F6',
        borderRadius: 0,
        paddingHorizontal: 10,
        alignItems: 'flex-start'
    },
    thumb: {
        width: 64,
        height: 64,
    },
    headerText: {
        fontSize: 18,
        color: 'black',
        marginBottom: 10,
        lineHeight: 20,
    },
    smallText: {
        fontSize: 12,
        color: 'black',
        lineHeight: 18,
    },
    mediumText: {
        fontSize: 14,
        color: 'black',
        lineHeight: 20,
    },
    text: {
        color: 'black'
    },
});