// CalendarHeader.jsx
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
// import Icondateyesterday from '../../../../assets/Iconsvg/Idea/IconDateYesterday.svg';
// import Icondatetomorrow from '../../../../assets/Iconsvg/Idea/IconDateTomorrow.svg';
import Icondateyesterday from '@assets/Iconsvg/Idea/IconDateYesterday.svg';
import Icondatetomorrow from '@assets/Iconsvg/Idea/IconDateTomorrow.svg';
import { FontFamily, FontSize, Colors } from '@styles/theme';

const CalendarHeader = () => {
  return (
    <View style={styles.datemonth}>
      <Icondateyesterday style={styles.iconbell} width={24} height={24} />
      <Text style={styles.month}>2025년 7월</Text>
      <Icondatetomorrow style={styles.iconbell} width={24} height={24} />
    </View>
  );
};

const styles = StyleSheet.create({
  datemonth: {
    gap: 70,
    alignItems: 'center',
    flexDirection: 'row',
  },
  month: {
    letterSpacing: 0.38,
    lineHeight: 24,
    fontSize: FontSize.size_20,
    color: Colors.colorBlack,
    textAlign: 'left',
    fontFamily: FontFamily.nanumSquareRoundOTF,
    fontWeight: '700',
  },
  iconbell: {
    width: 24,
    overflow: 'hidden',
    height: 24,
  },
});

export default CalendarHeader;
