// src/components/forms/GenderOption.jsx
import React from 'react';
import { TouchableOpacity, View, Text, StyleSheet } from 'react-native';
import { Theme } from '@styles/theme';

const GenderOption = ({ label, value, selectedValue, onSelect, fontSize, lineHeight }) => {
  return (
    <TouchableOpacity style={[styles.radio, styles.radioFlexBox]} onPress={() => onSelect(value)}>
      <View style={[styles.buttonsLayout, selectedValue === value ? styles.buttons : styles.buttons1]}>
        {/* 체크표시 추가 */}
        {selectedValue === value && (
          <View style={styles.checkMark}>
            <Text style={styles.checkText}>✓</Text>
          </View>
        )}
      </View>
      <Text style={[styles.textTypo, { fontSize, lineHeight }]}>{label}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  radioFlexBox: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  buttonsLayout: {
    height: 24,
    borderRadius: 631,
    width: 24,
    justifyContent: 'center',
    alignItems: 'center',
  },
  textTypo: {
    textAlign: 'center',
    fontFamily: Theme.FontFamily.pretendard,
  },
  buttons: {
    backgroundColor: Theme.Colors.purple500,
    paddingHorizontal: 4,
    paddingVertical: 6,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 50,
  },
  buttons1: {
    backgroundColor: Theme.Colors.customWhite,
    borderColor: Theme.Colors.gray7,
    borderWidth: 1.3,
    overflow: 'hidden',
    borderStyle: 'solid',
    borderRadius: 50,
  },
  checkMark: {
    position: 'absolute',
    top: 2,
    width: '100%',
    height: '150%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkText: {
    color: Theme.Colors.customWhite,
    fontSize: 14,
    fontWeight: 'bold',
  },
  radio: {
    gap: Theme.Gap.sm,
    justifyContent: 'center',
  },
});

export default GenderOption;
