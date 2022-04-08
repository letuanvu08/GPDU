import React, { Profiler } from "react";
import { DefaultTheme, NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import routesEnum from "../constants/routesEnum";
import RoutesScreen from "~/screens/RoutesScreen";
import OrderDetailScreen from "~/screens/ordersDetail";
import theme from "~/theme";
import { Icon } from "react-native-elements";
import { View } from "react-native";
import WelcomeScreen from "~/screens/auth/WelcomeScreen";
import CustomerLoginScreen from "~/screens/auth/CustomerLoginScreen";
import ShipperLoginScreen from "~/screens/auth/ShipperLoginScreen";
import ShipperSignupScreen from "~/screens/auth/ShipperSignupScreen";
import CustomerSignupScreen from "~/screens/auth/CustomerSignupScreen";
import LoadingModal from "~/modals/LoadingModal";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "~/reduces/AuthReducer";
import userTypes from "~/utils/user/userTypes";
import OrderListScreen from "~/screens/order/OrderListScreen";
import CustomerNavigation from "./CustomerNavigation";
import MapScreen from "~/screens/order/MapScreen";
import colors from "~/theme/colors";
export default function Navigation() {
  const Stack = createStackNavigator();
  const MyTheme = {
    ...DefaultTheme,
    colors: {
      ...DefaultTheme.colors,
      background: "#fff",
      card: colors.brand.primary,
      text: "#fff",
    },
  };
  const isAuth = useSelector((state) => state.auth.isAuth);
  const userType = useSelector((state) => state.auth.profile?.typeUser);
  return (
    <NavigationContainer theme={MyTheme}>
      <Stack.Navigator initialRouteName={routesEnum.WELCOME}>
        {isAuth && userType == userTypes.DRIVER && (
          <>
            <Stack.Screen
              options={{ headerShown: false }}
              name={routesEnum.ROUTES}
              component={RoutesScreen}
            />
            <Stack.Screen
              options={{
                title: "Order detail",
                headerStyle: {
                  backgroundColor: theme.colors.bg.primary,
                  borderWidth: 0,
                },
                headerTitleAlign: "center",
                headerTitleStyle: {
                  color: theme.colors.text.primary,
                },
              }}
              name={routesEnum.ORDERS_DETAIL}
              component={OrderDetailScreen}
            />
          </>
        )}
        {/* Auth */}
        {!isAuth && (
          <Stack.Group screenOptions={{ headerShown: false }}>
            <Stack.Screen name={routesEnum.WELCOME} component={WelcomeScreen} />
            <Stack.Screen
              name={routesEnum.CUSTOMER_LOGIN}
              component={CustomerLoginScreen}
            />
            <Stack.Screen
              name={routesEnum.SHIPPER_LOGIN}
              component={ShipperLoginScreen}
            />
            <Stack.Screen
              name={routesEnum.SHIPPER_SIGNUP}
              component={ShipperSignupScreen}
            />
            <Stack.Screen
              name={routesEnum.CUSTOMER_SIGNUP}
              component={CustomerSignupScreen}
            />
          </Stack.Group>
        )}
        {/* Order */}
        {isAuth && userType == userTypes.CUSTOMER && (
          <Stack.Screen
            name={routesEnum.CUSTOMER_NAVIGATION}
            component={CustomerNavigation}
            options={{ headerShown: false }}
          />
        )}
        {/* Modal */}
        <Stack.Group
          screenOptions={{
            presentation: "transparentModal",
            headerShown: false,
          }}
        >
          <Stack.Screen name={routesEnum.LOADING} component={LoadingModal} />
        </Stack.Group>
        {/* Map */}
        <Stack.Screen name={routesEnum.MAP} component={MapScreen} options={{title: "Map"}}/>
      </Stack.Navigator>
    </NavigationContainer>
  );
}
