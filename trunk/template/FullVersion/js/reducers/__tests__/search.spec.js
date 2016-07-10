/**
 * Created by kylefang on 4/30/16.
 * @flow
 */

'use strict';

import {expect} from 'chai';
import {describe, it} from 'mocha';

import reducer from '../search';
import {updateKeyword, updateTermResult, addSearchTermToHistory} from '../../actions/search';

describe('Search Reducers', ()=> {
  it('should be able to update term', ()=> {
    const initialState = {
      termToSearch: "",
      savedTerms: [],
      results: [],
    };
    const expected = {
      termToSearch: "Hello",
      savedTerms: [],
      results: [],
    };
    expect(reducer(initialState, updateKeyword("Hello"))).to.deep.equal(expected)
  });
  it('should be able to update term and set result to saved value', ()=> {
    const initialState = {
      termToSearch: "h",
      savedTerms: ['hello, world'],
      results: [],
    };
    const expected = {
      termToSearch: "",
      savedTerms: ['hello, world'],
      results: ['hello, world'],
    };
    expect(reducer(initialState, updateKeyword(""))).to.deep.equal(expected)
  });
  it('should be able to update term result', ()=> {
    const initialState = {
      termToSearch: "h",
      savedTerms: ['hello, world'],
      results: [],
    };
    const expected = {
      termToSearch: "h",
      savedTerms: ['hello, world'],
      results: ['hello, world'],
    };
    expect(reducer(initialState, updateTermResult(['hello, world']))).to.deep.equal(expected)
  });
  it('should be able to add term history and reset result', ()=> {
    const initialState = {
      termToSearch: "awesome",
      savedTerms: ['hello, world'],
      results: ['world'],
    };
    const expected = {
      termToSearch: "",
      savedTerms: ['awesome', 'hello, world'],
      results: [],
    };
    expect(reducer(initialState, addSearchTermToHistory('awesome'))).to.deep.equal(expected)
  });
});