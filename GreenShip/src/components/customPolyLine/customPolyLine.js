import React from 'react';
import {Polyline} from 'react-native-maps';

export function CustomPolyLine({coordinates }) {
  return (
    <Polyline
      coordinates={coordinates}
      strokeColor="#01B0FF" // fallback for when `strokeColors` is not supported by the map-provider
      style={{
        height:1
      }}
      strokeWidth={6}
    />
  );
}
