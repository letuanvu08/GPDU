import React from "react";
import { StyleSheet, TextInput, View } from "react-native";
import { Icon } from "react-native-elements";

const IconInput = ({
  icon: { name, type },
  input: { value, onChangeText, placeholder, keyboardType, onPress, editable },
  style,
  right,
}) => {
  return (
    <View style={[styles.container, style]}>
      <Icon name={name} type={type} size={20} />
      <TextInput
        value={value}
        onChangeText={onChangeText}
        placeholder={placeholder}
        style={styles.input}
        keyboardType={keyboardType}
        onFocus={onPress}
      />
      {right}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    paddingHorizontal: 10,
  },
  input: {
    paddingVertical: 5,
    flex: 1,
  },
});
export default IconInput;
