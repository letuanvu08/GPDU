import React from 'react';
import {Marker} from 'react-native-maps';
import Animated from 'react-native-reanimated';
import {StyleSheet, View, Image} from 'react-native';
import {default as Sender} from '~/assets/icons/Sender.png';
import {default as Recipient} from '~/assets/icons/Recipient.png';
import {default as Location} from '~/assets/icons/location_blue.png';
import TypeMarker from '~/constants/TypeMarker';
import {Icon} from 'react-native-elements';
export default function CustomMarker({node, onSelect}) {
  return (
    <Marker
      onPress={() => onSelect(node)}
      coordinate={{
        latitude: node.location.latitude,
        longitude: node.location.longitude,
      }}>
      {node.typeNode === TypeMarker.PACKAGE && (
        <Icon name="package" type="feather" size={24} />
      )}
      {node.typeNode=== TypeMarker.LOCATION && (
        <Image
        source={location}
        style={{width: 24, height: 24}}
        resizeMode="contain"
      />
      )}
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
