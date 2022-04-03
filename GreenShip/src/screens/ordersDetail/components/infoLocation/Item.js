import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
import {SmallText, MediumText} from '~/components/Text';
export function Item({type, name, phone, address}) {
  return (
    <View style={styles.container}>
      <View style={styles.type}>
        <SmallText text={type} />
      </View>
      <View style={styles.nameAndPhone}>
        <View style={styles.name}>
          <MediumText text={name} />
        </View>
        <View style={styles.phone}>
          <SmallText text={'(' + phone + ')'} />
        </View>
      </View>
      <View style={styles.address}>
        <SmallText text={address} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    paddingVertical:5,
  },
  type: {
    paddingBottom: 5,
  },
  nameAndPhone: {
    flex: 1,
    flexDirection: 'row',
    width:'100%',
  },
  name: {
    paddingRight: 5,
  },
  phone: {
    paddingTop: 3,
  },
  address: {
    paddingTop:5,
  },
});
