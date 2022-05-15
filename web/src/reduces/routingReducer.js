import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import routingApi from "api/routingApi";
import mapboxApi from "api/mapboxAPI";

const initState = {
  routing: null,
  polyline: [],
  locationSelected: {
    latitude: 10.7758439,
    longitude: 106.70175527777778,
    zoom: 15,
  },
  storage:{
    location:{
      latitude: 10.773372865851005,
      longitude: 106.65959845041081
    },
  }
};

export const getRoutingByOrderId = createAsyncThunk(
  "routing/getRoutingByOrderId",
  async ({ orderId }, { getState }) => {
    const res = await routingApi
      .getRoutingByOrderId({ orderId: orderId });
    console.log("response getRoutingByOrderId: ", res.Data);
    return res.Data;
  });

export const getRoutingByVehicleId = createAsyncThunk(
  "routing/getRoutingByVehicleId",
  async ({ vehicleId }, { getState }) => {
    const res = await routingApi
      .getRoutingByVehicleId({ vehicleId: vehicleId });
    console.log("response getRoutingByVehicleId: ", res.Data);
    return res.Data;
  });

export const getPolyline = createAsyncThunk(
  "routing/getPolyline",
  async ({ routing }, { getState }) => {
    const locations = routing.nodes.map((node) => node.location);
    locations.push({
      latitude: 10.773372865851005,
      longitude: 106.65959845041081
    })
    const res = await mapboxApi.direction(locations);
    console.log("getPolyline response: ", res)
    return res;
  },
);

const routingSlice = createSlice({
  name: "routing",
  initialState: initState,
  reducers: {
    setLocationSelected: (state, action) => {
      state.locationSelected = action.payload;
    },
  },
  extraReducers(buidler) {
    buidler
      .addCase(getRoutingByVehicleId.fulfilled, (state, action) => {
        state.routing = action.payload;
      })
      .addCase(getRoutingByOrderId.fulfilled, (state, action) => {
        state.routing = action.payload;
      })
      .addCase(getPolyline.fulfilled, (state, action) => {
        const coordinates = action.payload.data.routes[0]?.geometry.coordinates || [];
        const mapPolyline = coordinates.map((coordinate) => [coordinate[0], coordinate[1]]);
        console.log("mapPolyline", mapPolyline);
        state.polyline = mapPolyline;
      });
  },
});
export const { setLocationSelected } = routingSlice.actions;
export default routingSlice.reducer;