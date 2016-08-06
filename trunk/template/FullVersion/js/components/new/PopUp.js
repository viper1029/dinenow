import React from 'react'

export default PopUp = (props) => (
    <Modal
        animationType={"slide"}
        transparent={false}
        visible={props.modalVisible}
        onRequestClose={() => {alert("Modal has been closed.")}}
    >
        <View style={{marginTop: 22}}>
            <View>
                <Text>Hello World!</Text>


                    <Text>Hide Modal</Text>


            </View>
        </View>
    </Modal>
)
