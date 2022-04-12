import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
import theme from '~/theme';
import {SmallText, MediumText} from '~/components/Text';
export function PackageInfo({packageInfo}) {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={{paddingRight: 5}}>
          <SmallText text="Package: " />
        </View>
        <MediumText text={packageInfo.name} />
      </View>
      <View style={styles.content}>
        <View style={[styles.column, {width: '40%'}]}>
          <View style={styles.title}>
            <SmallText text="Category" />
          </View>
          <View>
            <SmallText text={packageInfo.category} />
          </View>
        </View>
        <View style={[styles.column, {width: '40%'}]}>
          <View style={styles.title}>
            <SmallText text="Weight" />
          </View>
          <View>
            <SmallText text={packageInfo.weight + ' kg '} />
          </View>
        </View>
        <View style={[styles.column]}>
          <View style={styles.title}>
            <SmallText text="Vehicle" />
          </View>
          <View style={styles.text}>
            <SmallText text={packageInfo.vehicle} />
          </View>
        </View>
      </View>
    </View>
  );
}

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    padding: theme.paddings.container,
  },
  header: {
    flex: 1,
    height: 1,
    flexDirection: 'row',
    backgroundColor: theme.colors.bg.primary,
  },
  content: {
    flex: 1,
    flexDirection: 'row',
    width: '100%',
    alignContent: 'center',
    paddingBottom: 20,
  },
  column: {
    alignContent: 'center',
  },
  title: {
    paddingBottom: 10,
  },
});
