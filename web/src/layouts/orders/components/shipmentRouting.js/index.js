import React, { useState, useMemo, useEffect } from "react";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL from "react-map-gl";
import MDBox from "components/MDBox";
import Card from "@mui/material/Card";
import PolylineOverlay from "components/PolylineOverlay";
import MarkerMap from "components/Marker";
import Location from "icons/location_blue.png";

import useMap from "../../../../hooks/useMap";
import { useDispatch, useSelector } from "react-redux";
import {
  getPolyline,
  getRoutingByOrderId,
  getRoutingByVehicleId,
  setLocationSelected,
} from "../../../../reduces/routingReducer";
import { MarkNumber } from "../../../../components/Marker/MarkNumber";

export default function ShipmentRouting({ item }) {
  const { viewport, setViewport } = useMap();
  const { polyline, locationSelected } = useSelector(state => state.routing);
  const dispatch = useDispatch();
  useEffect(() => {
    setViewport({
      ...viewport,
      latitude: locationSelected.latitude,
      longitude: locationSelected.longitude,
    });
  }, [locationSelected]);

  useEffect(() => {
    if (!!item) {
      dispatch(getRoutingByOrderId({ orderId: item.id }))
        .then(res => {
          if (res.payload) {
            dispatch(getPolyline({ routing: res.payload }));
            dispatch(setLocationSelected(res.payload.nodes?.[0].location));
          }
        });
    }
  }, [item]);
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
            >
              <img src={Location} style={{ width: 20, height: 20 }} />
            </MarkerMap>
          )}
          {item.pickup && (
            <MarkerMap
              latitude={item.pickup.location.latitude}
              longitude={item.pickup.location?.longitude}
            >
              <MarkNumber number={1} />
            </MarkerMap>
          )}
          {item.delivery && (
            <MarkerMap
              latitude={item.delivery.location.latitude}
              longitude={item.delivery.location.longitude}
            >
              <MarkNumber number={2} />
            </MarkerMap>
          )}
        </ReactMapGL>
      </MDBox>
    </Card>
  );
}

