import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import {useDispatch, useSelector} from 'react-redux';
import {logout} from '~/reduces/AuthReducer';
import EncryptedStorage from 'react-native-encrypted-storage';
import storageKeys from '~/utils/storageKeys';
import {Avatar} from 'react-native-elements';
import colors from '~/theme/colors';
import {fontSizes} from '~/theme/fonts';
import { paddings } from '~/theme/paddings';

const CustomerProfile = () => {
  const dispatch = useDispatch();
  const profile = useSelector(state => state.auth.profile);
  const handleLogout = async () => {
    await EncryptedStorage.setItem(storageKeys.ACCESS_TOKEN, '');
    await EncryptedStorage.setItem(storageKeys.REFRESH_TOKEN, '');
    dispatch(logout());
  };

  return (
    <View style={styles.container}>
      <Avatar source={require('../../assets/avatar.png')} size={200} />
      <View style={styles.contentContainer}>
        <Text style={styles.field}>Username</Text>
        <Text style={styles.text}>{profile?.userName}</Text>
        <Text style={styles.field}>Email</Text>
        <Text style={styles.text}>{profile?.email}</Text>
      </View>
      <Button
          title="Log out"
          onPress={handleLogout}
          buttonStyle={styles.button}
          titleStyle={styles.titleButton}
        />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: paddings.card
  },
  button: {
    backgroundColor: colors.brand.primary,
    width: 200,
    borderRadius: 10,
  },
  titleButton: {
    fontSize: fontSizes.button,
  },
  contentContainer: {
    marginTop: 50,
    alignSelf: "flex-start",
    width: "100%",
    marginBottom: 30
  },
  field: {
    fontSize: fontSizes.h5,
    color: colors.brand.primary,
    fontWeight: 'bold',
  },
  text: {
    fontSize: fontSizes.body,
    color: '#000',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 7,
    },
    shadowOpacity: 0.43,
    shadowRadius: 9.51,
    elevation: 15,
    borderRadius: 10,
    backgroundColor: "#fff",
    padding: paddings.input,
    marginVertical: 10
  },
});
export default CustomerProfile;
