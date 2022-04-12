import React from 'react';
import {View, Text, Button, ScrollView} from 'react-native';
import {StyleSheet} from 'react-native';
import theme from '~/theme';
import {Icon} from 'react-native-elements';
import {
  PackageInfo,
  InfoLocation,
  HeaderOrder,
  OrderStatus,
} from './components';
import {useSelector} from 'react-redux';
import TypeUser from '~/constants/TypeUser';
export function OrderDetailScreen() {
  const order = useSelector(state => state.selectedOrder);
  const user = useSelector(state => state.auth.profile);
  console.log(user);
  return (
    <ScrollView style={styles.container} nestedScrollEnabled={true}>
      {user?.typeUser === TypeUser.DRIVER && (
        <View style={styles.header}>
          <HeaderOrder orderId={order.id} />
        </View>
      )}
      <View style={styles.content}>
        <InfoLocation order={order} />
      </View>
      <View style={styles.package}>
        <PackageInfo packageInfo={order.packageInfo} />
      </View>
      <View style={styles.status}>
        <OrderStatus
          packageInfo={order.historyStatus}
          typeUser={user?.typeUser}
        />
      </View>
    </ScrollView>
  );
}

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 750,
    zIndex: 1,
    flexDirection: 'column',
    padding: theme.paddings.container,
    backgroundColor: theme.colors.bg.primary,
    borderWidth: 0,
  },
  header: {
    height: 40,
  },
  content: {
    height: 250,
  },
  package: {
    height: 120,
  },
  status: {height: 150},
});
