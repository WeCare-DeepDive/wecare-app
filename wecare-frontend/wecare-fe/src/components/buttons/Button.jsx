import React from 'react';
import { TouchableOpacity, Text, StyleSheet, Platform } from 'react-native';
import { Theme } from '../../styles/theme';

const CustomButton = ({
  title = '전체 할 일 보기',
  size = 'small', // "small" | "large"
  variant = 'filled', // "filled" | "outlined"
  isActive = true, // true | false
  onPress,
}) => {
  const buttonStyle = [
    styles.baseButton,
    size === 'large' ? styles.largeButton : styles.smallButton,
    variant === 'filled'
      ? isActive
        ? styles.filledButton
        : styles.filledButtonDisabled
      : isActive
      ? styles.outlinedButton
      : styles.outlinedButtonDisabled,
  ];

  const textStyle = [
    styles.baseText,
    variant === 'filled'
      ? isActive
        ? styles.filledText
        : styles.filledTextDisabled
      : isActive
      ? styles.outlinedText
      : styles.outlinedTextDisabled,
  ];

  return (
    <TouchableOpacity style={buttonStyle} onPress={onPress} disabled={!isActive}>
      <Text style={textStyle}>{title}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  baseButton: {
    width: 276,
    paddingHorizontal: Theme.Padding.p_30,
    borderRadius: Theme.BorderRadius['24'] / 2.4, // 10정도
    justifyContent: 'center',
    alignItems: 'center',
  },
  smallButton: {
    // paddingVertical: Theme.Padding.p_8,
    width: '30%',
  },
  largeButton: {
    paddingVertical: Theme.Padding.p_10,
    width: '100%',
  },
  // Filled 버튼 - 활성화
  filledButton: {
    backgroundColor: Theme.Colors.purple500,
  },
  // Filled 버튼 - 비활성화
  filledButtonDisabled: {
    backgroundColor: Theme.Colors.gray6,
    color: Theme.Colors.customWhite,
  },
  // Outlined 버튼 - 활성화
  outlinedButton: {
    backgroundColor: Theme.Colors.customWhite,
    borderWidth: 1,
    borderColor: Theme.Colors.purple500,
  },
  // Outlined 버튼 - 비활성화
  outlinedButtonDisabled: {
    backgroundColor: Theme.Colors.customWhite,
    borderWidth: 1,
    borderColor: Theme.Colors.iconDisable,
  },
  baseText: {
    textAlign: 'center',
    fontSize: Theme.FontSize.size_24,
    fontFamily: Theme.FontFamily.nanumB,
    fontWeight: '700',
    lineHeight: Theme.LineHeight[32],
  },
  // Filled 텍스트 - 활성화
  filledText: {
    color: Theme.Colors.customWhite,
  },
  // Filled 텍스트 - 비활성화
  filledTextDisabled: {
    color: Theme.Colors.customWhite,
  },
  // Outlined 텍스트 - 활성화
  outlinedText: {
    fontSize: Theme.FontSize.size_20,
    color: Theme.Colors.purple500,
  },
  // Outlined 텍스트 - 비활성화
  outlinedTextDisabled: {
    fontSize: Theme.FontSize.size_20,
    color: Theme.Colors.iconDisable,
  },
});

export default CustomButton;
