import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import vehicleApi from "../api/vehicleApi";
import { limit } from "chroma-js/src/utils";

export const fetchVehicles = createAsyncThunk(
  "vehicles/fetchVehicles",
  async ({offset, limit},{getState})=>{
    const res = await vehicleApi.fetchVehicles({offset: offset, limit})
    console.log("fetch vehicle  offset: %s, limit: %s, response: %s", offset, limit, res.Data);
    return res.Data
  }
)
const initialState = {
  vehicles: [],
  offset:0,
  limit: 1000,
  hasNext: true,
  vehicleSelected: null
}
const vehiclesSlice = createSlice({
  name: "vehicles",
  initialState: initialState,
  reducers: {
    setSelectedVehicle: (state, action) =>{
      console.log("vehicleSelected: ", action.payload);
      state.vehicleSelected = action.payload
    } ,
    setProperties:(state, action)=>{
      state.offset= action.payload.offset;
      state.limit= action.payload.limit;
      state.hasNext= action.payload.hasNext;
    }
  },
  extraReducers(builder ){
    builder
      .addCase(fetchVehicles.fulfilled,(state, action)=>{
        state.vehicles.unshift(...action.payload);
        state.offset += action.payload.length;
        if(action.payload.length< limit){
          state.hasNext = false;
        }
      })
  }
})

export const {setProperties, setSelectedVehicle } = vehiclesSlice.actions
export default vehiclesSlice.reducer;