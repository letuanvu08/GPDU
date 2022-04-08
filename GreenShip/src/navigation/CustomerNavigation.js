import React from "react";

import { Icon } from "react-native-elements";
import colors from "../theme/colors";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { StyleSheet, Text, View } from "react-native";
import { TouchableOpacity } from "react-native-gesture-handler";
import routesEnum from "~/constants/routesEnum";
import OrderListScreen from "~/screens/order/OrderListScreen";
import CreateOrderScreen from "~/screens/order/CreateOrderScreen";
import CustomerProfile from "~/screens/profile/CustomerProfile";
const Tab = createBottomTabNavigator();

const CutomTabBarButton = ({ children, onPress }) => (
  <TouchableOpacity
    style={{
      top: -30,
      justifyContent: "center",
      alignItems: "center",
      ...styles.shadow,
      backgroundColor: colors.brand.primary,
      width: 80,
      height: 80,
      borderRadius: 40,
    }}
    onPress={onPress}
  >
    {children}
  </TouchableOpacity>
);
const CustomerNavigation = () => {
  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarShowLabel: false,
        tabBarStyle: {
          position: "absolute",
          bottom: 10,
          left: 20,
          right: 20,
          elevation: 0,
          backgroundColor: "#fff",
          borderRadius: 15,
          height: 70,
          ...styles.shadow,
        },
        tabBarActiveTintColor: colors.brand.primary
      }}
      barStyle={{ backgroundColor: "#fff" }}
    >
      <Tab.Screen
        name={routesEnum.ORDER_LIST}
        component={OrderListScreen}
        options={{
          tabBarIcon: ({ color, size }) => (
            <View>
              <Icon type="entypo" name="home" color={color} />
              <Text>Home</Text>
            </View>
          ),
        }}
      />
      <Tab.Screen
        name={routesEnum.CREATE_ORDER}
        component={CreateOrderScreen}
        options={{
          tabBarButton: (props) => <CutomTabBarButton {...props} />,
          tabBarIcon: ({ color, size }) => (
            <Icon type="ionicons" name="add" color="#fff" size={40} />
          ),
        }}
      />
      <Tab.Screen
        name={routesEnum.CUSTOMER_PROFILE}
        component={CustomerProfile}
        options={{
          tabBarIcon: ({ color, size }) => (
            <View>
              <Icon type="entypo" name="user" color={color} />
              <Text>Profile</Text>
            </View>
          ),
        }}
      />
    </Tab.Navigator>
  );
};
const styles = StyleSheet.create({
  shadow: {
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,

    elevation: 5,
  },
});
export default CustomerNavigation;
