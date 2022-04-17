import React, {useEffect, useState} from 'react';
import {StyleSheet, View, Dimensions} from 'react-native';
import MapView, {PROVIDER_GOOGLE} from 'react-native-maps';
import mapStyle from './map.style';
import TopBar from '~/components/topBar';
import BottomSheet from '~/components/bottomSheet';
import CustomPolyLine from '~/components/customPolyLine';
import {ListItem} from './components/ListItem';
import {useMap} from '~/hooks/useMap';
import FAKE_DATA from '~/constants/FakeData';
import {setSelectedOrder} from '~/reduces/SelectedOrder';
import CustomMarker from '~/components/marker';
import {useDispatch} from 'react-redux';
import routingApi from '~/api/routingApi';
import {useSelector} from 'react-redux';
import mapboxApi from '~/api/mapboxAPI';
import Geolocation from 'react-native-geolocation-service';
import {default as LocationImage} from '~/assets/icons/location_blue.png';
import vehicleApi from '~/api/vehicleApi';
export function RoutesScreen() {
  const user = useSelector(state => state.auth.profile);
  const [routing, setRouting] = useState(null);
  const [polyline, setPolyline] = useState([]);
  const [nodes, setNotes] = useState([]);
  const {mapRef, handelResetInitialToLocation, setSelectedMarker} = useMap();
  const dispatch = useDispatch();
    const [location, setLocation] = useState({
    latitude: 10.7758439,
    longitude: 106.7017555,
  });
  const [initialRegion, setInitialRegion] = useState({
    latitude: location.latitude,
    longitude: location.longitude,
    latitudeDelta: 0.05,
    longitudeDelta: 0.05,
  });

  useEffect(() => {
    const intervalId = setInterval(() => {
      fetchLocation();
    }, 5000);
    return () => clearInterval(intervalId);
  }, []);

  const updateLocationVehicle = ({location}) => {
    vehicleApi
      .updateLocationVehicle({vehicleId: user.vehicleId, location: location})
      .then(re =>
        console.log(
          'Update location vehicleId: %s, response: %s ',
          user.vehicleId,
          re.ReturnMessage,
        ),
      );
  };
  const fetchLocation = () => {
    Geolocation.getCurrentPosition(
      position => {
        console.log('position', position);
        setLocation({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
        updateLocationVehicle({
          location: {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
          },
        });
      },
      e => console.log(e),
      {enableHighAccuracy: true, timeout: 15000, maximumAge: 10000},
    );
  };

  useEffect(() => {
    if (!!user) {
      getRouting(user);
    }
  }, [user]);
  useEffect(() => {
    if (!!routing) {
      console.log('routing: ', routing);
      const locations = routing.nodes.map(node => node.location);
      setNotes(routing.nodes);
      getPolyline(locations);
    }
  }, [routing]);

  const getRouting = user => {
    routingApi
      .getRoutingByVehicleId({vehicleId: user.vehicleId})
      .then(res => {
        if (!!res.Data) {
          setRouting(res.Data);
        }
      })
      .catch(e => console.log(e));
  };

  useEffect(() => {
    if (nodes.length > 0) {
      setInitialRegion({
        ...initialRegion,
        latitude: nodes[0].location.latitude,
        longitude: nodes[0].location.longitude,
      });
    }
  }, [nodes]);

  const getPolyline = locations => {
    mapboxApi
      .direction(locations)
      .then(res => {
        const coordinates = res.data.routes[0]?.geometry.coordinates || [];
        const mapPolyline = coordinates.map(coordinate => ({
          longitude: coordinate[0],
          latitude: coordinate[1],
        }));
        console.log('mapPolyline', res.data);
        setPolyline(mapPolyline);
      })
      .catch(e => console.log(e));
  };
  const handleSelectedMarker = node => {
    setSelectedMarker(node.location);
  };

  return (
    <View style={styles.container}>
      <TopBar onPressElement={()=> handelResetInitialToLocation(location)} />
      <MapView
        ref={mapRef}
        customMapStyle={mapStyle}
        provider={PROVIDER_GOOGLE}
        style={styles.mapStyle}
        initialRegion={initialRegion}
        mapType="standard">
          <CustomMarker coordinate={location} image={LocationImage}/>
        {nodes.map((node, index) => (
          <CustomMarker
            coordinate={node.location}
          />
        ))}
        {polyline.length > 0 && <CustomPolyLine coordinates={polyline} />}
      </MapView>
      <BottomSheet
        items={nodes}
        renderItem={item => (
          <ListItem item={item} onSelected={handleSelectedMarker} />
        )}
      />
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
