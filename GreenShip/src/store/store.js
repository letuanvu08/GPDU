import {configureStore} from '@reduxjs/toolkit';
import AuthReducer from '../reduces/AuthReducer';
import SelectedOrder from '~/reduces/SelectedOrder';
export const store = configureStore({
  reducer: {
    auth: AuthReducer,
    selectedOrder: SelectedOrder,
  },
});
