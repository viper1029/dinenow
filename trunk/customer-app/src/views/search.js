import React, { View, Text, StyleSheet,  TextInput} from 'react-native';
import Button from '../components/Button';

module.exports = React.createClass({
  getInitialState: function() {
    return {
      query: '',
    };
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Text style={styles.label}>Enter Address:</Text>
        <TextInput
          style={styles.input}
          value={this.state.query}
          onChangeText={(text) => this.setState({query: text})}
        />
        <Button text={'Search'} onPress={this.onSearchPress} />
      </View>
    );
  },
  onSearchPress: function() {

  }
});

var styles = StyleSheet.create({
  container : {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  input: {
    padding: 4,
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 5,
    margin: 5,
    width: 200,
    alignSelf: 'center'
  },
  label: {
    fontSize: 18
  }
});
