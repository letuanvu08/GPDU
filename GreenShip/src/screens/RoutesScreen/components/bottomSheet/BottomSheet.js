import 'react-native-gesture-handler'
import 'react-native-reanimated'
import React from 'react';
import { Dimensions, StyleSheet, View } from 'react-native';
import ScrollBottomSheet from 'react-native-scroll-bottom-sheet';
import { ListItem } from './ListItem';
import { paddings } from '~/theme/paddings';
const windowHeight = Dimensions.get('window').height;

export function BottomSheet({ MarkerData }) {
  return (
    <ScrollBottomSheet
      componentType="FlatList"
      snapPoints={[100, '50%', windowHeight - 200]}
      initialSnapIndex={1}
      renderHandle={() => (
        <View style={styles.header}>
          <View style={styles.panelHandle} />
        </View>
      )}
      data={MarkerData}
      keyExtractor={(i) => i.id}
      renderItem={({ item }) => (
        <ListItem item={item} />
      )}
      contentContainerStyle={styles.contentContainerStyle}
    />
  );
}

const styles = StyleSheet.create({
  contentContainerStyle: {
    flex: 1,
    backgroundColor: 'white',
    padding: paddings.container
  },
  header: {
    alignItems: 'center',
    backgroundColor: 'white',
    paddingVertical: 20,
    borderTopEndRadius:30,
    borderTopStartRadius:30
  },
  panelHandle: {
    width: 41,
    height: 3,
    backgroundColor: '#E1E1E1',
    borderRadius: 17,
    opacity:0.9,
  },
});