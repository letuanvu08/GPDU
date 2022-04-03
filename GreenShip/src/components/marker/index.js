import React from 'react';
import {Marker} from 'react-native-maps';
import Animated from 'react-native-reanimated';
import {StyleSheet, View, Image} from 'react-native';
import {default as Sender} from '~/assets/icons/Sender.png';
import {default as Recipient} from '~/assets/icons/Recipient.png';
import TypeMarker from '~/constants/TypeMarker';
export default function CustomMarker({id, marker, onSelect, type}) {
  return (
    <Marker
      identifier={id}
      onPress={() => onSelect(marker)}
      coordinate={{
        latitude: marker.latitude,
        longitude: marker.longitude,
      }}>
      <Image
        source={type === TypeMarker.SEND ? Sender : Recipient}
        style={{width: 30, height: 30}}
        resizeMode="contain"
      />
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
