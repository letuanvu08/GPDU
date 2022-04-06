import React from "react";
import {
  Image,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
} from "react-native";
import { Button, Icon } from "react-native-elements";
import colors from "~/theme/colors";
import { paddings } from "~/theme/paddings";
import VectorImage from "react-native-vector-image";
import { fontSizes } from "~/theme/fonts";
import routesEnum from "~/constants/routesEnum";

const WelcomeScreen = ({ navigation }) => {
  const handleShipperPress = () => {
    navigation.navigate(routesEnum.SHIPPER_LOGIN);
  };
  const handleCustomerPress = () => {
    navigation.navigate(routesEnum.CUSTOMER_LOGIN);
  };
  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Image style={styles.logo} source={require("../../assets/logo.png")} />
      <Text style={styles.title}>Who are you?</Text>
      <Button
        title="Shipper"
        icon={
          <VectorImage
            source={require("../../assets/icons/shipper.svg")}
            style={styles.icon}
          />
        }
        iconContainerStyle={{ marginRight: 10 }}
        buttonStyle={styles.button}
        containerStyle={styles.buttonContainer}
        titleStyle={styles.buttonTitle}
        onPress={handleShipperPress}
      />
      <Button
        title="Customer"
        icon={
          <VectorImage
            source={require("../../assets/icons/user.svg")}
            style={styles.icon}
          />
        }
        buttonStyle={styles.button}
        containerStyle={styles.buttonContainer}
        titleStyle={styles.buttonTitle}
        onPress={handleCustomerPress}
      />
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: paddings.container,
    alignItems: "center",
  },
  logo: {
    width: 200,
    height: 200,
    marginVertical: 50,
  },
  title: {
    fontSize: fontSizes.title,
    fontWeight: "bold",
    color: "#000",
    marginBottom: 10,
  },
  button: {
    backgroundColor: "transparent",
    alignItems: "center",
    padding: 20,
    justifyContent: "flex-start",
  },
  buttonContainer: {
    width: 230,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
    borderColor: "transparent",
    borderWidth: 0,
    borderRadius: 30,
    backgroundColor: colors.brand.primary,
    marginVertical: 10,
  },
  buttonTitle: {
    fontSize: fontSizes.h3,
    marginLeft: 20,
  },
  icon: {
    height: 50,
    width: 50,
  },
});
export default WelcomeScreen;
