var React = require('react-native');
var {
	AppRegistry,
  StyleSheet,
	Navigator
} = React;

var Intro = require('./src/views/intro');
var SignUp = require('./src/views/authentication/signup');
var SignIn = require('./src/views/authentication/signin');
var Search = require('./src/views/search');

var ROUTES = {
	intro: Intro,
	signin: SignIn,
	signup: SignUp,
	search: Search
};

var DineNow = React.createClass({
	renderScene: function(route, navigator){
    var Component = ROUTES[route.name];
		return <Component route={route} navigator={navigator}/>
	},
  render: function(){
    return (
      <Navigator
			  style={styles.container}
				initialRoute={{name: 'intro'}}
				renderScene={this.renderScene}
				configureScene={() => { return Navigator.SceneConfigs.FloatFromRight;}}
				/>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1
  }
});

// Show the react component on the screen
AppRegistry.registerComponent('DineNow', function() {
return DineNow
});
