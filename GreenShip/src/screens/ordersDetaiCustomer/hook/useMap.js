import {useState, useRef, useCallback, useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {unSetSelectedOrder} from '~/reduces/SelectedOrder';

const DEVIATION = 0.0002;

export function useMap() {
  const mapRef = useRef(null);
  const selectedMarker = useSelector(states => states.selectedOrder);
  const dispatch = useDispatch();
  useEffect(() => {
    if (mapRef) {
      mapRef.current.animateToRegion(
        {
          latitude: selectedMarker.latitude,
          longitude: selectedMarker.longitude,
          latitudeDelta: 0.05,
          longitudeDelta: 0.05,
        },
        500,
      );
    }
  }, [mapRef, selectedMarker]);

  const handelResetInitialPosition = useCallback(() => {
    if (mapRef) {
      mapRef.current.animateToRegion(
        {
          latitude: 10.7758439,
          longitude: 106.7017555,
          latitudeDelta: 0.003,
          longitudeDelta: 0.003,
        },
        500,
      );
      dispatch(unSetSelectedOrder());
    }
  }, [mapRef, selectedMarker]);

  return {
    mapRef,
    selectedMarker,
    handelResetInitialPosition,
  };
}
