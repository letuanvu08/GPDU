import React from 'react';
import {View, Text} from 'react-native';
import {StyleSheet} from 'react-native';
import theme from '~/theme';
import {IconInfoLocation} from './IconInfoLocation';
import {Item} from './Item';
import {Distance} from './Distance';
import {or} from 'react-native-reanimated';
export function InfoLocation({order}) {
  return (
    <View style={styles.container}>
      <View style={styles.icon}>
        <IconInfoLocation />
      </View>
      <View style={styles.content}>
        <View style={styles.item}>
          <Item
            type="Sender"
            name={order.sender.name}
            phone={order.sender.phone}
            address={order.sender.address}
          />
        </View>
        <View style={styles.distance}>
          <Distance distance={order.distance} />
        </View>
        <View style={styles.item}>
          <Item
            type="Recipient"
            name={order.recipient.name}
            phone={order.recipient.phone}
            address={order.recipient.address}
          />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: theme.colors.bg.primary,
  },
  icon: {
    width: 20,
    padding: theme.paddings.card,
    height: '100%',
  },
  content: {
    flex: 1,
    width: 400,
    height: '100%',
    flexDirection: 'column',
    padding: theme.paddings.card,
  },
  item: {
    height:90,
  },
  distance: {
    height: 30,
  },

  infoLocationContent: {},
});
