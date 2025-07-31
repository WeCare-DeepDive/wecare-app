import React, { useState, useEffect } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { KeyboardAvoidingView, Platform, StyleSheet, Text, View } from 'react-native';
import { Theme } from '../../styles/theme';
import { Controller, useForm } from 'react-hook-form';
import FormInput from '../../components/forms/FormInputs';
import { Picker } from '@react-native-picker/picker';
import InputWithButton from '../../components/forms/InputWithButton';
import CustomButton from '../../components/buttons/Button';
import useUserInfo from '../../hooks/useUserInfo';
import * as Clipboard from 'expo-clipboard';
import { useNavigation } from '@react-navigation/native';
import useInviteStore from './store/inviteStore';


// TODO: 초대코드 불러오기 초대코드 무조건 불러와져야함!!!!
//       초대코드 입력받는부분(상대방에게 전달받았을 때) 이부분은 폼으로 넘겨야함
//       

export default function InvitationScreen() {
  // 초대코드 불러오기
  const { inviteCode, fetchInviteCode, isLoading, fetchInviteAccept, isSuccess, inviteCodeError } = useInviteStore();
  // 사용자 정보 불러오기
  const {user, isDependent, loading, error} = useUserInfo({useMock: true});

  if (loading) {
    return <Text>Loading...</Text>;
  }
  
  if (error) {
    useEffect(() => {
      Alert.alert('Error', error.message);
      // navigation.navigate('InvitationScreen');
    }, []);
    return null;
  }
  

  // 네비게이션
  const navigation = useNavigation();

  // 폼 상태 관리
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors, isValid },
  } = useForm({
    defaultValues: {
      invitationCode: '',
      relationshipType: '',
    },
    mode: 'onChange',
  });

 // isDependent에 따른 폰트 크기 결정
  const fontSize = isDependent ? Theme.FontSize.size_24 : Theme.FontSize.size_18;
  const lineHeight = isDependent ? Theme.LineHeight[24] : Theme.LineHeight[18];

  // 화면 로딩 시 초대코드 불러오기
  // useEffect(() => {
  //   if(inviteCode === null) {
  //     fetchInviteCode();
  //   }
  // }, []);

  // 초대코드 복사
  const handleCopy = async() => {
    await Clipboard.setStringAsync(value);
  }


  // 입력값 감시
  const watchedValues = watch();
  const isFormValid = watchedValues.invitationCode && watchedValues.relationshipType && isValid;

  // 저장 버튼 눌렀을 때 처리
  const onSubmit = async (data) => {
    console.log('🔍 저장 버튼 눌렀을 때 처리:', data);
    // const { invitationCode, relationshipType } = data;
    // const requestData = {
    //   invitationCode,
    //   relationshipType,
    // }
    // await fetchInviteAccept(requestData);
    
    // if(isSuccess) {
    //   // 하루 메인으로 다시 이동 => 만약 성공을 했을 시, 성공 했다는 모달을 띄워야함
    //   navigation.navigate('RoutineMain', {showModal: true});
    // } else {
    //   if(LOG_LEVEL === 'debug') {
    //     console.log('🔍 초대 코드 수락 실패');
    //     console.log('🔍 초대 코드 수락 실패 이유:', inviteCodeError);
    //   }
    //   // 실패에 관한 alert 띄우기
    //   Alert.alert('초대 코드 수락 실패', inviteCodeError?.message);
    // }

    
    return navigation.navigate('RoutineMain', {showModal: true});
  }

  return (
    <SafeAreaView style={styles.safeareaview}>
      <KeyboardAvoidingView 
        style={styles.container}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      >
          <View style={styles.view}>
            <View style={styles.codeshareParent}>
              <View style={styles.codeshare}>
                {/* //아래 부분 인풋윗버튼 컴포넌트로 수정해야함 */}
                <InputWithButton 
                  label='초대코드'
                  fontSize={fontSize}
                  lineHeight={lineHeight}
                  value={inviteCode}          
                  // value={'12345'}    // UI test용
                  buttonTitle='공유'
                  isActive={true}
                  onPress={handleCopy}
                />
              
                <FormInput 
                  control={control}
                  name='invitationCode'
                  label='상대방 초대코드를 전달받으셨나요?'
                  placeholder='초대코드 입력'
                  rules={{ required: '초대코드 입력' }}
                  errors={errors}
                  fontSize={fontSize}
                  lineHeight={lineHeight}
                />
                {errors.invitationCode && <Text style={styles.errorText}>{errors.invitationCode.message}</Text>}

                <View style={styles.inputset}>  
                  <Text style={[styles.label, {fontSize: fontSize, lineHeight: lineHeight}]}>상대방과의 관계가 어떻게 되시나요?</Text>
                  <View style={styles.dropdownWrapper}>
                    <Controller
                      control={control}
                      name='relationshipType'
                      render={({field: {onChange, onBlur, value}}) => (
                        <Picker
                          selectedValue={value}
                          onValueChange={(itemValue) => onChange(itemValue)}
                          onBlur={onBlur}
                          style={[
                            styles.dropdown, 
                            Platform.OS === 'android' ? { fontSize: fontSize, lineHeight: lineHeight } : null,
                          ]}
                          mode="dropdown"
                        >
                          <Picker.Item label="부모" value="parent" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                          <Picker.Item label="조부모" value="grandparent" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                          <Picker.Item label="형제/자매" value="sibling" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                          <Picker.Item label="친구" value="friend" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                          <Picker.Item label="친척" value="relative" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                          <Picker.Item label="기타" value="etc" style={[styles.dropdownItem, {fontSize: fontSize, lineHeight: lineHeight}]} />
                        </Picker>
                      )}
                    />
                  
                    {errors.relationshipType && <Text style={styles.errorText}>{errors.relationshipType.message}</Text>}
                  </View>

                </View>
              </View>
            </View>
          </View>
            {/* 저장 버튼 */}
            <View style={styles.buttonContainer}>
              <CustomButton 
                title='연결하기'
                size='large'
                variant='filled'
                isActive={isFormValid && !isLoading}
                onPress={handleSubmit(onSubmit)}
                isLoading={isLoading}
              />
            </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeareaview: {
    backgroundColor: '#fff',
    flex: 1,
  },
  container: {
    flex: 1,
  },
  
 label: {
    color: Theme.Colors.customBlack,
    textAlign: 'left',
    fontFamily: Theme.FontFamily.pretendard,
    fontWeight: '500',
    marginBottom: 10,
  },
  dropdown: {
    width: '100%',
    height: 56,
    borderWidth: 1,
    borderColor: Theme.Colors.gray9,
    borderRadius: 4,
    backgroundColor: Theme.Colors.white,
  },
  dropdownItem: {
    width: '100%'
  },
  view: {
    height: 917,
    overflow: 'hidden',
    width: '100%',
    backgroundColor: '#fff',
    flex: 1,
  },
  codeshareParent:{
    top: Theme.Padding.xxl,
    gap: 36,
    paddingHorizontal: 20,
    left: 0,
  },
  codeshare: {
    gap: 10,
  },
  inputset: {
    width: '100%',
  },
  dropdownWrapper: {
    width: '100%',
    height: 56,
    borderWidth: 1,
    borderColor: Theme.Colors.gray9,
    borderRadius: 4,
    backgroundColor: Theme.Colors.white,
  },
  buttonContainer:{
    // 가장 아래에 위치하도록 함
    paddingHorizontal: 20,
    paddingBottom: Theme.Padding.xl,
    backgroundColor: Theme.Colors.white,
  }
});
