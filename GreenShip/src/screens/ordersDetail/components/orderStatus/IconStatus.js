import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
export function IconInfoLocation({}) {
  return (
    <View style={styles.container}>
      <View style={styles.circle} />
      <View style={styles.line}></View>
      <View style={styles.circle} />
    </View>
  );
}

 const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    paddingVertical: 20,
    paddingHorizontal: 5,
  },
  circle: {
    backgroundColor: 'black',
    borderRadius: 50,
    width: 12,
    height: 12,
  },
  line: {
    borderLeftColor: 'black',
    borderLeftWidth: 2,
    borderStyle: 'dashed',
    height: '80%',
    opacity: 0.2,
    margin: 5,
  },
});
