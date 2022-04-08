import React from "react";
import { StyleSheet, Text, View } from "react-native";

const OrderLocations = ({ from, to }) => {
  return (
    <View style={styles.container}>
      <View style={styles.part}>
        <View style={styles.paint}>
          <View style={styles.blank} />
          <View style={styles.circle} />
          <View style={styles.dashedLine} />
        </View>
        <Text style={styles.text}>{from}</Text>
      </View>
      <View style={styles.part}>
        <View style={styles.paint}>
          <View style={styles.dashedLine} />
        </View>
        <View style={styles.line} />
      </View>
      <View style={styles.part}>
        <View style={styles.paint}>
          <View style={styles.dashedLine} />
          <View style={styles.circle} />
          <View style={styles.blank} />
        </View>
        <Text style={styles.text}>{to}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  part: {
    flexDirection: "row",
    alignItems: "center",
  },
  paint: {
    alignItems: "center",
    marginRight: 5,
    justifyContent: "center",
    width: 10,
  },
  circle: {
    borderWidth: 2,
    height: 10,
    width: 10,
    borderRadius: 5,
  },
  dashedLine: {
    borderStyle: "dashed",
    borderWidth: 1,
    borderColor: "#797979",
    flex: 1,
  },
  blank: {
    flex: 1,
  },
  text: {
    color: "#000",
  },
  line: {
    height: 1,
    width: "100%",
    backgroundColor: "#aaaaaa",
    marginVertical: 10,
  },
});
export default OrderLocations;
