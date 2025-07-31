// screens/Home/HomeScreen.jsx
import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ImageBackground } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import SOSButton from '../../components/buttons/SOSButton';
import { Theme } from '../../styles/theme';
import Calendar from '../../components/Calendar/Calendar';
import { Colors } from 'react-native/Libraries/NewAppScreen';

// 서비스 카드 컴포넌트
const ServiceCard = ({ title, onPress, style }) => (
  <TouchableOpacity style={[styles.serviceCard, style]} onPress={onPress}>
    <Text style={styles.serviceCardText}>{title}</Text>
  </TouchableOpacity>
);

export default function HomeScreen() {
  const handleAddSchedule = () => {
    console.log('일정 추가하기');
  };

  const handleFamilyRegister = () => {
    console.log('가족 등록하기');
  };

  const handleScheduleManage = () => {
    console.log('일정 관리');
  };

  const handleHealthReport = () => {
    console.log('건강 리포트');
  };

  const handleMedicationInfo = () => {
    console.log('복약 정보');
  };

  const handleUserGuide = () => {
    console.log('위케어 사용법');
  };

  const handleWelfareCheck = () => {
    console.log('복지 정보 확인');
  };

  const handleWelfareApply = () => {
    console.log('복지 지원금 신청');
  };

  return (
    <SafeAreaView style={styles.container} testID='home-screen'>
      <ScrollView showsVerticalScrollIndicator={false}>
        {/* 달력 */}
        <Calendar />

        {/* 오늘의 일정 */}
        <View style={styles.scheduleContainer}>
          <Text style={styles.ScheduleText}>오늘의 일정이 없습니다!</Text>
          <TouchableOpacity onPress={handleAddSchedule}>
            <Text style={styles.addScheduleText}>일정 추가하기</Text>
          </TouchableOpacity>
        </View>

        {/* 위케어 서비스 */}
        <View style={styles.serviceSection}>
          <Text style={styles.sectionTitle}>위케어 서비스</Text>

          { /* 가족 등록 카드 */}
          <TouchableOpacity onPress={handleFamilyRegister} style={styles.largeCard}>
            <ImageBackground
              source={require('@assets/images/HomeImage1.png')}
              style={styles.imageBackground}
              imageStyle={styles.imageStyle}
            >
            <Text style={styles.cardText}>가족 등록하기</Text>
            </ImageBackground>
          </TouchableOpacity>

          <View style={styles.gridView}>
            <TouchableOpacity onPress={handleScheduleManage} style={styles.smallCard}>
              <ImageBackground
                style={styles.imageBackground}
                imageStyle={styles.imageStyle}
                source={require('@assets/images/HomeImage2.png')}
              >
                <Text style={styles.cardText}>일정 관리</Text>
              </ImageBackground>
            </TouchableOpacity>

            <TouchableOpacity onPress={handleHealthReport} style={styles.smallCard}>
              <ImageBackground
                style={styles.imageBackground}
                imageStyle={styles.imageStyle}
                source={require('@assets/images/HomeImage3.png')}
              >
                <Text style={styles.cardText}>건강 리포트{'\n'}확인</Text>
              </ImageBackground>
            </TouchableOpacity>

            <TouchableOpacity onPress={handleMedicationInfo} style={styles.smallCard}>
              <ImageBackground
                style={styles.imageBackground}
                imageStyle={styles.imageStyle}
                source={require('@assets/images/HomeImage4.png')}
              >
                <Text style={styles.cardText}>복약 정보{'\n'}확인</Text>
              </ImageBackground>
            </TouchableOpacity>

            <TouchableOpacity onPress={handleUserGuide} style={styles.smallCard}>
              <ImageBackground
                style={styles.imageBackground}
                imageStyle={styles.imageStyle}
                source={require('@assets/images/HomeImage5.png')}
              >
                <Text style={styles.cardText}>위케어 사용법</Text>
              </ImageBackground>
            </TouchableOpacity>
          </View>
        </View>

        {/* 복지 정보 */}
        <View style={styles.serviceSection}>
          <Text style={styles.sectionTitle}>복지 정보</Text>
          <TouchableOpacity onPress={handleWelfareCheck} style={styles.largeCard}>
            <ImageBackground
              source={require('@assets/images/HomeImage6.png')}
              style={styles.imageBackground}
              imageStyle={styles.imageStyle}
            >
            <Text style={styles.cardText}>내게 맞는 복지 정보 확인</Text>
            </ImageBackground>
          </TouchableOpacity>

          <TouchableOpacity onPress={handleWelfareApply} style={styles.largeCard}>
            <ImageBackground
              source={require('@assets/images/HomeImage7.png')}
              style={styles.imageBackground}
              imageStyle={styles.imageStyle}
            >
            <Text style={styles.cardText}>복지 지원금 신청하기</Text>
            </ImageBackground>
          </TouchableOpacity>

        </View>
      </ScrollView>

      {/* SOS 버튼 */}
      <SOSButton />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  scheduleContainer: {
    position: 'relative',
    top: -20,
    zIndex: -1,
    backgroundColor: '#F5F6F7',
    marginHorizontal: 0,
    paddingHorizontal: 20,
    paddingTop: 50,
    paddingBottom: 30,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    alignSelf: 'stretch',
  },
  ScheduleText: {
    fontSize: 22,
    fontWeight: '600',
    color: Theme.Colors.gray900,
    textAlign: 'center',
  },
  addScheduleText: {
    fontSize: 14,
    color: Theme.Colors.gray800,
    textAlign: 'right',
  },
  serviceSection: {
    paddingHorizontal: 20,
    paddingVertical: 20,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Theme.Colors.customBlack,
    marginBottom: 20,
  },
  largeCard: {
    backgroundColor: '#f5f6f7',
    borderColor: '#DCDDDF',
    borderWidth: 1,
    borderRadius: 10,
    alignItems: 'flex-start',
    marginBottom: 16,
    overflow: 'hidden',
    height: 90,
  },
  smallCard: {
    backgroundColor: '#f5f6f7',
    borderColor: '#DCDDDF',
    borderWidth: 1,
    borderRadius: 10,
    alignItems: 'flex-start',
    marginBottom: 16,
    width: '48%', // 두 개의 카드가 한 줄에 들어갈 수 있도록 설정
    height: 150,
  },
  gridView: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  cardText: {
    position: 'absolute',
    top: 20,
    left: 20,
    fontSize: 20,
    fontWeight: '500',
    lineHeight: 28,
    color: '#000',
    textAlign: 'left',
  },
  imageBackground: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  imageStyle: {
    borderRadius: 10,
    width: '100%',
    height: '100%',
    resizeMode: 'contain',
  },
});
