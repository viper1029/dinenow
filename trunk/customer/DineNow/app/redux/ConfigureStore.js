'use strict';

import {createStore, applyMiddleware, compose} from 'redux'
import thunk from 'redux-thunk'
import reducer from './CombinedReducers'
import {autoRehydrate, persistStore} from 'redux-persist'
import {AsyncStorage} from 'react-native'
import devTools from 'remote-redux-devtools'

export default function configureStore(onCompletion:()=>void) {
    const enhancer = compose(
        applyMiddleware(thunk),
        devTools({
            name: 'DineNow', realtime: true
        }),
    );

    let store = createStore(reducer, enhancer);
    persistStore(store, {storage: AsyncStorage}, onCompletion);
    return store
}
