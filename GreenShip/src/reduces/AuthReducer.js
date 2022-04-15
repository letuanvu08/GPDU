import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    isAuth: false,
    profile: null,
  },
  reducers: {
    logout: (state) => {
      state.isAuth = false;
      state.profile = null;
    },
    updateProfile: (state, action) => {
      console.log("User profile: ",action.payload);
      state.profile = action.payload;
    },
    login: (state, action) => {
      console.log("User profile: ",action.payload);
      state.profile = action.payload;
      state.isAuth = true;
    },
  },
});

export const { logout, updateProfile, login } = authSlice.actions;

export default authSlice.reducer;
