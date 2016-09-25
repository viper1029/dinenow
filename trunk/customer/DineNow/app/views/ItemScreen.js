'use strict';
import React, {Component} from "react";
import {
    Image,
    View,
    StyleSheet,
    Platform,
    TouchableOpacity,
    StatusBar,
    Text,
    Picker,
    TouchableHighlight,
    PixelRatio,
    ScrollView,
Dimensions,
TextInput
} from "react-native";
import NumberInput from "../components/NumberInput";
import Modal from 'react-native-simple-modal';
import MultipleChoice from '../components/MultipeChoice'
import Button from "../components/Button";
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view'
var height = (Dimensions.get('window').height) - ((Platform.OS === 'ios') ? 64 : 54) - StatusBar.currentHeight;
import {BackNavBar} from "./../components/NavBars";

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

export default class ItemScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            size: "regular",
            modalVisible: false,
            offset: 0,
        };
    }

    render() {
        return (
            <View style={{flex: 1}}>
                <BackNavBar title="Delivery Address"/>
                <Image source={require('../assets/glow2.png')} style={styles.container}>
                    <ScrollView contentContainerStyle={{height:height}}>
                    <View style={{
                        flexDirection: 'row',
                        justifyContent: 'space-between',
                        padding: 20,
                        paddingTop: 15,
                        paddingBottom: 15,
                    }}>
                        <Text style={{fontSize: 16, fontWeight: 'bold'}}>
                            Chicken Wings
                        </Text>
                        <Text>
                            $9.98
                        </Text>
                    </View>
                    <View style={{padding: 20, paddingTop: 10, paddingBottom: 10}}>
                        <Text>
                            Item description here
                        </Text>
                    </View>
                    <View style={styles.section}>
                        <View style={{flex: 0.5}}>
                            <Text style={{fontSize: 16}}>
                                Quantity
                            </Text>
                        </View>
                        <View style={{flex: 0.5, alignItems: 'flex-end'}}>
                            <NumberInput/>
                        </View>
                    </View>
                    <View style={styles.section}>
                        <View style={{flex: 0.5}}>
                            <Text style={{fontSize: 16}}>
                                Size
                            </Text>
                        </View>
                        <View style={{flex: 0.5, alignItems: 'flex-end'}}>
                            <Button onPress={()=> {
                                this.setState({
                                    modalVisible: true
                                })
                            }}>Select Size</Button>
                        </View>
                    </View>
                    <View style={styles.section}>
                        <View style={{flex: 0.5}}>
                            <Text style={{fontSize: 16}}>
                                Addons
                            </Text>
                        </View>
                        <View style={{flex: 0.5, alignItems: 'flex-end'}}>
                            <Button onPress={()=> {
                                this.setState({
                                    modalVisible: true
                                })
                            }}>Select Addon</Button>
                        </View>
                    </View>
                    <View style={styles.section}>
                        <View style={{flex: 1, borderWidth: 1 / PixelRatio.get(), borderColor: '#d6d7da', borderRadius: 4}}>
                            <TextInput
                                style={{textAlign: "left", textAlignVertical: "top", height: 75}}
                                multiline={true}
                                underlineColorAndroid="transparent"
                                placeholder="Additonal Instructions..."
                            ></TextInput>
                        </View>
                    </View>
                    <View style={{flex: 1, justifyContent: 'flex-end'}}>
                        <Button style={{height: 60, backgroundColor: '#00c497', borderRadius: 0}}
                                textStyle={{color: 'white', fontSize: 18, fontWeight: 'bold'}}
                                onPress={()=> {}}>Add Item</Button>
                    </View></ScrollView>
                </Image>
                <Modal
                    overlayBackground={'rgba(0, 0, 0, 0.75)'}
                    offset={this.state.offset}
                    open={this.state.modalVisible}
                    modalDidOpen={() => console.log('modal did open')}
                    modalDidClose={() => this.setState({modalVisible: false})}
                    style={{alignItems: 'center'}}
                    modalStyle={{padding: 0, borderRadius: 4}}>
                    <View>
                        <MultipleChoice
                            options={[
                                {title: "Cheese", price: "$11"},
                                {title: "Chicken", price: "$15"}
                            ]}
                            style={{marginTop: 4}}
                            selectedOptions={['Lorem ipsum']}
                            maxSelectedOptions={2}
                            onSelection={(option)=> {
                            }}
                        />
                        <Button
                            style={{margin: 0, padding: 0}}
                            onPress={()=> {
                                this.setState({modalVisible: false})
                            }}>
                            Okay
                        </Button>
                    </View>
                </Modal>
            </View>
        )
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        width: null,
        height: null,
        marginTop: (Platform.OS === 'ios') ? 64 : 54,
    },
    section: {
        padding: 20,
        paddingTop: 10,
        paddingBottom: 10,
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 1 / PixelRatio.get(),
        borderColor: '#d6d7da'
    }
});
