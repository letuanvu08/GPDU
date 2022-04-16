import React, { useEffect, useState } from "react";
import {
  KeyboardAvoidingView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { Button, Icon, Input } from "react-native-elements";
import routesEnum from "~/constants/routesEnum";
import colors from "~/theme/colors";
import { fontSizes } from "~/theme/fonts";
import { paddings } from "~/theme/paddings";
import IconInput from "./components/IconInput";
import DatePicker from "react-native-date-picker";
import { useDispatch } from "react-redux";
import { addNewOrder } from "~/reduces/OrdersReducer";

const SENDER = "sender";
const RECERIVER = "receiver";
const EARLIEST = "earliest";
const LASTEST = "lastest";

const CreateOrderScreen = ({ navigation, route }) => {
  const [senderName, setSenderName] = useState("");
  const [senderPhone, setSenderPhone] = useState("");
  const [senderAddress, setSenderAddress] = useState("");
  const [senderLocation, setSenderLocation] = useState({});
  const [senderEarliestTime, setSenderEarliestTime] = useState(0);
  const [senderLastestTime, setSenderLastestTime] = useState(0);
  const [receiverName, setReceiverName] = useState("");
  const [receiverPhone, setReceiverPhone] = useState("");
  const [receiverAddress, setReceiverAddress] = useState("");
  const [receiverLocation, setReceiverLocation] = useState({});
  const [receiverEarliestTime, setReceiverEarliestTime] = useState(0);
  const [receiverLastestTime, setReceiverLastestTime] = useState(0);
  const [packageCategory, setPackageCategory] = useState("");
  const [packageDescription, setPackageDescription] = useState("");
  const [packageWeight, setPackageWeight] = useState(0);
  const [personType, setPersonType] = useState(SENDER);
  const [dateType, setDateType] = useState(EARLIEST);
  const [openPicker, setOpenPicker] = useState(false);
  const [date, setDate] = useState(new Date());
  const [note, setNote] = useState("");
  const dispatch = useDispatch();

  useEffect(() => {
    if (route.params) {
      const { location, name } = route.params;
      console.log(location, name);
      if (personType == SENDER) {
        setSenderAddress(name);
        setSenderLocation(location);
      } else {
        setReceiverAddress(name);
        setReceiverLocation(location);
      }
    }
  }, [route.params]);
  const handleOpenMap = (type) => () => {
    setPersonType(type);
    navigation.navigate(routesEnum.MAP);
  };

  const handleOpenPicker = ({ personType, dateType }) => () => {
    setPersonType(personType);
    setDateType(dateType);
    setOpenPicker(true);
  };
  const handlePickerConfirm = (date) => {
    setOpenPicker(false);
    setDate(date);
    if (personType == SENDER) {
      if (dateType == EARLIEST) setSenderEarliestTime(date);
      else setSenderLastestTime(date);
    } else {
      if (dateType == EARLIEST) setReceiverEarliestTime(date);
      else setReceiverLastestTime(date);
    }
  };
  const canCreate =
    senderAddress &&
    senderEarliestTime != 0 &&
    senderLastestTime != 0 &&
    senderLocation &&
    senderName &&
    senderPhone &&
    receiverAddress &&
    receiverEarliestTime != 0 &&
    receiverLastestTime != 0 &&
    receiverLocation &&
    receiverName &&
    receiverPhone &&
    packageCategory &&
    packageDescription &&
    packageWeight;
  const handleCreateOrder = () => {
    if (canCreate) {
      dispatch(
        addNewOrder({
          senderAddress,
          senderEarliestTime: Date.parse(senderEarliestTime) / 1000,
          senderLastestTime: Date.parse(senderLastestTime) / 1000,
          senderLocation,
          senderName,
          senderPhone,
          receiverAddress,
          receiverEarliestTime: Date.parse(receiverEarliestTime) / 1000,
          receiverLastestTime: Date.parse(receiverLastestTime) / 1000,
          receiverLocation,
          receiverName,
          receiverPhone,
          packageCategory,
          packageDescription,
          packageWeight,
          note,
        })
      );
      navigation.navigate(routesEnum.ORDER_LIST);
    }
  };
  return (
    <KeyboardAvoidingView behavior="position" keyboardVerticalOffset={-250}>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Create a new order</Text>
        <View style={styles.card}>
          <Text style={styles.item}>Sender</Text>
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Name",
              value: senderName,
              onChangeText: setSenderName,
            }}
            icon={{ name: "user", type: "ant-design" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Phone",
              value: senderPhone,
              onChangeText: setSenderPhone,
              keyboardType: "numeric",
            }}
            icon={{ name: "phone", type: "ant-design" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Address",
              value: senderAddress,
              onChangeText: setSenderAddress,
            }}
            icon={{ name: "location", type: "entypo" }}
            right={
              <Icon
                Component={TouchableOpacity}
                name="map"
                type="entypo"
                color={colors.brand.primary}
                size={20}
                onPress={handleOpenMap(SENDER)}
              />
            }
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Earliest time",
              value: senderEarliestTime.toString(),
              onChangeText: setSenderEarliestTime,
              onPress: handleOpenPicker({
                personType: SENDER,
                dateType: EARLIEST,
              }),
            }}
            icon={{ name: "time-outline", type: "ionicon" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Lastest time",
              value: senderLastestTime.toString(),
              onChangeText: setSenderLastestTime,
              onPress: handleOpenPicker({
                personType: SENDER,
                dateType: LASTEST,
              }),
            }}
            icon={{ name: "time", type: "ionicon" }}
          />
        </View>
        <View style={styles.card}>
          <Text style={styles.item}>Receiver</Text>
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Name",
              value: receiverName,
              onChangeText: setReceiverName,
            }}
            icon={{ name: "user", type: "ant-design" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Phone",
              value: receiverPhone,
              onChangeText: setReceiverPhone,
              keyboardType: "numeric",
            }}
            icon={{ name: "phone", type: "ant-design" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Address",
              value: receiverAddress,
              onChangeText: setReceiverAddress,
            }}
            icon={{ name: "location", type: "entypo" }}
            right={
              <Icon
                Component={TouchableOpacity}
                name="map"
                type="entypo"
                color={colors.brand.primary}
                size={20}
                onPress={handleOpenMap(RECERIVER)}
              />
            }
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Earliest time",
              value: receiverEarliestTime.toString(),
              onChangeText: setReceiverEarliestTime,
              onPress: handleOpenPicker({
                personType: RECERIVER,
                dateType: EARLIEST,
              }),
            }}
            icon={{ name: "time-outline", type: "ionicon" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Lastest time",
              value: receiverLastestTime.toString(),
              onChangeText: setReceiverLastestTime,
              onPress: handleOpenPicker({
                personType: RECERIVER,
                dateType: LASTEST,
              }),
            }}
            icon={{ name: "time", type: "ionicon" }}
          />
        </View>
        <View style={styles.card}>
          <Text style={styles.item}>Package</Text>
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Category",
              value: packageCategory,
              onChangeText: setPackageCategory,
            }}
            icon={{ name: "category", type: "material-icon" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Description",
              value: packageDescription,
              onChangeText: setPackageDescription,
            }}
            icon={{ name: "package", type: "feather" }}
          />
          <IconInput
            style={styles.input}
            input={{
              placeholder: "Weight",
              value: packageWeight,
              onChangeText: (t) => setPackageWeight(Number(t)),
              keyboardType: "numeric",
            }}
            icon={{ name: "weight-hanging", type: "font-awesome-5" }}
            right={<Text style={{ color: "#000" }}>kg</Text>}
          />
        </View>
        <View style={styles.card}>
          <Text style={styles.item}>Note</Text>
          <TextInput
            style={styles.note}
            multiline
            numberOfLines={5}
            value={note}
            onChangeText={setNote}
          />
        </View>
        <Button
          title="CREAT ORDER"
          buttonStyle={styles.button}
          onPress={handleCreateOrder}
          disabled={!canCreate}
        />
      </ScrollView>
      <DatePicker
        modal
        mode="datetime"
        open={openPicker}
        date={date}
        onConfirm={handlePickerConfirm}
        onCancel={() => {
          setOpenPicker(false);
        }}
      />
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingBottom: 120,
  },
  title: {
    fontSize: fontSizes.title,
    fontWeight: "bold",
    color: colors.brand.primary,
    margin: 10,
    alignSelf: "center",
  },
  card: {
    backgroundColor: colors.bg.card,
    padding: paddings.card,
    marginVertical: 5,
  },
  item: {
    color: "#000",
    fontSize: fontSizes.body,
    fontWeight: "bold",
  },
  input: {
    borderColor: "#000",
    borderWidth: 1,
    paddingVertical: 3,
    marginVertical: 5,
    backgroundColor: "#fff",
  },
  note: {
    backgroundColor: "#fff",
    borderWidth: 1,
    padding: 10,
    textAlignVertical: "top",
  },
  button: {
    backgroundColor: colors.brand.primary,
    borderRadius: 5,
    width: "80%",
    alignSelf: "center",
  },
});
export default CreateOrderScreen;
