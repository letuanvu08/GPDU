import React from 'react';
import {Polyline} from 'react-native-maps';

export function CustomPolyLine({coordinates }) {
  return (
    <Polyline
      coordinates={coordinates}
      strokeColor="#01B0FF" // fallback for when `strokeColors` is not supported by the map-provider
      // strokeColors={[
      //   '#7F0000',
      //   '#00000000', // no color, creates a "long" gradient between the previous and next coordinate
      //   '#B24112',
      //   '#E5845C',
      //   '#238C23',
      //   '#7F0000',
      // ]}
      strokeWidth={coordinates.length}
    />
  );
}
