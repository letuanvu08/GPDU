import { configureStore } from "@reduxjs/toolkit";
import ordersReducer from "reduces/ordersReducer";
import vehiclesReducer from "../reduces/vehiclesReducer";
const store = configureStore({
  reducer: {
    orders: ordersReducer,
    vehicles: vehiclesReducer
  },
});
export default store;
