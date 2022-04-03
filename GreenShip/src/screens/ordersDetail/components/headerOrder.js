import React from 'react';
import {View} from 'react-native';
import {StyleSheet} from 'react-native';
import {SmallText, MediumText} from '~/components/Text';
export function HeaderOrder({orderId}) {
  return (
    <View style={styles.header}>
      <View style={styles.title}>
        <SmallText text="Order ID" />
      </View>
      <View style={styles.orderId}>
        <MediumText text={orderId} />
      </View>
    </View>
  );
}

export const styles = StyleSheet.create({
  header: {
  },
  title: {
    paddingBottom: 5,
  },
  orderId: {
    // padding: 5,
  },
});
