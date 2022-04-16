import React, {useEffect, useState} from 'react';
import {
  ActivityIndicator,
  PermissionsAndroid,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import MapView, {Marker, Polyline} from 'react-native-maps';
import Geolocation from 'react-native-geolocation-service';
import colors from '~/theme/colors';
import {Button, Icon} from 'react-native-elements';
import {paddings} from '~/theme/paddings';
import mapboxApi from '~/api/mapboxAPI';
import {fontSizes} from '~/theme/fonts';
import routesEnum from '~/constants/routesEnum';

const MapScreen = ({navigation}) => {
  const [granted, setGranted] = useState(null);
  const [location, setLocation] = useState({
    latitude: 10.7758439,
    longitude: 106.7017555,
  });
  const [initLocation, setInitLocation] = useState({
    latitude: location.latitude,
    longitude: location.longitude,
    latitudeDelta: 0.01,
    longitudeDelta: 0.01,
  });
  const [name, setName] = useState('');
  const requestPermission = async () => {
    try {
      const re = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        {
          title: 'Location Permission',
          message: 'GPDU needs access to your location ',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (re === PermissionsAndroid.RESULTS.GRANTED) {
        Geolocation.getCurrentPosition(
          position => {
            console.log('position', position);
            setLocation({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
            });
            setInitLocation({
              ...initLocation,
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
            });
            setGranted(true);
          },
          e => console.log(e),
          {enableHighAccuracy: true, timeout: 15000, maximumAge: 10000},
        );
      } else {
        setGranted(false);
      }
    } catch (err) {
      console.warn(err);
    }
  };
  useEffect(() => {
    requestPermission();
  }, []);
  useEffect(() => {
    console.log('location: ', location);
    setInitLocation({...initLocation, ...location});
  }, location);
  const handleRegionChange = e =>
    setLocation({longitude: e.longitude, latitude: e.latitude});

  const handleRegionChangeComplete = async e => {
    try {
      const re = await mapboxApi.geocodingReverse({
        longitude: e.longitude,
        latitude: e.latitude,
      });
      if (re.data.features.length > 0) setName(re.data.features[0].place_name);
    } catch (e) {
      console.log(e);
    }
  };
  const handleSelectLocation = () => {
    navigation.navigate(routesEnum.CREATE_ORDER, {location, name});
  };
  if (granted == null)
    return (
      <View style={styles.loading}>
        <ActivityIndicator size={'large'} color={colors.brand.primary} />
      </View>
    );
  return (
    <View style={styles.container}>
      <MapView
        initialRegion={initLocation}
        style={styles.map}
        onRegionChange={handleRegionChange}
        onRegionChangeComplete={handleRegionChangeComplete}>
        <Marker coordinate={location} />
      </MapView>
      <View style={styles.card}>
        <View style={styles.nameContainer}>
          <Icon name="location-pin" type="entypo" size={40} color="#e20000" />
          <Text style={styles.name}>{name}</Text>
        </View>
        <Button
          title="SELECT LOCATION"
          buttonStyle={styles.button}
          titleStyle={styles.buttonTitle}
          containerStyle={styles.buttonContainer}
          onPress={handleSelectLocation}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  loading: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  map: {
    flex: 1,
  },
  card: {
    backgroundColor: '#fff',
    width: '90%',
    height: 150,
    position: 'absolute',
    bottom: 10,
    left: '5%',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 4.65,
    padding: paddings.card,
    elevation: 8,
    borderRadius: 20,
    justifyContent: 'center',
  },
  button: {
    backgroundColor: '#E1E1E1',
    borderRadius: 10,
  },
  buttonTitle: {color: '#000'},
  buttonContainer: {
    width: '100%',
  },
  nameContainer: {
    flexDirection: 'row',
    marginBottom: 20,
  },
  name: {
    color: '#000',
    fontSize: fontSizes.body,
    flex: 1,
    fontWeight: 'bold',
  },
});
export default MapScreen;
