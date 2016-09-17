'use strict';

import {combineReducers} from 'redux';
import auth from './reducers/Auth';
import search from './reducers/Search';
import network from './reducers/Network';

export default combineReducers({
    auth,
    network,
    search
})



