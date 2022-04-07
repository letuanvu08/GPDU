import React, { useState } from "react";
import {
  KeyboardAvoidingView,
  StyleSheet,
  Text,
  View,
  ScrollView,
  Image,
  Alert,
} from "react-native";
import { Button } from "react-native-elements";
import { Fumi } from "react-native-textinput-effects";
import colors from "~/theme/colors";
import { fontSizes } from "~/theme/fonts";
import FontAwesomeIcon from "react-native-vector-icons/FontAwesome";
import Entypo from "react-native-vector-icons/Entypo";
import routesEnum from "~/constants/routesEnum";
import VectorImage from "react-native-vector-image";
import { login } from "~/reduces/AuthReducer";
import requestStatusEnum from "~/utils/requestStatusEnum";
import { useDispatch } from "react-redux";
import userApi from "~/api/user/userApi";
import EncryptedStorage from "react-native-encrypted-storage";
import storageKeys from "~/utils/storageKeys";

const ShipperLoginScreen = ({ navigation }) => {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [status, setStatus] = useState(requestStatusEnum.IDLE);
  const handleSignup = () => navigation.navigate(routesEnum.SHIPPER_SIGNUP);
  const canLogin = userName && password;
  const dispatch = useDispatch();
  const handleLogin = async () => {
    if (canLogin) {
      try {
        setStatus(requestStatusEnum.LOADING);
        navigation.navigate(routesEnum.LOADING);
        const re = await userApi.login({ userName, password });
        await EncryptedStorage.setItem(storageKeys.ACCESS_TOKEN, re.Data.token);
        await EncryptedStorage.setItem(storageKeys.REFRESH_TOKEN, re.Data.refreshToken);
        const profile = await userApi.getProfile();
        navigation.goBack();
        dispatch(login(profile.Data));
      } catch (e) {
        navigation.goBack();
        console.log(e);
      } finally {
        setStatus(requestStatusEnum.IDLE);
      }
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <KeyboardAvoidingView behavior="position">
        <VectorImage
          source={require("../../assets/icons/shipper-green.svg")}
          style={styles.icon}
        />
        <Text style={styles.userType}>Shipper</Text>
        <Text style={styles.title}>Log in</Text>
        <Fumi
          label={"Username"}
          iconClass={Entypo}
          iconName={"user"}
          iconColor={colors.brand.primary}
          iconSize={25}
          iconWidth={40}
          inputPadding={16}
          onChangeText={setUserName}
          inputStyle={{ color: "#000" }}
          style={styles.input}
        />
        <Fumi
          label={"Password"}
          iconClass={FontAwesomeIcon}
          iconName={"lock"}
          iconColor={colors.brand.primary}
          iconSize={25}
          iconWidth={40}
          inputPadding={16}
          onChangeText={setPassword}
          secureTextEntry={true}
          inputStyle={{ color: "#000" }}
          style={styles.input}
        />
        <Button
          title="Log in"
          buttonStyle={[
            styles.button,
            { backgroundColor: colors.brand.primary },
          ]}
          containerStyle={[styles.buttonContainer, { marginTop: 20 }]}
          onPress={handleLogin}
          disabled={!canLogin}
          titleStyle={styles.buttonTitle}
        />
        <View style={styles.line} />
        <Button
          title="Sign up"
          buttonStyle={[
            styles.button,
            { borderColor: colors.brand.primary, borderWidth: 2 },
          ]}
          containerStyle={styles.buttonContainer}
          type="outline"
          onPress={handleSignup}
          titleStyle={[styles.buttonTitle, { color: colors.brand.primary }]}
        />
      </KeyboardAvoidingView>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  logo: {
    width: 150,
    height: 150,
    alignSelf: "center",
    marginTop: 20,
  },
  userType: {
    color: colors.brand.primary,
    fontSize: fontSizes.h3,
    fontWeight: "bold",
    alignSelf: "center",
  },
  title: {
    fontSize: fontSizes.title,
    color: "#000",
    fontWeight: "bold",
    marginTop: 50,
  },
  input: {
    marginTop: 20,
    backgroundColor: colors.bg.input,
    borderRadius: 30,
  },
  button: {
    borderRadius: 20,
    height: 70,
  },
  buttonContainer: {
    width: 300,
    alignSelf: "center",
  },
  buttonTitle: {
    fontSize: fontSizes.largeButton,
  },
  line: {
    borderBottomColor: "#a9a9a9",
    borderBottomWidth: 1,
    marginVertical: 20,
    width: "80%",
    alignSelf: "center",
  },
  icon: {
    height: 150,
    width: 150,
    alignSelf: "center",
    marginVertical: 10,
  },
});
export default ShipperLoginScreen;
