import React, { useState, useMemo, useEffect } from "react";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL from "react-map-gl";
import MDBox from "components/MDBox";
import Card from "@mui/material/Card";
import PolylineOverlay from "components/PolylineOverlay";
import MarkerMap from "components/Marker";
import Location from "icons/location.png";
import Pickup from "icons/arrow-increase.svg";
import Delivery from "icons/arrow-decrease.svg";
import mapboxApi from "api/mapboxAPI";
import routingApi from "api/routingApi";
export default function ShipmentRouting({ item }) {
  const [viewport, setViewport] = React.useState({
    latitude: 10.7758439,
    longitude: 106.70175527777778,
    zoom: 15,
  });
  const [polyline, setPolyline] = useState([]);
  useEffect(() => {
    if (!!item) {
      getRouting(item.id);
      setViewport({
        ...viewport,
        latitude: item.currentLocation?.latitude || 10.7758439,
        longitude: item.currentLocation?.longitude || 106.70175527777778,
      });
      console.log("latitu", item.pickup?.location);
    }
  }, [item]);
  const getRouting = (orderId) => {
    routingApi
      .getRoutingByOrderId({ orderId: orderId })
      .then((res) => {
        if (!!res.Data) {
          const routing = res.Data;
          console.log("routing: ", routing);
          if (routing) {
            const locations = routing.nodes.map((node) => node.location);
            getPolyline(locations);
          }
        }
      })
      .catch((e) => console.log(e));
  };
  const getPolyline = (locations) => {
    mapboxApi
      .direction(locations)
      .then((res) => {
        const coordinates = res.data.routes[0]?.geometry.coordinates || [];
        const mapPolyline = coordinates.map((coordinate) => [coordinate[0], coordinate[1]]);
        console.log("mapPolyline", mapPolyline);
        setPolyline(mapPolyline);
      })
      .catch((e) => console.log(e));
  };
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
          <PolylineOverlay points={polyline} />
          {item.currentLocation && (
            <MarkerMap
              latitude={item.currentLocation.latitude}
              longitude={item.currentLocation.longitude}
              image={Location}
            />
          )}
          {item.pickup && (
            <MarkerMap
              latitude={item.pickup.location.latitude}
              longitude={item.pickup.location?.longitude}
              image={Location}
            />
          )}
          {item.delivery && (
            <MarkerMap
              latitude={item.delivery.location.latitude}
              longitude={item.delivery.location.longitude}
              image={Location}
            />
          )}
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
