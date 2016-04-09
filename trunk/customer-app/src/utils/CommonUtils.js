'use strict';

export function fetchAsJson(url, params) {
  return fetch(url, params)
    .then(function(response) {
      return response.json();
    });
}