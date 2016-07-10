/**
 * Created by kylefang on 4/30/16.
 * @flow
 */

'use strict';

import {expect} from 'chai';
import {describe, it} from 'mocha';

import reducer from '../listings';
import {_loadedChannel, _loadedMoreInChannel} from '../../actions/listings';

describe('Listing Reducers', ()=> {
  it('should be able to load channel', ()=> {
    const initialState = {
      default: {},
    };
    const result = reducer(initialState, _loadedChannel("default", 10, ["123"]));
    expect(result.default.total).to.equal(10);
    expect(result.default.items).to.deep.equal(["123"]);
  });
  it('should be able to load more in channel', ()=> {
    const initialState = {
      default: {
        items: [1, 2, 3],
        total: 10,
        updated: 'DATE',
      }
    };
    const results = reducer(initialState, _loadedMoreInChannel('default', [4, 5, 6]));
    expect(results.default.items).to.deep.equal([1, 2, 3, 4, 5, 6]);
  });
  it('should not load more when channel is not initialized', ()=> {
    const initialState = {};
    expect(reducer(initialState, _loadedMoreInChannel('default', [1, 2, 3])).default).to.equal(undefined);
  });
});