import React, { useState, useRef, useCallback, useEffect } from "react";
import Grid from "@mui/material/Grid";
import MDBox from "../../components/MDBox";
import MDInput from "components/MDInput";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import DashboardLayout from "components/LayoutContainers/DashboardLayout";
import DashboardNavbar from "components/Navbars/DashboardNavbar";
import MarkerMap from "components/Marker";
import "mapbox-gl/dist/mapbox-gl.css";
import ReactMapGL, { GeolocateControl, NavigationControl, Marker } from "react-map-gl";
import Geocoder from "react-map-gl-geocoder";
import Geocoding from "react-native-geocoding";
import Location from "icons/location.png";
import DeckGL, { GeoJsonLayer } from "deck.gl";
import "./mapbox-gl-geocoder.css";
const MAPBOX_TOKEN_PRIVATE =
  "sk.eyJ1IjoibGV0dWFudnUiLCJhIjoiY2wxaXpseDhmMGlpMTNjcnB5aGdnajQ3OSJ9.yopJnvlfdiLvmG6iW5fhgg";
const MAPBOX_TOKEN_PUB = "pk.eyJ1IjoibGV0dWFudnUiLCJhIjoiY2wwZXRhZ2Y0MGwwZDNrbGNxZmF3OXlhbiJ9.EhStD2wRsGq65yD_bGhk9Q";
function AddOrders() {
  const [viewport, setViewport] = useState({
    latitude: 10.7758439,
    longitude: 106.70175527777778,
    zoom: 12,
  });
  const [searchResultLayer, setSearchResultLayer] = useState(null);
  const order = useRef({});
  const [currentSearch, setCurrentSearch] = useState("");

  const [markers, setMarkers] = useState([]);
  const handleAddMarker = (event) => {
    console.log(event);
    const coordination = event.lngLat;
    Geocoding.init(MAPBOX_TOKEN_PUB, { language: "vi" });
    Geocoding.from(coordination[1], coordination[0])
      .then((json) => {
        var addressComponent = json.results[0].address_components[0];
        console.log(addressComponent);
      })
      .catch((error) => console.warn(error));

    setMarkers([...markers, coordination]);
    setViewport({ ...viewport, latitude: coordination[1], longitude: coordination[0] });
  };
  const handleViewportChange = useCallback((newViewport) => setViewport(newViewport), []);

  const handleGeocoderViewportChange = useCallback((newViewport) => {
    const geocoderDefaultOverrides = { transitionDuration: 1000 };

    return handleViewportChange({
      ...newViewport,
      ...geocoderDefaultOverrides,
    });
  }, []);

  const handleOnResultPickup = useCallback((result) => {
    handleSetLocationOrder("pickup", result.result);
  }, []);
  const handleOnResultDelivery = useCallback((result) => {
    handleSetLocationOrder("delivery", result.result);
  }, []);
  const handleSetLocationOrder = (type, result) => {
    console.log(result);
    const data = {
      coordinate: {
        latitude: result.geometry.coordinates[1],
        longitude: result.geometry.coordinates[0],
      },
      name: result.text,
      address: result.place_name,
    };
    if (type == "pickup") {
      order.current = { ...order.current, pickup: data };
    } else {
      order.current = { ...order.current, delivery: data };
    }
    setSearchResultLayer({
      searchResultLayer: new GeoJsonLayer({
        id: "search-result",
        data: result.geometry,
        getFillColor: [255, 0, 0, 128],
        getRadius: 1000,
        pointRadiusMinPixels: 10,
        pointRadiusMaxPixels: 10,
      }),
    });
  };
  useEffect(() => {
    console.log(order.current);
  }, [order.current]);

  const mapRef = useRef();
  const pickupRef = useRef();
  const deliveryRef = useRef();
  return (
    <DashboardLayout>
      <DashboardNavbar absolute isMini />
      <MDBox mt={8}>
        <MDBox mb={3}>
          <Grid container spacing={1}>
            <Grid item xs={12} md={4}>
              <Grid container spacing={1}>
                <Grid
                  item
                  ref={pickupRef}
                  m={2}
                  xs={12}
                  md={12}
                  sx={{ position: "relative", borderRadius: 10, zIndex: 2 }}
                />
                <Grid
                  item
                  ref={deliveryRef}
                  m={2}
                  xs={12}
                  md={12}
                  sx={{
                    position: "relative",
                    borderRadius: 10,
                    zIndex: 1,
                  }}
                />
              </Grid>
              ˝
            </Grid>
            <Grid item xs={12} md={7}>
              <ReactMapGL
                ref={mapRef}
                {...viewport}
                width={745}
                height={680}
                onViewportChange={handleViewportChange}
                mapboxApiAccessToken={MAPBOX_TOKEN_PUB}
                mapStyle="mapbox://styles/mapbox/streets-v9"
                onDblClick={handleAddMarker}
              >
                {markers.map((point) => (
                  <MarkerMap latitude={point[0]} longitude={point[1]} image={Location} />
                ))}
                <Geocoder
                  mapRef={mapRef}
                  containerRef={pickupRef}
                  language="vi"
                  onViewportChange={handleGeocoderViewportChange}
                  mapboxApiAccessToken={MAPBOX_TOKEN_PUB}
                  onResult={handleOnResultPickup}
                  position="top-right"
                  marker={true}
                />
                <Geocoder
                  mapRef={mapRef}
                  containerRef={deliveryRef}
                  language="vi"
                  onViewportChange={handleGeocoderViewportChange}
                  mapboxApiAccessToken={MAPBOX_TOKEN_PUB}
                  onResult={handleOnResultDelivery}
                  position="top-right"
                />
                <DeckGL {...viewport} layers={[searchResultLayer]} />
                <GeolocateControl onPointerMove={(e) => console.log(e)} />
              </ReactMapGL>
            </Grid>
          </Grid>
        </MDBox>
      </MDBox>
    </DashboardLayout>
  );
}
export default AddOrders;
