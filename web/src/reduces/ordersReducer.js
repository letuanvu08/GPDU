const { createSlice, createAsyncThunk } = require("@reduxjs/toolkit");
import orderApi from "api/orderApi";
const initialState = {
  items: [],
  offset: 0,
  limit: 4,
  hasNext: true,
  itemSelected: {},
};

export const fetchOrders = createAsyncThunk(
  "orders/fetchOrders", 
async (_, { getState }) => {
  const res = await orderApi.fetchListOrders({
    offset: getState.orders.offset,
  });
  console.log(res.Data);
  return res.Data;
});

const ordersSlice = createSlice({
  name: "orders",
  initialState: initialState,
  reducers: {
    setOrderSelected: (state, action) => {
      state.itemSelected = action.payload.item;
    },
    addListOrder: (state, action) => {
      if (action.payload.length == 0) {
        state.hasNext = false;
      }
      state.items.push(...action.payload);
      state.offset += action.payload.length;
    },
  },
  extraReducers(builder) {
    builder.addCase(fetchOrders.fulfilled, (state, action) => {
      if (action.payload.length == 0) {
        state.hasNext = false;
      }
      console.log(">>sdfdsf>",action.payload);
      state.items.push(...action.payload);
      state.offset += action.payload.length;
    });
  },
});

export const { setOrderSelected, addListOrder } = ordersSlice.actions;

export default ordersSlice.reducer;
