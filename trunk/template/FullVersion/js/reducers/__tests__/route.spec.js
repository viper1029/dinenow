/**
 * Created by kylefang on 4/30/16.
 * @flow
 */

'use strict';

import {expect} from 'chai';
import {describe, it} from 'mocha';

import reducer from '../route';
import {popRoute, replaceRoute, pushNewRoute, popToRoute} from '../../actions/route';
import { REHYDRATE } from 'redux-persist/constants'

describe('Route Reducers', ()=> {
  it('should be able to push new route', ()=> {
    const initialState = {
      animationType: 'none',
      routes: ['abc']
    };
    const expected = {
      animationType: 'forward',
      routes: ['abc', 'edf']
    };
    expect(reducer(initialState, pushNewRoute("edf"))).to.deep.equal(expected)
  });
  it('should be able to replace with new route', ()=> {
    const initialState = {
      animationType: 'none',
      routes: ['abc']
    };
    const expected = {
      animationType: 'forward',
      routes: ['edf']
    };
    expect(reducer(initialState, replaceRoute("edf"))).to.deep.equal(expected)
  });
  it('should be able to pop route', ()=> {
    const initialState = {
      animationType: 'none',
      routes: ['abc', 'edf', 'ghi']
    };
    const expected = {
      animationType: 'backward',
      routes: ['abc', 'edf']
    };
    expect(reducer(initialState, popRoute())).to.deep.equal(expected);
  });
  it('should be able to pop to route', ()=> {
    const initialState = {
      animationType: 'none',
      routes: ['abc', 'edf', 'ghi']
    };
    const expected = {
      animationType: 'backward',
      routes: ['abc']
    };
    expect(reducer(initialState, popToRoute('abc'))).to.deep.equal(expected);
  });
  it('should be able to recover as none animation', ()=> {
    const initialState = {
      animationType: 'forward',
      routes: ['abc', 'edf', 'ghi']
    };
    const expected = {
      animationType: 'none',
      routes: ['abc', 'edf', 'ghi']
    };
    const action = {
      type: REHYDRATE,
      payload: {
        route: initialState
      }
    };
    expect(reducer(initialState, action)).to.deep.equal(expected);
  });
});