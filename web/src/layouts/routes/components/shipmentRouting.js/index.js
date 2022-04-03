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
  [106.69733047485352, 10.82184082444266],
  [106.68582916259766, 10.817625582101046],
  [106.67913436889648, 10.814421958289003],
  [106.68394088745117, 10.820323344027514],
  [106.67964935302734, 10.826561825487895],
  [106.66299819946289, 10.834654797047353],
  [106.66574478149414, 10.839038398639847],
  [106.67570114135742, 10.838869799765812],
  [106.67947769165039, 10.855897806107624],
  [106.68033599853514, 10.862135545836304],
  [106.6604232788086, 10.861461201876166],
  [106.66831970214844, 10.861966959989033],
];
