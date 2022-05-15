import React, { useEffect, useState } from "react";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL from "react-map-gl";
import MDBox from "components/MDBox";
import Card from "@mui/material/Card";
import PolylineOverlay from "components/PolylineOverlay";
import MarkerMap from "components/Marker";
import Location from "icons/location_blue.png";

import useMap from "../../../../hooks/useMap";
import { useSelector } from "react-redux";
import useWindowDimensions from "../../../../hooks/useWindowDimensions";
import { MarkNumber } from "../../../../components/Marker/MarkNumber";
import warehouse from "icons/warehouse.png";
import StorageApi from "api/storageApi";

export default function RoutesVehicleMap() {
  const { viewport, setViewport } = useMap();
  const { routing, polyline, locationSelected } = useSelector(state => state.routing);
  const { vehicleSelected } = useSelector(state => state.vehicles);
  const [storage, setStorage] = useState(null);
  const window = useWindowDimensions();

  useEffect(() => {
    StorageApi.getRoutingByOrderId().then(res => {
      const data = res.Data;
      if (data) {
        setStorage(data);
      }
    });
  }, []);

  useEffect(() => {
    setViewport({
      ...viewport,
      latitude: locationSelected.latitude,
      longitude: locationSelected.longitude,
    });
  }, [locationSelected]);
  return (
    <Card sx={{ height: "100%", width: "100%" }}>
      <MDBox>
        <ReactMapGL
          width={window.width - 560}
          height={window.height - 120}
          {...viewport}
          onViewportChange={(viewport) => setViewport(viewport)}
          mapboxApiAccessToken="pk.eyJ1IjoibGV0dWFudnUiLCJhIjoiY2wwZXRhZ2Y0MGwwZDNrbGNxZmF3OXlhbiJ9.EhStD2wRsGq65yD_bGhk9Q"
          mapStyle="mapbox://styles/mapbox/streets-v9"
        >
          <PolylineOverlay points={polyline} />
          {routing && routing.nodes.map((node, index) => (
            <MarkerMap
              latitude={node.location.latitude}
              longitude={node.location.longitude}
            >
              <MarkNumber number={index + 1} />
            </MarkerMap>))}
          <MarkerMap
            latitude={vehicleSelected?.currentLocation.latitude}
            longitude={vehicleSelected?.currentLocation.longitude}
          >
            <img src={Location} style={{ width: 20, height: 20 }} />
          </MarkerMap>
          <MarkerMap
            latitude={vehicleSelected?.currentLocation.latitude}
            longitude={vehicleSelected?.currentLocation.longitude}
          >
            <img src={Location} style={{ width: 20, height: 20 }} />
          </MarkerMap>
          {storage && <MarkerMap
            latitude={storage.location.latitude}
            longitude={storage?.location.longitude}
          >
            <img src={warehouse} style={{ width: 24, height: 24 }} />
          </MarkerMap>}
        </ReactMapGL>
      </MDBox>
    </Card>
  );
}

