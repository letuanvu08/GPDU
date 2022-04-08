import React, { useState } from "react";
import { Marker } from "react-map-gl";
export default function MarkerMap({ latitude, longitude, image }) {
  return (
    <Marker longitude={latitude} latitude={longitude} anchor="center">
      <img src={image} style={{ width: 20, height: 20 }} />
    </Marker>
  );
}