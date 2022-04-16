import React, { useEffect } from "react";
import { Provider, useDispatch } from "react-redux";
import { store } from "./src/store/store";
import Navigation from "./src/navigation";
import { SafeAreaProvider } from "react-native-safe-area-context";
import EncryptedStorage from "react-native-encrypted-storage";
import storageKeys from "~/utils/storageKeys";
import userApi from "~/api/user/userApi";
import { login, logout } from "~/reduces/AuthReducer";

export default function App() {
  const dispatch = store.dispatch;
  const checkLogin = async () => {
    try {
      const re = await userApi.getProfile();
      if(!!re.Data){
      dispatch(login(re.Data));
      }
    } catch (e) {
      console.log("error when login: ", e);
    }
  }
  useEffect(() => {
    checkLogin();
  }, []);
  return (
    <Provider store={store}>
      <SafeAreaProvider>
        <Navigation />
      </SafeAreaProvider>
    </Provider>
  );
}
