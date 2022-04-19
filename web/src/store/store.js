import { configureStore } from "@reduxjs/toolkit";
import ordersReducer from "reduces/ordersReducer";
const store = configureStore({
  reducer: {
    orders: ordersReducer,
  },
});
export default store;
