import React from "react";
import SimpleModal from "react-native-simple-modal";
import {View, Text, ScrollView} from "react-native";
import {KeyboardAwareScrollView, } from 'react-native-keyboard-aware-scroll-view'

export default Modal = (props) => (
    <SimpleModal
        overlayBackground={'rgba(0, 0, 0, 0.75)'}
        offset={props.offset || 0}
        open={props.visible}
        modalDidOpen={props.onModalOpen}
        modalDidClose={props.onModalClose}
        style={{alignItems: 'center'}}
        modalStyle={{padding: 0, borderRadius: 4}}>
        <View style={{flex: 1, minHeight: 150, maxHeight:500}}>
            {props.title && <View style={{borderBottomWidth: 1, borderColor: 'rgba(0, 0, 0, 0.1)'}}><Text style={{
                fontSize: 18,
                alignSelf: 'center',
                margin: 10
            }}>{props.title}</Text></View>}
            <KeyboardAwareScrollView style={{flex: 1, maxHeight: 500}}>
            {props.children}
            </KeyboardAwareScrollView>
            <View style={{flexDirection: 'row'}}>
            {props.onCancelPress && <View style={{flex: 1, borderTopWidth: 1, borderRightWidth: 1, flexDirection: 'row', borderColor: 'rgba(0, 0, 0, 0.1)', flexDirection: 'row', alignItems: 'stretch'}}>
                <Button
                    style={{backgroundColor: "#F5F5F5",
                        flex: 1,
                        paddingRight: 10,
                        paddingLeft: 10,
                        paddingTop: 10,
                        paddingBottom: 10,
                        width: 50,
                        padding: 0,
                        elevation: 0,
                        borderTopLeftRadius: 0,
                        borderTopRightRadius: 0,
                        alignSelf: 'flex-end'}}
                    onPress={props.onCancelPress}>
                    Cancel
                </Button>
            </View>}
            {props.onOkayPress && <View style={{flex:1, borderTopWidth: 1, flexDirection: 'row', borderColor: 'rgba(0, 0, 0, 0.1)', flexDirection: 'row', alignItems: 'stretch'}}>
                <Button
                    style={{backgroundColor: "#F5F5F5",
                        flex: 1,
                        paddingRight: 10,
                        paddingLeft: 10,
                        paddingTop: 10,
                        paddingBottom: 10,
                        width: 50,
                        padding: 0,
                        elevation: 0,
                        borderTopLeftRadius: 0,
                        borderTopRightRadius: 0,
                        alignSelf: 'flex-end'}}
                    onPress={props.onOkayPress}>
                    Okay
                </Button>
            </View>}
            </View>
        </View>

    </SimpleModal>
)