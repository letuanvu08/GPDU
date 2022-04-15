import {createSlice, current} from '@reduxjs/toolkit';

const init = {
  orderId: "",
};

export const SelectedOrder = createSlice({
  name: 'selectedOrder',
  initialState: init,
  reducers: {
    setSelectedOrder: (state, action) => {
      return {orderId: action.payload.orderId};
    },
    unSetSelectedOrder: state => {
      return {...init};
    },
  },
});

export const {setSelectedOrder, unSetSelectedOrder} = SelectedOrder.actions;

export default SelectedOrder.reducer;
