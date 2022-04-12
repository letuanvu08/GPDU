import React, { useState, useMemo } from "react";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL from "react-map-gl";
import MDBox from "components/MDBox";
import Card from "@mui/material/Card";
import PolylineOverlay from "components/PolylineOverlay";
import MarkerMap from "components/Marker";
import Location from "icons/location.png";
import Pickup from "icons/arrow-increase.svg";
import Delivery from "icons/arrow-decrease.svg";
export default function ShipmentRouting() {
  const [viewport, setViewport] = React.useState({
    latitude: 10.7758439,
    longitude: 106.70175527777778,
    zoom: 12,
  });
  return (
    <Card sx={{ height: "100%", width: "100%" }}>
      <MDBox>
        <ReactMapGL
          {...viewport}
          width={500}
          height={600}
          onViewportChange={(viewport) => setViewport(viewport)}
          mapboxApiAccessToken="pk.eyJ1IjoibGV0dWFudnUiLCJhIjoiY2wwZXRhZ2Y0MGwwZDNrbGNxZmF3OXlhbiJ9.EhStD2wRsGq65yD_bGhk9Q"
          mapStyle="mapbox://styles/mapbox/streets-v9"
        >
          <PolylineOverlay points={points} />
          <MarkerMap latitude={points[0][0]} longitude={points[0][1]} image={Location} />
          <MarkerMap latitude={points[10][0]} longitude={points[10][1]} image={Location} />
          {points.map((point) => (
            <MarkerMap latitude={point[0]} longitude={point[1]} image={Location} />
          ))}
        </ReactMapGL>
      </MDBox>
    </Card>
  );
}

const points = [
  [106.726539, 10.814565],
  [106.727486, 10.814006],
  [106.728074, 10.813663],
  [106.7285, 10.81334],
  [106.728552, 10.813301],
  [106.728587, 10.813274],
  [106.728755, 10.813147],
  [106.728977, 10.812954],
  [106.729029, 10.812934],
  [106.729438, 10.812672],
  [106.729512, 10.812617],
];
