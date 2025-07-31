import React from 'react';
import { Text, View, TextInput, StyleSheet } from 'react-native';
import { Controller } from 'react-hook-form';
import { Theme } from '@styles/theme';

const FormInput = ({
  control,
  name,
  label,
  placeholder,
  rules,
  errors,
  secureTextEntry = false,
  keyboardType = 'default',
  fontSize,
  lineHeight,
  ...otherProps
}) => {
  return (
    <View style={styles.inputset}>
      <Text style={[styles.label, {fontSize: fontSize, lineHeight: lineHeight}]}>{label}</Text>
      <Controller
        control={control}
        rules={rules}
        render={({ field: { onChange, onBlur, value } }) => (
          <TextInput
            style={[styles.inputsinputtiltle, errors[name] && styles.inputError, {fontSize: fontSize}]}
            onBlur={onBlur}
            onChangeText={onChange}
            value={value}
            placeholder={placeholder}
            placeholderTextColor={Theme.Colors.gray9}
            secureTextEntry={secureTextEntry}
            keyboardType={keyboardType}
            autoCapitalize='none'
            autoCorrect={false}
            {...otherProps}
          />
        )}
        name={name}
      />
      {errors[name] && <Text style={styles.errorText}>{errors[name].message}</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  inputset: {
    gap: Theme.Gap.xs,
    width: '100%',
  },
  label: {
    color: Theme.Colors.customBlack,
    textAlign: 'left',
    fontFamily: Theme.FontFamily.pretendard,
    fontWeight: '500',
    marginBottom: 10,
  },
  inputsinputtiltle: {
    backgroundColor: Theme.Colors.gray1,
    borderColor: Theme.Colors.gray2,
    borderWidth: 1,
    paddingLeft: Theme.Padding.lg,
    paddingTop: 14,
    paddingBottom: 14,
    borderRadius: 10,
    borderStyle: 'solid',
    fontFamily: Theme.FontFamily.pretendard,
    color: Theme.Colors.customBlack,
  },
  inputError: {
    borderColor: Theme.Colors.error,
    backgroundColor: '#FFF5F5',
  },
  errorText: {
    color: Theme.Colors.error,
    fontSize: Theme.FontSize.size_18,
    marginTop: 4,
  },
});

export default FormInput;
