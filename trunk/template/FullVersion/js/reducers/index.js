'use strict';

import {combineReducers} from 'redux';

import drawer from './drawer';
import route from './route';
import auth from './auth';

export default combineReducers({
  drawer,
  route,
  auth
})



