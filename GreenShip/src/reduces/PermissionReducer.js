import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {PermissionsAndroid} from 'react-native';
const init = {
  grandLocation: false,
};

export const requestPermissionLocation = createAsyncThunk(
  'requestLocation',
  async () => {
    return await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      {
        title: 'Location Permission',
        message: 'GPDU needs access to your location ',
        buttonNeutral: 'Ask Me Later',
        buttonNegative: 'Cancel',
        buttonPositive: 'OK',
      },
    );
  },
);
const permissionSlice = createSlice({
  name: 'permission',
  initialState: init,
  reducers: {},
  extraReducers(builder) {
    builder.addCase(requestPermissionLocation.fulfilled, (state, action) => {
      if (action.payload === PermissionsAndroid.RESULTS.GRANTED) {
        state.grandLocation = true;
      }
    });
  },
});

export default permissionSlice.reducer;
