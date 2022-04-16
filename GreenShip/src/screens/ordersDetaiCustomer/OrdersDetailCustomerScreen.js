import React, {useEffect, useState, useMemo} from 'react';
import {StyleSheet, View, Dimensions} from 'react-native';
import MapView, {PROVIDER_GOOGLE, Marker} from 'react-native-maps';
import mapStyle from './map.style';
import TopBar from '~/components/topBar';
import BottomSheet from '~/components/bottomSheet';
import {useMap} from '~/hooks/useMap';
import {setSelectedOrder} from '~/reduces/SelectedOrder';
import {useSelector} from 'react-redux';
import {useDispatch} from 'react-redux';
import OrderDetailScreen from '../ordersDetail';
import {Icon} from 'react-native-elements';
import mapboxApi from '~/api/mapboxAPI';
import CustomPolyLine from '~/components/customPolyLine';
import orderApi from '~/api/orderApi';
import routingApi from '~/api/routingApi';
export function OrderDetailCustomerScreen() {
  const {orderId} = useSelector(state => state.selectedOrder);
  const [order, setOrder] = useState(null);
  console.log('order: ', order);
  const [polyline, setPolyline] = useState([]);
  const dispatch = useDispatch();
  const {mapRef, handelResetInitialPosition, setSelectedMarker} = useMap();
  const [routing, setRouting] = useState(null);
  const [initialRegion, setInitialRegion] = useState({
    latitude: 10.7758439,
    longitude: 106.7017555,
    latitudeDelta: 0.01,
    longitudeDelta: 0.01,
  });
  const getOrderDetail = orderId => {
    orderApi
      .getOrderById({orderId: orderId})
      .then(res => {
        if (res.Data) {
          setOrder(res.Data);
        }
      })
      .catch(e => console.log(e));
  };
  useEffect(() => {
    getOrderDetail(orderId);
    getRouting(orderId);
  }, [orderId]);

  useEffect(() => {
    if (!!order) {
      setInitialRegion({
        ...initialRegion,
        latitude: order.pickup.location.latitude,
        longitude: order.pickup.location.longitude,
      });
      setSelectedMarker(order.pickup.location);
    }
  }, [order]);

  useEffect(() => {
    if (routing) {
      getPolyline(routing.nodes.map(node => node.location));
    }
  }, [routing]);

  const getRouting = orderId => {
    routingApi.getRoutingByOrderId({orderId: orderId}).then(res => {
      console.log("routing: ", res)
      if (res.Data) {

        setRouting(res.Data);
      }
    });
  };

  const getPolyline = locations => {
    mapboxApi
      .direction(locations)
      .then(res => {
        const coordinates = res.data.routes[0].geometry.coordinates;
        const mapPolyline = coordinates.map(coordinate => ({
          longitude: coordinate[0],
          latitude: coordinate[1],
        }));
        console.log('mapPolyline', mapPolyline);
        setPolyline(mapPolyline);
      })
      .catch(e => console.log(e));
  };

  return (
    <View style={styles.container}>
      <MapView
        ref={mapRef}
        customMapStyle={mapStyle}
        provider={PROVIDER_GOOGLE}
        style={styles.mapStyle}
        initialRegion={initialRegion}
        mapType="standard">
        {!!order && (
          <View>
            <Marker coordinate={order.pickup.location} />
            <Marker coordinate={order.delivery.location} />
            <Marker coordinate={order.currentLocation}>
              <Icon name="package" type="feather" size={24} />
            </Marker>
            <CustomPolyLine coordinates={polyline} />
          </View>
        )}
      </MapView>
      <BottomSheet items={[order]} renderItem={item => <OrderDetailScreen />} />
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
