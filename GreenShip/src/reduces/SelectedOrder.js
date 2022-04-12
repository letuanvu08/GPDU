import {createSlice, current} from '@reduxjs/toolkit';

const init = {
  orderID: '1',
  latitude: 10.775948327861144,
  longitude: 106.69959749384073,
  color: '#2F3136',
  name: 'Museum of Ho Chi Minh City',
  direction: 'Ly Tu Trong Street',
  type: 'send',
};

export const SelectedOrder = createSlice({
  name: 'selectedOrder',
  initialState: init,
  reducers: {
    setSelectedOrder: (state, action) => {
      return {...action.payload};
    },
    unSetSelectedOrder: state => {
      return {...init};
    },
  },
});

export const {setSelectedOrder, unSetSelectedOrder} = SelectedOrder.actions;

export default SelectedOrder.reducer;
