'use strict';

import React, {Component} from 'React';
import NavigationRouter from './NavigationRouter';
import ConfigureStore from './redux/ConfigureStore'
import {Provider} from 'react-redux';
import {Text} from 'react-native';

class Root extends Component {

    constructor() {
        super();
        this.state = {
            store: ConfigureStore(),
        };
    }

    render() {
        return (
            <Provider store={this.state.store}>
                <NavigationRouter store={this.state.store}/>
            </Provider>
        );
    }
}

export default Root;
