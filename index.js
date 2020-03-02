import React from 'react';
import {AppRegistry, StyleSheet, Text, Button, View} from 'react-native';
import {NativeModules} from 'react-native';

class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, {this.props.name}</Text>
        <Button title="Click me" onPress={() => NativeModules.WorkflowEvents.sendEvent("onClick", "bar")}>Click me</Button>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);
