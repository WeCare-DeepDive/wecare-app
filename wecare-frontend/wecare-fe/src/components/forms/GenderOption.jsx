// src/components/forms/GenderOption.jsx
import React from 'react';
import { TouchableOpacity, View, Text, StyleSheet } from 'react-native';
import { Theme } from '@styles/theme';
import RadioButton from '@assets/Iconsvg/Buttons/RadioButtons.svg';
import RadioButtonChecked from '@assets/Iconsvg/Buttons/RadioButtons-check.svg';

const GenderOption = ({ label, value, selectedValue, onSelect, fontSize, lineHeight }) => {
  const isSelected = selectedValue === value;

  return (
    <TouchableOpacity style={[styles.radioFlexBox]} onPress={() => onSelect(value)}>
      {/* 이미지로 라디오 버튼 */}
      {isSelected ? (
        <RadioButtonChecked width={24} height={24} />
      ) : (
        <RadioButton width={24} height={24} />
      )}
      <Text style={[styles.textTypo, { fontSize, lineHeight }]}>{label}</Text>
    </TouchableOpacity>
  );
};


const styles = StyleSheet.create({
  radioFlexBox: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.Gap.sm,
  },
  textTypo: {
    textAlign: 'center',
    fontFamily: Theme.FontFamily.pretendard,
  },
});



// const styles = StyleSheet.create({
//   radioFlexBox: {
//     flexDirection: 'row',
//     alignItems: 'center',
//   },
//   buttonsLayout: {
//     height: 24,
//     borderRadius: 631,
//     width: 24,
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   textTypo: {
//     textAlign: 'center',
//     fontFamily: Theme.FontFamily.pretendard,
//   },
//   buttons: {
//     backgroundColor: Theme.Colors.purple500,
//     paddingHorizontal: 4,
//     paddingVertical: 6,
//     flexDirection: 'row',
//     alignItems: 'center',
//     justifyContent: 'center',
//     borderRadius: 50,
//   },
//   buttons1: {
//     backgroundColor: Theme.Colors.customWhite,
//     borderColor: Theme.Colors.gray7,
//     borderWidth: 1.3,
//     overflow: 'hidden',
//     borderStyle: 'solid',
//     borderRadius: 50,
//   },
//   checkMark: {
//     position: 'absolute',
//     top: 2,
//     width: '100%',
//     height: '150%',
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   checkText: {
//     color: Theme.Colors.customWhite,
//     fontSize: 14,
//     fontWeight: 'bold',
//   },
//   radio: {
//     gap: Theme.Gap.sm,
//     justifyContent: 'center',
//   },
// });

export default GenderOption;
