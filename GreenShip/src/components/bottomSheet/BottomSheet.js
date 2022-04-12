import 'react-native-gesture-handler'
import 'react-native-reanimated'
import React from 'react';
import { Dimensions, StyleSheet, View } from 'react-native';
import ScrollBottomSheet from 'react-native-scroll-bottom-sheet';
import { paddings } from '~/theme/paddings';
const windowHeight = Dimensions.get('window').height;

export function BottomSheet({ items, renderItem }) {
  return (
    <ScrollBottomSheet
      componentType='FlatList'
      snapPoints={[100, '50%', windowHeight - 200]}
      initialSnapIndex={1}
      renderHandle={() => (
        <View style={styles.header}>
          <View style={styles.panelHandle} />
        </View>
      )}
      data={items}
      keyExtractor={(i) => i.id}
      renderItem={renderItem}
      contentContainerStyle={styles.contentContainerStyle}
    />
  );
}

const styles = StyleSheet.create({
  contentContainerStyle: {
    flex: 100,
    backgroundColor: 'white',
    padding: paddings.container,
  },
  header: {
    alignItems: 'center',
    backgroundColor: 'white',
    paddingVertical: 10,
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