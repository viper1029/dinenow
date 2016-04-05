'use strict';
import React, {AsyncStorage} from 'react-native';
import Constants from '../configs/Constants';

export default class Storage {

  static getSessionToken () {
    return AsyncStorage.getItem(Constants.StorageKeys.SESSION_TOKEN);
  }

  static removeSessionToken () {
    return AsyncStorage.removeItem(Constants.StorageKeys.SESSION_TOKEN);
  }

  static removeUser () {
    return AsyncStorage.removeItem(Constants.StorageKeys.USER);
  }

  static getUser () {
    return AsyncStorage.getItem(Constants.StorageKeys.USER);
  }
};



