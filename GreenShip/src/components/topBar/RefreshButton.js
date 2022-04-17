import React from 'react';
import { StyleSheet, Image, Pressable } from 'react-native';
import { default as Refresh } from '~/assets/icons/refresh.png';
import {Icon} from 'react-native-elements';
export function RefreshButton({ onPressElement }) {
  return (
    <Pressable
      style={({ pressed }) => [
        {
          backgroundColor: pressed ? '#FAFAFA' : 'white',
        },
        styles.container,
      ]}
      onPress={onPressElement}
    >
        <Icon name="location-sharp" type='ionicon'></Icon>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 36,
    width: 36,
    borderRadius: 50,
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    height: '70%',
    width: '70%',
  },
});