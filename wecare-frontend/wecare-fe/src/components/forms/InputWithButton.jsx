import React from 'react';
import { View, Text, TextInput, StyleSheet } from 'react-native';
import CustomButton from "../buttons/Button";
import { Theme } from "../../styles/theme";

const InputWithButton = ({
    label,
    value,
    buttonTitle,
    fontSize,
    lineHeight,
    isActive = true,
    keyboardType = 'default',
    secureTextEntry = false,
    onPress,
    ...otherProps
  }) => {

  return (
    <View style={styles.inputset}>
      <Text style={[styles.label, {fontSize: fontSize, lineHeight: lineHeight}]}>{label}</Text>
      <View style={styles.inputContainer}>
             <TextInput
                style={[styles.inputsinputtiltle, {fontSize: fontSize, lineHeight: lineHeight}]}
                value={value}
                secureTextEntry={secureTextEntry}
                keyboardType={keyboardType}
                autoCapitalize='none'
                editable={false}
                {...otherProps}
            />
            
        <CustomButton
            title={buttonTitle}
            onPress={onPress}
            size='xsmall'
            variant='filled'
            isActive={isActive}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
    inputset: {
      gap: Theme.Gap.xs,
      width: '100%',
    },
    inputContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      gap: 10,
    },
    label: {
      color: Theme.Colors.labelsPrimary,
      textAlign: 'left',
      fontFamily: Theme.FontFamily.pretendard,
      // fontSize: Theme.FontSize.size_18,
      // lineHeight: 22,
      fontWeight: '500',
    },
    inputsinputtiltle: {
      backgroundColor: Theme.Colors.gray1,
      borderColor: Theme.Colors.gray2,
      borderWidth: 1,
      // paddingLeft: Theme.Padding.lg,
      paddingTop: 14,
      paddingBottom: 14,
      borderRadius: 10,
      borderStyle: 'solid',
      // fontSize: Theme.FontSize.size_16,
      fontFamily: Theme.FontFamily.pretendard,
      color: Theme.Colors.customBlack,
      flex: 1,
    },
    inputError: {
      borderColor: Theme.Colors.error,
      backgroundColor: '#FFF5F5',
    },
    errorText: {
      color: Theme.Colors.error,
      fontSize: Theme.FontSize.size_12,
      marginTop: 4,
    },
  });
    
export default InputWithButton;