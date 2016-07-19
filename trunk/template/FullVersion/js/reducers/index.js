'use strict';

import {combineReducers} from 'redux';

import drawer from './drawer';
import route from './route';
import loginReducer from './loginReducer';

export default combineReducers({
  drawer,
  route,
  loginReducer
})



