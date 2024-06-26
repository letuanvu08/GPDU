import {useState, useRef, useCallback, useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {unSetSelectedOrder} from '~/reduces/SelectedOrder';

const DEVIATION = 0.0002;

export function useMap() {
  const mapRef = useRef(null);
  const [selectedMarker, setSelectedMarker] = useState({
    latitude: 10.7758439,
    longitude: 106.7017555,
  });
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

  const handelResetInitialToLocation = useCallback((location) => {
    if (mapRef) {
      mapRef.current.animateToRegion(
        {
          latitude: location.latitude,
          longitude: location.longitude,
          latitudeDelta: 0.05,
          longitudeDelta: 0.05,
        },
        500,
      );
      dispatch(unSetSelectedOrder());
    }
  }, [mapRef, selectedMarker]);

  return {
    mapRef,
    selectedMarker,
    handelResetInitialToLocation,
    setSelectedMarker,
  };
}
