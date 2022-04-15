import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  Pressable,
  TouchableOpacity,
  Image,
} from 'react-native';
import {Icon} from 'react-native-elements';
import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {useDispatch} from 'react-redux';
import {setSelectedOrder} from '~/reduces/SelectedOrder';
import {useNavigation} from '@react-navigation/native';
import routesEnum from '~/constants/routesEnum';

export function ListItem({item, onSelected}) {
  console.log("item: ", item);
  const navigation = useNavigation();
  const dispatch = useDispatch();

  const handleClickDetail = () => {
    dispatch(setSelectedOrder({...item}));
    
    navigation.navigate(routesEnum.ORDERS_DETAIL);
  };
  const handleSelected = ()=>{
    onSelected(item);
  }
  return (
    <Pressable
      style={({pressed}) => [
        {
          backgroundColor: pressed ? '#FAFAFA' : 'white',
        },
        styles.item,
      ]}
      onPress={handleSelected}>
      <View style={styles.logo}>
      <Icon name="location-sharp" type='ionicon'></Icon>
      </View>
      <View style={styles.content}>
        <Text style={styles.title}>{item.customerName}</Text>
        <Text style={styles.direction}>{item.address}</Text>
      </View>
      <TouchableOpacity onPress={handleClickDetail}>
        <Icon name="chevron-forward-outline" type="ionicon" />
      </TouchableOpacity>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  item: {
    flexDirection: 'row',
    padding: 20,
    paddingRight: 20,
    borderBottomWidth: 0.5,
    borderColor: '#E1E1E1',
    alignItems: 'center',
    borderRadius: 10,
  },
  logo: {
    height: 32,
    width: 32,
    borderRadius: 50,
    marginRight: 19,
    alignItems: 'center',
    justifyContent: 'center',
    opacity: 1,
  },
  logoImage: {
    width: 30,
    height: 30,
  },
  content: {
    width: 260,
  },
  title: {
    fontSize: 14,
    fontWeight: '600',
    color: '#2F3136',
  },
  direction: {
    fontSize: 14,
    fontWeight: '400',
    color: '#989CA5',
  },
  detail: {},
  ImageDetail: {},
});
