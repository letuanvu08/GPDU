import React from 'react';
import {View, Text, Button} from 'react-native';
import {StyleSheet} from 'react-native';
import theme from '~/theme';

export function SmallText({text ,props} ) {
  return (
      <Text style={[styles.style, props]}>{text}</Text>
  );
}
const styles = StyleSheet.create({
  style: {
    fontSize: theme.fontSizes.body,
    fontFamily: theme.fonts.body,
    fontWeight: theme.fontWeights.regular,
    color: theme.colors.text.secondary,
  },
});
