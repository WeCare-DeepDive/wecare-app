import React, { useState } from 'react';
import { TextInput, StyleSheet, View, Text } from 'react-native';
import { Theme } from '@styles/theme';
import { Controller } from 'react-hook-form';

const DateInputs = ({
  control,
  name,
  label,
  placeholder,
  rules,
  errors,
  fontSize = Theme.FontSize.size_16,
  lineHeight = 22,
  ...otherProps
}) => {
  const [date, setDate] = useState('');

  const handleDateChange = (text) => {
    let formattedText = text;
    if (text.length === 4 || text.length === 7) {
      formattedText += '-';
    }
    setDate(formattedText);

    // 날짜 형식 체크
    const dateRegex = /^(\d{4})-(\d{2})-(\d{2})$/;
    if (dateRegex.test(formattedText)) {
      setValue(name, formattedText); // ✅ 수정
    } else {
      setError(name, { message: '올바른 날짜 형식이 아닙니다.' }); // ✅ 수정
    }
  };

  return (
    <View style={styles.inputset}>
      <Text style={styles.label}>{label}</Text>
      <Controller
        name={name}
        control={control}
        rules={rules}
        render={({ field: { onChange, value }, fieldState: { error } }) => (
          <>
            <TextInput
              style={[styles.inputsinputtiltle, error && styles.inputError]}
              value={value || ''} // ✅ 수정: undefined 방지
              onChangeText={(text) => {
                let formattedText = text;
                if (text.length === 4 || text.length === 7) {
                  formattedText += '-';
                }

                const dateRegex = /^(\d{4})-(\d{2})-(\d{2})$/;
                if (dateRegex.test(formattedText)) {
                  onChange(formattedText);
                } else {
                  onChange(formattedText); // ✅ 포맷은 바꾸지만 validation은 rules에서 처리
                }
              }}
              placeholder={placeholder}
            />
            {error && <Text style={styles.errorText}>{error.message}</Text>}
          </>
        )}
      />
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
    fontSize: Theme.FontSize.size_18,
    lineHeight: 22,
    fontWeight: '500',
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
    fontSize: Theme.FontSize.size_16,
    fontFamily: Theme.FontFamily.pretendard,
    color: Theme.Colors.customBlack,
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

export default DateInputs;
