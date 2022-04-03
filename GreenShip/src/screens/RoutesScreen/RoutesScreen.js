import React, {useEffect, useState} from 'react';
import {StyleSheet, View, Dimensions} from 'react-native';
import MapView, {PROVIDER_GOOGLE} from 'react-native-maps';
import mapStyle from './map.style';
import {TopBar, BottomSheet, CustomPolyLine} from './components';
import {useMap} from './hook/useMap';
import FAKE_DATA from '~/constants/FakeData';
import {setSelectedOrder} from '~/reduces/SelectedOrder';
import CustomMarker from '~/components/marker';
import {useDispatch} from 'react-redux';
export function RoutesScreen() {
  const [markers, setMarkers] = useState([]);
  const [coordinates, setCoordinates] = useState([]);
  const dispatch = useDispatch();
  const [initialRegion, setInitialRegion] = useState({
    latitude: 10.7758439,
    longitude: 106.7017555,
    latitudeDelta: 0.01,
    longitudeDelta: 0.01,
  });
  useEffect(() => {
    setMarkers(FAKE_DATA.MarkerData);
    setCoordinates(FAKE_DATA.coordinatesLine);
  }, []);

  useEffect(() => {
    if (markers.length > 0) {
      setInitialRegion({
        ...initialRegion,
        latitude: markers[0].latitude,
        longitude: markers[0].longitude,
      });
    }
  }, [markers]);
  const {mapRef, handelResetInitialPosition} = useMap();

  const handleClickMark = marker => {
    dispatch(setSelectedOrder({...marker}));
  };

  return (
    <View style={styles.container}>
      <TopBar onPressElement={handelResetInitialPosition} />
      <MapView
        ref={mapRef}
        customMapStyle={mapStyle}
        provider={PROVIDER_GOOGLE}
        style={styles.mapStyle}
        initialRegion={initialRegion}
        on
        mapType="standard">
        {markers.map(marker => (
          <CustomMarker
            key={marker.id}
            id={marker.id}
            onSelect={handleClickMark}
            marker={marker}
            type={marker.type}
          />
        ))}
        <CustomPolyLine coordinates={coordinates} />
      </MapView>
      <BottomSheet MarkerData={markers} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
    alignItems: 'center',
    justifyContent: 'center',
  },
  mapStyle: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
  },
});
