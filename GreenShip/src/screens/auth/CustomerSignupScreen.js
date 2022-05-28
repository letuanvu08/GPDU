import React, {useState} from "react";
import {
  KeyboardAvoidingView,
  StyleSheet,
  Text,
  View,
  ScrollView,
  Image,
} from "react-native";
import {Button} from "react-native-elements";
import {Fumi} from "react-native-textinput-effects";
import colors from "~/theme/colors";
import {fontSizes} from "~/theme/fonts";
import FontAwesomeIcon from "react-native-vector-icons/FontAwesome";
import Entypo from "react-native-vector-icons/Entypo";
import routesEnum from "~/constants/routesEnum";
import {default as UserGreenIcon} from '~/assets/icons/user-green.png';
import requestStatusEnum from "~/utils/requestStatusEnum";
import userApi from "~/api/user/userApi";
import TypeUser from "~/constants/TypeUser";

const CustomerSignupScreen = ({navigation}) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [userName, setUserName] = useState("");
  const [status, setStatus] = useState(requestStatusEnum.IDLE);
  const handleSignup = async () => {
    setStatus(requestStatusEnum.LOADING);
    navigation.navigate(routesEnum.LOADING);
    try {

      const res = await userApi.register({userName: userName, password: password, email: email, type: "CUSTOMER"})
      console.log(res.Data);
    } catch (e) {
      console.log("Exception when register: ", e)
    } finally {
      navigation.goBack();
      navigation.navigate(routesEnum.CUSTOMER_LOGIN)
    }
  };

  const handleLogin = () => navigation.navigate(routesEnum.CUSTOMER_LOGIN);
  const canSignup = email && password && userName;

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <KeyboardAvoidingView behavior="position">
        <Image source={UserGreenIcon} style={styles.icon}/>
        <Text style={styles.userType}>Customer</Text>
        <Text style={styles.title}>Sign up</Text>
        <Fumi
          label={"Username"}
          iconClass={Entypo}
          iconName={"user"}
          iconColor={colors.brand.primary}
          iconSize={25}
          iconWidth={40}
          inputPadding={16}
          onChangeText={setUserName}
          inputStyle={{color: "#000"}}
          style={styles.input}
        />
        <Fumi
          label={"Email Adress"}
          iconClass={Entypo}
          iconName={"mail"}
          iconColor={colors.brand.primary}
          iconSize={25}
          iconWidth={40}
          inputPadding={16}
          onChangeText={setEmail}
          inputStyle={{color: "#000"}}
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
          style={styles.input}
        />
        <Button
          title="Sign up"
          buttonStyle={[
            styles.button,
            {backgroundColor: colors.brand.primary},
          ]}
          containerStyle={[styles.buttonContainer, {marginTop: 20}]}
          onPress={handleSignup}
          disabled={!canSignup}
          titleStyle={styles.buttonTitle}
        />
        <View style={styles.line}/>
        <Button
          title="Log in"
          buttonStyle={[
            styles.button,
            {borderColor: colors.brand.primary, borderWidth: 2},
          ]}
          containerStyle={styles.buttonContainer}
          type="outline"
          onPress={handleLogin}
          titleStyle={[styles.buttonTitle, {color: colors.brand.primary}]}
        />
      </KeyboardAvoidingView>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
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
export default CustomerSignupScreen;
