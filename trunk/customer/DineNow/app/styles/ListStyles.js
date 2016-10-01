import {PixelRatio, StyleSheet} from "react-native";

export default styles = StyleSheet.create({
    row: {
        flexDirection: 'row',
        backgroundColor: 'transparent',
        borderRadius: 0,
        borderWidth: 0,
        borderTopWidth: 0,
        borderColor: '#d6d7da',
        padding: 10,
        alignItems: 'center',
        alignSelf: 'stretch',
        borderBottomWidth: 1 / PixelRatio.get(),
    },
    rowContent: {
        flex: 1,
        flexDirection: 'row',
        alignSelf: 'stretch',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: 30,
    },
    arrow: {
        justifyContent: 'flex-end',
        alignSelf: 'flex-end',
        alignItems: 'flex-end',
        color: 'white'
    },
    rowLabel: {
        left: 10,
        fontSize: 15,
        flex: 1,
        color: 'white'
    }
});