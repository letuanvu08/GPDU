import React, {useEffect, useState} from 'react';
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
import orderApi from '~/api/orderApi';
export function OrderDetailScreen() {
  const {orderId} = useSelector(state => state.selectedOrder);
  const user = useSelector(state => state.auth.profile);
  const [order, setOrder] = useState(null);

  const getOrderDetail = orderId => {
    orderApi
      .getOrderById({orderId: orderId})
      .then(res => {
        console.log('orderDetail: ', res.Data);
        if (res.Data) {
          setOrder(res.Data);
        }
      })
      .catch(e => console.log(e));
  };
  useEffect(() => {
    console.log("orderid: ",orderId);
    getOrderDetail(orderId);
  }, [orderId]);
  return (
    <View>
      {!!order && (
        <ScrollView style={styles.container} nestedScrollEnabled={true}>
          {user?.typeUser === TypeUser.DRIVER && (
            <View style={styles.header}>
              <HeaderOrder orderId={order?.id} />
            </View>
          )}
          <View style={styles.content}>
            <InfoLocation order={order} />
          </View>
          <View style={styles.package}>
            <PackageInfo packageInfo={order?.packageInfo} />
          </View>
          <View style={styles.status}>
            <OrderStatus
              packageInfo={order?.historyStatus}
              typeUser={user?.typeUser}
            />
          </View>
        </ScrollView>
      )}
    </View>
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
    height: 50,
  },
  content: {
    height: 250,
  },
  package: {
    height: 120,
    marginBottom: 10,
  },
  status: {height: 150},
});
