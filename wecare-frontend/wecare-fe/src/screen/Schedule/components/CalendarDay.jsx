// CalendarDay.jsx
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { FontFamily, FontSize, Colors, Padding } from '../../../styles/theme';

const CalendarDay = ({ date }) => {
  return (
    <View style={[styles.buttonsbuttondateday, styles.buttonsbuttondatedaySpaceBlock]}>
      <Text style={[styles.text9, styles.textTypo]}>{date}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  buttonsbuttondateday: {
    paddingVertical: Padding.p_10,
    paddingHorizontal: Padding.p_6,
    alignItems: 'center',
    flexDirection: 'row',
    position: 'absolute',
  },
  text9: {
    color: Colors.colorBlack,
    fontSize: FontSize.size_22,
    textAlign: 'center',
    lineHeight: 16,
    fontFamily: FontFamily.nanumSquareRoundOTF,
  },
  textTypo: {
    fontSize: FontSize.size_22,
    textAlign: 'center',
    lineHeight: 16,
    fontFamily: FontFamily.nanumSquareRoundOTF,
  },
  buttonsbuttondatedaySpaceBlock: {
    paddingVertical: Padding.p_10,
    paddingHorizontal: Padding.p_6,
    alignItems: 'center',
    flexDirection: 'row',
    position: 'absolute',
  },
});

export default CalendarDay;
