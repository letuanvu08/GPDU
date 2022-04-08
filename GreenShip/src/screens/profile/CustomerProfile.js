import React from "react";
import { ScrollView, Text } from "react-native";
import { Button } from "react-native-elements";
import { useDispatch } from "react-redux";
import { logout } from "~/reduces/AuthReducer";
import EncryptedStorage from "react-native-encrypted-storage";
import storageKeys from "~/utils/storageKeys";

const CustomerProfile = () => {
  const dispatch = useDispatch();
  const handleLogout = async () => {
    await EncryptedStorage.setItem(storageKeys.ACCESS_TOKEN, "");
    await EncryptedStorage.setItem(storageKeys.REFRESH_TOKEN, "");
    dispatch(logout());
  };
  return (
    <ScrollView>
      <Text>Customer Profile</Text>
      <Button title="Log out" onPress={handleLogout} />
    </ScrollView>
  );
};

export default CustomerProfile;
