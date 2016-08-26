import React, {Component} from 'react';
import Drawer from 'react-native-drawer';
import SideMenu from './SideMenu';
import {Actions, DefaultRenderer} from 'react-native-router-flux';

export default class extends Component {
    render(){
        const state = this.props.navigationState;
        const children = state.children;
        return (
            <Drawer
                styles={{ shadowColor: '#000000', shadowOpacity: 0.8, shadowRadius: 3, marginTop: 0}}
                ref="navigation"
                open={state.open}
                onOpen={()=>Actions.refresh({key:state.key, open: true})}
                onClose={()=>Actions.refresh({key:state.key, open: false})}
                type="static"
                content={<SideMenu />}
                acceptPan={false}
                tapToClose={true}
                openDrawerOffset={0.35}
                panCloseMask={0.35}
                negotiatePan={true}
                tweenHandler={Drawer.tweenPresets.parallax}>
                <DefaultRenderer navigationState={children[0]} onNavigate={this.props.onNavigate} />
            </Drawer>
        );
    }
}
