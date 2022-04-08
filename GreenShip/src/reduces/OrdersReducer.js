import orderApi from "~/api/orderApi";
const { createSlice, createAsyncThunk } = require("@reduxjs/toolkit");

export const addNewOrder = createAsyncThunk("orders/addNewOrder", async (order) => {
  return await orderApi.createOrder(order);
});
const initialState = {
  orders: [],
};
const ordersSlice = createSlice({
  name: "orders",
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder.addCase(addNewOrder.fulfilled, (state, action) => {
      state.orders.push(action.payload);
    });
  },
});

export default ordersSlice.reducer