import {StyleSheet} from 'react-native';
import paddings from "../../theme/paddings"
export const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  map: {
    flex: 1,
    StyleURL: 'mapbox://styles/mapbox/streets-v11',
    height: "100%",
    width: "100%",
  },
});
