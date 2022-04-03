import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
import theme from '~/theme';
export function LargeText({text}) {
  return (
      <Text style={styles.style}>{text}</Text>
  );
}
const styles = StyleSheet.create({
  style: {
    fontSize: theme.fontSizes.h4,
    fontFamily: theme.fonts.body,
    fontWeight: theme.fontWeights.bold,
    color: theme.colors.text.primary,
  },
});
