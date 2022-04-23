import orderApi from "~/api/orderApi";
const { createSlice, createAsyncThunk } = require("@reduxjs/toolkit");

export const addNewOrder = createAsyncThunk(
  "orders/addNewOrder",
  async (order) => {
    const re = await orderApi.createOrder(order);
    console.log(re);
    return re.Data;
  }
);

export const fetchOrders = createAsyncThunk(
  "orders/fetchOrders",
  async (_, { getState }) => {
    const re = await orderApi.fetchOrders({
      offset: getState().orders.offset,
    });
    console.log(re.Data);
    return re.Data;
  }
);
const initialState = {
  items: [],
  offset: 0,
  hasNext: true,
};
const ordersSlice = createSlice({
  name: "orders",
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(addNewOrder.fulfilled, (state, action) => {
        state.items.unshift(action.payload);
        state.offset++;
      })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.items.push(...action.payload);
        state.offset += action.payload.length;
      });
  },
});

export default ordersSlice.reducer;
