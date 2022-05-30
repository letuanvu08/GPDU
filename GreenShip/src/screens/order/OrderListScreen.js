import React, {useEffect} from 'react';
import {ScrollView, View, Text, FlatList, StyleSheet} from 'react-native';
import {useDispatch, useSelector} from 'react-redux';
import {fetchOrders, flushOrders} from '~/reduces/OrdersReducer';
import {setSelectedOrder} from '~/reduces/SelectedOrder';
import {paddings} from '~/theme/paddings';
import OrderItem from './components/OrderItem';
import {useNavigation} from '@react-navigation/native';
import routesEnum from '~/constants/routesEnum';

const OrderListScreen = () => {
  const orders = useSelector(state => state.orders.items);
  const dispatch = useDispatch();
  const navigation = useNavigation();
  useEffect(() => {
    dispatch(fetchOrders());
    return () => dispatch(flushOrders());
  }, []);

  const handleSelectedOrder = order => {
    console.log(order);
    dispatch(setSelectedOrder({orderId: order.id}));
    navigation.navigate(routesEnum.ORDERS_DETAIL_CUSTOMER);
  };
  const renderItem = ({item}) => (
    <OrderItem item={item} onSelected={handleSelectedOrder} />
  );

  return (
    <FlatList
      data={orders}
      renderItem={renderItem}
      keyExtractor={item => item.id}
      onEndReached={() => dispatch(fetchOrders())}
      onEndReachedThreshold={0.7}
      contentContainerStyle={styles.container}
    />
  );
};

const styles = StyleSheet.create({
  container: {
    padding: paddings.container,
  },
});
export default OrderListScreen;
