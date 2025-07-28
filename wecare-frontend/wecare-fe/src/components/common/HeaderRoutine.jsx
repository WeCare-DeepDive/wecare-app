import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import IconDateYesterday from '../../../assets/Iconsvg/Idea/IconDateYesterday.svg';
import { Theme } from '../../styles/theme';

export default function HeaderRoutine({ backgroundType = 'fill', title, saveButton = 'false' }) {
  return (
    <SafeAreaView style={styles.headeralarminfo}>
      <View style={[styles.view, styles.viewFlexBox]}>
        <View style={[styles.body, styles.viewFlexBox]}>
          <IconDateYesterday />
          <View></View>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  headeralarminfo: {
    backgroundColor: Theme.Colors.customWhite,
    flex: 1,
  },
  viewFlexBox: {
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    flex: 1,
  },
  text1Layout: {
    width: 37,
    alignSelf: 'stretch',
  },
  iconcontainer: {
    maxHeight: '100%',
  },
  text: {
    fontSize: 24,
    lineHeight: 38,
    fontWeight: '700',
    color: Theme.Colors.customBlack,
    textAlign: 'center',
    fontFamily: Theme.FontFamily.nanumR,
  },
  text1: {
    fontSize: 20,
    lineHeight: 32,
    color: Theme.Colors.purple600,
    textAlign: 'left',
    display: 'flex',
    fontFamily: Theme.FontFamily.nanumR,
    alignItems: 'center',
    alignSelf: 'stretch',
  },
  wrapper: {
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'stretch',
    flexDirection: 'row',
  },
  body: {
    gap: 0,
  },
  view: {
    width: '100%',
    shadowColor: Theme.Colors.iconDisable,
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowRadius: 6,
    elevation: 6,
    shadowOpacity: 1,
    height: 56,
    paddingHorizontal: 20,
    paddingVertical: 10,
    backgroundColor: Theme.Colors.customWhite,
  },
});
