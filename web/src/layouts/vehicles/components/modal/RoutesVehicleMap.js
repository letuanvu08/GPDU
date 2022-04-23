import React, { useEffect } from "react";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL from "react-map-gl";
import MDBox from "components/MDBox";
import Card from "@mui/material/Card";
import PolylineOverlay from "components/PolylineOverlay";
import MarkerMap from "components/Marker";
import Location from "icons/location.png";

import useMap from "../../../../hooks/useMap";
import { useSelector } from "react-redux";
import useWindowDimensions from "../../../../hooks/useWindowDimensions";

export default function RoutesVehicleMap() {
  const { viewport, setViewport } = useMap();
  const { routing, polyline, locationSelected } = useSelector(state => state.routing);
  const window = useWindowDimensions();
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
          {routing && routing.nodes.map(node => (
            <MarkerMap
              latitude={node.location.latitude}
              longitude={node.location.longitude}
              image={Location}
            />))}
        </ReactMapGL>
      </MDBox>
    </Card>
  );
}

