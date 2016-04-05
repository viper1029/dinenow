module.exports = function(url, params) {
  return fetch(url, params)
    .then(function(response) {
      return response.json();
    });
}
