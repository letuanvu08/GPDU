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
import FakeData from '~/constants/FakeData';
export function OrderDetailScreen({}) {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <HeaderOrder orderId={FakeData.order.id} />
      </View>
      <View style={styles.content}>
        <InfoLocation order={FakeData.order} />
      </View>
      <View style={styles.package}>
        <PackageInfo packageInfo={FakeData.order.package} />
      </View>
      <View style={styles.status}>
        <OrderStatus packageInfo={FakeData.order.package} />
      </View>
    </ScrollView>
  );
}

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    padding: theme.paddings.card,
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
    height: 150,
  },
  status: {height: 190,
  },
});
