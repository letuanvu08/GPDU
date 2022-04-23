import { configureStore } from "@reduxjs/toolkit";
import ordersReducer from "reduces/ordersReducer";
import vehiclesReducer from "../reduces/vehiclesReducer";
import routingReducer from "../reduces/routingReducer";
const store = configureStore({
  reducer: {
    orders: ordersReducer,
    vehicles: vehiclesReducer,
    routing: routingReducer,
  },
});
export default store;
