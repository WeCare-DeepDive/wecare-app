// screens/Home/HomeScreen.jsx
import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Theme } from '../../styles/theme';
import { useNavigation, useRoute } from '@react-navigation/native';
import NonInviteFamilyScreen from './components/NonInviteFamilyScreen';
import RoutineModal from '../../components/modal/RoutineModal';
import useUserInfo from '../../hooks/useUserInfo';  

export default function RoutineScreen() {
  // 네비게이션 참조 
  const navigation = useNavigation();
  const route = useRoute();
  const [modalData, setModalData] = useState(null);
  const [overlayOpacity, setOverlayOpacity] = useState(0);
  
  // 모든 훅을 최상위 레벨에서 호출
  const {user, isDependent, loading, error} = useUserInfo({useMock: true});

  // 에러 시 Alert + 화면 이동
  useEffect(() => {
    if (error) {
      Alert.alert('Error', error.message);
      navigation.navigate('InvitationScreen');
    }
  }, [error, navigation]);
  
  // 연결이 되어서 넘어온 경우 => 모달 띄우기
  useEffect(() => {
    if (route.params?.showModal && user && (user.guardians || user.dependents)) {
      // 보호자인지 여부에 따라 모달 메시지 생성
      const isGuardian = !isDependent;
      console.log('🔍 user', user);
      console.log('🔍 isGuardian', isGuardian);
      
      const targetName = isGuardian
        ? user.dependents[0]?.name
        : user.guardians[0]?.name;
      console.log('🔍 targetName', targetName);
      
      const data = {
        name: targetName,
        title: '님과 연결 되었어요!',
        description: `추천 할 일을 ${targetName} 님의 하루에 추가할까요?`,
        cancelButtonText: '안 할래요',
        confirmButtonText: isGuardian ? '추가할래요' : '일정으로',
        isVisible: true,
        onCancel: () => {
          console.log('모달 취소');
          setModalData(null);
        },
        onConfirm: () => {
          console.log('모달 확인');
          setModalData(null);
          if (!isGuardian) {
            navigation.navigate('ScheduleScreen');
          }
        },
      };
      setOverlayOpacity(1);
      setModalData(data);
      // 모달은 한 번만 띄우도록 초기화
      navigation.setParams({ showModal: false });
    }
  }, [route.params?.showModal, user, isDependent, navigation]);

  // 테스트용: user 데이터가 로드되면 자동으로 모달 띄우기
  // useEffect(() => {
  //   if (user && !loading && !modalData) {
  //     console.log('🔍 테스트: user 데이터 로드됨', user);
  //     const isGuardian = !isDependent;
      
  //     if (isGuardian && user.dependents && user.dependents.length > 0) {
  //       const targetName = user.dependents[0]?.name;
  //       console.log('🔍 테스트: targetName', targetName);
        
  //       const data = {
  //         name: targetName,
  //         title: '님과 연결 되었어요!',
  //         description: `추천 할 일을 ${targetName} 님의 하루에 추가할까요?`,
  //         cancelButtonText: '안 할래요',
  //         confirmButtonText: '추가할래요',
  //         isVisible: true,
  //         onCancel: () => {
  //           console.log('모달 취소');
  //           setModalData(null);
  //           setOverlayOpacity(0);
  //         },
  //         onConfirm: () => {
  //           console.log('모달 확인');
  //           setModalData(null);
  //           setOverlayOpacity(0);
  //         },
  //       };
  //       setOverlayOpacity(1);
  //       setModalData(data);
  //     }
  //   }
  // }, [user, loading, modalData, isDependent]);
  
  // 모달 취소 핸들러
  const handleCancel = () => {
    modalData?.onCancel?.();
  }
  // 모달 확인 핸들러
  const handleConfirm = () => {
    modalData?.onConfirm?.();
  }

  // 로딩 중이거나 에러가 있는 경우
  if (loading || error) {
    return <Text>Loading...</Text>;
  }

  // 연결된 가족 정보가 없는 경우 => 초대 화면으로 이동
  if(!user) {
    return <NonInviteFamilyScreen />
  }

  // 연결된 가족 정보가 있는 경우 => 메인 화면으로 이동
  return (
    <SafeAreaView style={[styles.safeareaview, {opacity: 1 - overlayOpacity}]}>
      <View style={[styles.view, {opacity: 1 - overlayOpacity}]}>
        <View style={styles.invitetext}>
          <Text>루틴 화면</Text>  
          {/* TODO: 모달 추가
                  : 화면에 모달이 잘 뜨는지 확인
                  : 네임카드 추가
                  : 할 일이 없는 경우 화면 
                  : 할 일이 잇는경우 할 일 보이게(시간순나열)
                  : 할 일 추가 버튼
                  : 할 일 추가하는 페이지 => 매우 중요!!!
                  : 전체 할 일 보기 => 전체 할일이 나열 => 이건 후순위
          */}
          {/* ✅ 조건부 모달 렌더링 */}
          {modalData && (
            <RoutineModal
              isImageVisible={true}
              title={modalData.title}
              name={modalData.name}
              description={modalData.description}
              cancelButtonText={modalData.cancelButtonText}
              confirmButtonText={modalData.confirmButtonText}
              onCancel={handleCancel}
              onConfirm={handleConfirm}
              isVisible={modalData.isVisible}
            />
          )}
          
        </View>
      </View>
    </SafeAreaView>
  );
  
}
const styles = StyleSheet.create({
  safeareaview: {
      backgroundColor: '#fff',
      flex: 1,
  },
  view: {
      minHeight: 917,
      overflow: 'hidden',
      width: '100%',
      backgroundColor: '#fff',
      flex: 1,
  },
  invitetext: {
      alignItems: 'center',
  },
});
