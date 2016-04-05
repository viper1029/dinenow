'use strict';
import Constants from '../configs/Constants';
import Storage from './Storage';

export function getLocalSessionToken() {
  return Storage.getSessionToken();
}

export function removeLocalSessionToken(onLogout) {
  return Storage.removeSessionToken().then((item) => {
    console.log("Session Token Removed");
  })
}