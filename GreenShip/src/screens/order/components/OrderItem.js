import React from "react";
import { StyleSheet, Text, View } from "react-native";
import colors from "~/theme/colors";
import { fontSizes } from "~/theme/fonts";
import { paddings } from "~/theme/paddings";
import OrderLocations from "./OrderLocations";

const OrderItem = ({ item }) => {
  return (
    <View style={styles.container}>
      <View style={styles.left}>
        <Text>WEIGHT</Text>
        <Text style={styles.weight}>{item.packageInfo?.weight}kg</Text>
      </View>
      <View style={styles.line} />
      <View style={styles.right}>
        <OrderLocations from={item.pickup?.address} to={item.delivery?.address} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: paddings.card,
    backgroundColor: colors.bg.card,
    marginVertical: 10,
    flexDirection: "row",
    flex: 1,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 6,
    },
    shadowOpacity: 0.37,
    shadowRadius: 7.49,
    elevation: 12,
    borderRadius: 10,
  },
  title: {
    color: colors.brand.primary,
    fontWeight: "bold",
    fontSize: fontSizes.body,
  },
  line: {
    width: 1,
    backgroundColor: "#a1a1a1",
  },
  left: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  right: {
    flex: 3,
  },
  weight: {
    color: "#000",
    fontSize: fontSizes.h3,
    fontWeight: "bold",
  },
});
export default OrderItem;
