//TopBar.js
import React from 'react';
import { Pressable, StyleSheet, View } from 'react-native';
import { Avatar } from './Avatar';
import { RefreshButton } from './RefreshButton';

export function TopBar({ onPressElement }) {
  const handleViewProfile = ()=>{
    console.log(">>> view profile need implement")
  }
  return (
    <View style={styles.container}>
      <Pressable onPress={handleViewProfile}> 
      <Avatar/>
      </Pressable>
      <RefreshButton onPressElement={onPressElement} />
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    left: 0,
    top: 40,
    width: '100%',
    zIndex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 10,
  },
});