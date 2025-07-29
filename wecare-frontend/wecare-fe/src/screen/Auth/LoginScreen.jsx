import React, { useState } from 'react';
import { Text, View, StyleSheet, TextInput, TouchableOpacity, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useForm, Controller } from 'react-hook-form';
import { Theme } from '../../styles/theme';
import { useNavigation } from '@react-navigation/native';
import CustomButton from '../../components/buttons/Button';
import { useAuthStore } from '../../store/authStore';

export default function LoginScreen() {
  const navigation = useNavigation();
  const { login, isLoading, error } = useAuthStore();
  
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors, isValid },
  } = useForm({
    mode: 'onChange', // 실시간 검증을 위해 추가
    defaultValues: {
      userId: '',
      password: '',
    },
  });

  // 실시간으로 입력값 감시
  const watchedValues = watch();
  const isFormValid = watchedValues.userId && watchedValues.password && isValid;

  const onSubmit = async (data) => {
    try {
      console.log('Login Data:', data);
      await login(data);
      console.log('Login successful!');
      // 로그인 성공 시 자동으로 메인 화면으로 이동 (App.js에서 처리)
    } catch (error) {
      console.error('Login failed:', error);
      Alert.alert(
        '로그인 실패',
        error.message || '로그인에 실패했습니다. 다시 시도해주세요.',
        [{ text: '확인' }]
      );
    }
  };
  

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <View style={styles.inputsetParent}>
          {/* 아이디 입력 */}
          <View style={styles.inputset}>
            <Text style={styles.label}>아이디</Text>
            <Controller
              control={control}
              rules={{
                required: '아이디를 입력해 주세요.',
                minLength: {
                  value: 3,
                  message: '아이디는 3자 이상이어야 합니다.',
                },
              }}
              render={({ field: { onChange, onBlur, value } }) => (
                <TextInput
                  style={[styles.inputsinputtiltle, errors.userId && styles.inputError]}
                  onBlur={onBlur}
                  onChangeText={onChange}
                  value={value}
                  placeholder='아이디를 입력해 주세요.'
                  placeholderTextColor={Theme.Colors.gray9}
                  autoCapitalize='none'
                  autoCorrect={false}
                />
              )}
              name='userId'
            />
            {errors.userId && <Text style={styles.errorText}>{errors.userId.message}</Text>}
          </View>

          {/* 비밀번호 입력 */}
          <View style={styles.inputset}>
            <Text style={styles.label}>비밀번호</Text>
            <Controller
              control={control}
              rules={{
                required: '비밀번호를 입력해 주세요.',
                minLength: {
                  value: 8,
                  message: '비밀번호는 8자 이상이어야 합니다.',
                },
              }}
              render={({ field: { onChange, onBlur, value } }) => (
                <TextInput
                  style={[styles.inputsinputtiltle, errors.password && styles.inputError]}
                  onBlur={onBlur}
                  onChangeText={onChange}
                  value={value}
                  placeholder='비밀번호를 입력해 주세요.'
                  placeholderTextColor={Theme.Colors.gray9}
                  secureTextEntry
                  autoCapitalize='none'
                  autoCorrect={false}
                />
              )}
              name='password'
            />
            {errors.password && <Text style={styles.errorText}>{errors.password.message}</Text>}
          </View>

          {/* 아이디/비밀번호 찾기 */}
          <View style={styles.findContainer}>
            <TouchableOpacity onPress={() => {}}>
              <Text style={styles.findText}>아이디 찾기</Text>
            </TouchableOpacity>
            <View style={styles.separator} />
            <TouchableOpacity onPress={() => {}}>
              <Text style={styles.findText}>비밀번호 찾기</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* 로그인 버튼 */}
        <View style={styles.buttonContainer}>
          <CustomButton
            title={isLoading ? '로그인 중...' : '로그인'}
            size='large'
            variant='filled'
            isActive={isFormValid && !isLoading} // 폼이 유효하고 로딩 중이 아닐 때만 활성화
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
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Theme.Colors.customWhite,
    flex: 1,
  },
  content: {
    flex: 1,
    paddingHorizontal: Theme.Padding.lg,
    paddingTop: 40,
    justifyContent: 'space-between',
  },
  inputsetParent: {
    gap: 30,
  },
  inputset: {
    gap: Theme.Gap.xs,
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
  findContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 10,
    marginVertical: 20,
  },
  findText: {
    color: Theme.Colors.gray10,
    fontFamily: Theme.FontFamily.pretendard,
    fontSize: Theme.FontSize.size_14,
    lineHeight: 20,
  },
  separator: {
    width: 1,
    height: 12,
    backgroundColor: Theme.Colors.gray5,
  },
  buttonContainer: {
    paddingBottom: 48,
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
