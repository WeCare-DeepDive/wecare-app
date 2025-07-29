import React, { useState } from 'react';
import { ScrollView, Text, View, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useForm, Controller } from 'react-hook-form';
import { Theme } from '../../styles/theme';
import { useRoute } from '@react-navigation/native';
import CustomButton from '../../components/buttons/Button';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';
import FormInput from '../../components/forms/FormInputs';
import { useFormattedBirthDate } from './hook/useFormattedBirthDate';
import DateInputs from '../../components/forms/DateInputs';
import { useAuthStore } from '../../store/authStore';

export default function RegisterFormScreen() {
  const route = useRoute();
  const { isPretender = true } = route.params || {};
  const [selectedGender, setSelectedGender] = useState('');
  const { formattedValue, setFormattedValue } = useFormattedBirthDate();
  const { register, isLoading, error } = useAuthStore();

  const {
    control,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm({
    defaultValues: {
      name: '',
      gender: '',
      birthDate: '',
      username: '',
      password: '',
      role: isPretender ? 'GUARDIAN' : 'DEPENDENT',
    },
    mode: 'onChange',
  });

  const onSubmit = async (data) => {
    try {
      //console.log('Form Data:', data);
      // 생년월일 형식 변환 (YYYY-MM-DD)
      const formattedData = {
        ...data,
        birthDate: formattedValue || data.birthDate,
      };
      
      //console.log('Submitting registration with data:', formattedData);
      
      // 회원가입 실행 (자동 로그인 포함)
      await register(formattedData);
      
      console.log('Registration successful!');
      // Alert.alert(
      //   '회원가입 성공',
      //   '회원가입이 완료되었습니다. 자동으로 로그인되었습니다.',
      //   [{ text: '확인' }]
      // );
      
      // 회원가입 성공 시 자동으로 메인 화면으로 이동 (App.js에서 처리)
    } catch (error) {
      console.error('Registration failed:', error);
      Alert.alert(
        '회원가입 실패',
        error.message || '회원가입에 실패했습니다. 다시 시도해주세요.',
        [{ text: '확인' }]
      );
    }
  };

  // isPretender에 따른 폰트 크기 결정
  const fontSize = isPretender ? Theme.FontSize.size_18 : Theme.FontSize.size_24;
  const lineHeight = isPretender ? Theme.LineHeight[18] : Theme.LineHeight[24];

  return (
    <SafeAreaView style={styles.frameParentLayout}>
      <KeyboardAwareScrollView
        contentContainerStyle={{ flexGrow: 1 }}
        enableOnAndroid={true}
        extraScrollHeight={20} // 선택한 input이 키보드와 겹치지 않도록 여유 공간
        keyboardShouldPersistTaps='handled'>
        <View style={styles.inputsetParent}>
          {/* 성명 */}
          <FormInput
            control={control}
            name='name'
            label='성명'
            placeholder='성명을 입력해 주세요.'
            rules={{ required: '성명을 입력해 주세요.' }}
            errors={errors}
            fontSize={fontSize}
            lineHeight={lineHeight}
          />

          {/* 성별 */}
          <View style={styles.radiogender}>
            <Text style={[styles.text, { fontSize, lineHeight }]}>성별</Text>
            <Controller
              control={control}
              rules={{ required: '성별을 선택해 주세요.' }}
              render={({ field: { onChange, value } }) => (
                <View style={[styles.radioradiogender, styles.radioFlexBox]}>
                  <GenderOption
                    label='남성'
                    value='MALE'
                    selectedValue={value}
                    onSelect={(gender) => {
                      onChange(gender);
                      setSelectedGender(gender);
                    }}
                    fontSize={fontSize}
                    lineHeight={lineHeight}
                  />
                  <GenderOption
                    label='여성'
                    value='FEMALE'
                    selectedValue={value}
                    onSelect={(gender) => {
                      onChange(gender);
                      setSelectedGender(gender);
                    }}
                    fontSize={fontSize}
                    lineHeight={lineHeight}
                  />
                </View>
              )}
              name='gender'
            />
            {errors.gender && <Text style={styles.errorText}>{errors.gender.message}</Text>}
          </View>

          {/* 생년월일 */}
          <DateInputs
            control={control}
            name='birthDate'
            label='생년월일'
            placeholder='YYYY-MM-DD'
            rules={{ required: '생년월일을 입력해 주세요.' }}
            errors={errors}
            fontSize={fontSize}
            lineHeight={lineHeight}
          />

          {/* 아이디 */}
          <FormInput
            control={control}
            name='username'
            label='아이디'
            placeholder='아이디를 입력해 주세요.'
            rules={{ 
              required: '아이디를 입력해 주세요.',
              minLength: {
                value: 3,
                message: '아이디는 3자 이상이어야 합니다.',
              },
              pattern: {
                value: /^[a-zA-Z0-9_]+$/,
                message: '아이디는 영문, 숫자, 언더스코어만 사용 가능합니다.',
              },
            }}
            errors={errors}
            fontSize={fontSize}
            lineHeight={lineHeight}
          />

          {/* 비밀번호 */}
          <FormInput
            control={control}
            name='password'
            label='비밀번호'
            placeholder='비밀번호를 입력해 주세요.'
            rules={{ 
              required: '비밀번호를 입력해 주세요.',
              minLength: {
                value: 8,
                message: '비밀번호는 8자 이상 20자 이하이어야 합니다.',
              },
              pattern: {
                value: /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])/,
                message: '비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.',
              },
            }}
            errors={errors}
            fontSize={fontSize}
            lineHeight={lineHeight}
            secureTextEntry
          />

          {/* 제출 버튼 */}
          <View style={styles.buttonWrapper}>
            <CustomButton
              title={isLoading ? '회원가입 중...' : '회원가입'}
              size='large'
              variant='filled'
              isActive={isValid && !isLoading}
              onPress={handleSubmit(onSubmit)}
            />
          </View>

          {/* 에러 메시지 표시 */}
          {error && (
            <View style={styles.errorContainer}>
              <Text style={styles.errorMessage}>{error}</Text>
            </View>
          )}
        </View>
      </KeyboardAwareScrollView>
    </SafeAreaView>
  );
}

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
  frameParentLayout: {
    flex: 1,
    backgroundColor: '#fff',
  },
  inputsetParent: {
    flex: 1,
    paddingHorizontal: Theme.Padding.lg,
    paddingVertical: 40,
    gap: 40,
    justifyContent: 'flex-start',
    alignItems: 'center',
  },
  radiogender: {
    alignSelf: 'stretch',
    gap: Theme.Gap.xs,
  },
  radioradiogender: {
    alignSelf: 'stretch',
    gap: 40,
  },
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
  text: {
    alignSelf: 'stretch',
    textAlign: 'left',
    fontFamily: Theme.FontFamily.pretendard,
    color: Theme.Colors.customBlack,
  },
  errorText: {
    color: Theme.Colors.error,
    fontSize: Theme.FontSize.size_12,
    marginTop: 4,
  },
  buttonWrapper: {
    width: '100%',
    margin: 10,
  },
  errorContainer: {
    marginTop: 16,
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: '#FEF2F2',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#FECACA',
  },
  errorMessage: {
    color: Theme.Colors.error,
    fontSize: Theme.FontSize.size_14,
    textAlign: 'center',
    fontFamily: Theme.FontFamily.pretendard,
  },
});
