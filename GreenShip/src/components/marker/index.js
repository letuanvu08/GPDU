import React from 'react';
import {Marker} from 'react-native-maps';
import Animated from 'react-native-reanimated';
import {StyleSheet, View, Image} from 'react-native';
export default function CustomMarker({coordinate, onSelect, children}) {
  return (
    <Marker onPress={() => onSelect()} coordinate={coordinate}>
      {children}
      {/*{image && (*/}
      {/*  <Image*/}
      {/*    source={image}*/}
      {/*    style={{width: 24, height: 24}}*/}
      {/*    resizeMode="contain"*/}
      {/*  />*/}
      {/*)}*/}
    </Marker>
  );
}

const styles = StyleSheet.create({
  markerWrapper: {
    height: 50,
    width: 50,
    alignItems: 'center',
    justifyContent: 'center',
  },
  marker: {
    height: 22,
    width: 22,
    borderRadius: 20,
    borderColor: 'white',
    borderWidth: 2,
  },
});
