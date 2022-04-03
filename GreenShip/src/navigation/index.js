import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import routesEnum from '../constants/routesEnum';
import RoutesScreen from '~/screens/RoutesScreen';
import OrderDetailScreen from '~/screens/ordersDetail';
import theme from '~/theme';
import {Icon} from 'react-native-elements';
import {View} from 'react-native';
export default function Navigation() {
  const Stack = createStackNavigator();
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          options={{headerShown: false}}
          name={routesEnum.ROUTES}
          component={RoutesScreen}
        />
        <Stack.Screen
            
          options={{
        
            title: 'Order detail',
            headerStyle: {
              backgroundColor: theme.colors.bg.primary,
              borderWidth: 0,
            },
            headerTitleAlign: 'center',
            headerTitleStyle: {
              color: theme.colors.text.primary,
            },
          }}
          name={routesEnum.ORDERS_DETAIL}
          component={OrderDetailScreen}
          
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
