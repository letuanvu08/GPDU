import React, { useEffect } from "react";
import { ScrollView, View, Text, FlatList, StyleSheet } from "react-native";
import { useDispatch, useSelector } from "react-redux";
import { fetchOrders } from "~/reduces/OrdersReducer";
import { paddings } from "~/theme/paddings";
import OrderItem from "./components/OrderItem";

const OrderListScreen = () => {
  const renderItem = ({ item }) => <OrderItem item={item} />;
  const orders = useSelector((state) => state.orders.items);
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(fetchOrders());
  }, []);
  return (
    <FlatList
      data={orders}
      renderItem={renderItem}
      keyExtractor={(item) => item.id}
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
