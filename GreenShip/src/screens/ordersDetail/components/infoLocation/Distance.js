import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
import {MediumText} from '~/components/Text';
import { SmallText } from '~/components/Text';
export function Distance({distance}) {
  
  return (
    <View style={styles.container}>
      <View style={styles.line}></View>
      <View style={styles.distance}>
        {/* <SmallText text={distance + ' Km'} /> */}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    padding:5,
  },
  distance: {
    paddingLeft: 5,
  },
  line: {
    borderBottomColor: 'black',
    borderBottomWidth: 2,
    borderStyle: 'solid',
    width: '100%',
    height: 1,
    opacity: 0.2,
    paddingTop: 12,
  },
});
