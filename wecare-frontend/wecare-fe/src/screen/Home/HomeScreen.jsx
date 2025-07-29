// screens/Home/HomeScreen.jsx
import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import SOSButton from '../../components/buttons/SOSButton';
import { Theme } from '../../styles/theme';
import Calendar from '../../components/Calendar/Calendar';

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
          <Text style={styles.noScheduleText}>오늘의 일정이 없습니다!</Text>
          <TouchableOpacity onPress={handleAddSchedule}>
            <Text style={styles.addScheduleText}>일정 추가하기</Text>
          </TouchableOpacity>
        </View>

        {/* 위케어 서비스 */}
        <View style={styles.serviceSection}>
          <Text style={styles.sectionTitle}>위케어 서비스</Text>

          <ServiceCard title='가족 등록하기' onPress={handleFamilyRegister} style={styles.familyRegisterCard} />

          <View style={styles.serviceGrid}>
            <ServiceCard
              title='일정을&#10;관리해 보세요!'
              onPress={handleScheduleManage}
              style={styles.gridCard}
            />
            <ServiceCard
              title='건강 리포트를&#10;확인해 보세요~'
              onPress={handleHealthReport}
              style={styles.gridCard}
            />
            <ServiceCard title='복약 정보' onPress={handleMedicationInfo} style={styles.gridCard} />
            <ServiceCard title='위케어 사용법' onPress={handleUserGuide} style={styles.gridCard} />
          </View>
        </View>

        {/* 복지 정보 */}
        <View style={styles.serviceSection}>
          <Text style={styles.sectionTitle}>복지 정보</Text>
          <ServiceCard title='나에게 맞는 복지 정보 확인하기' onPress={handleWelfareCheck} />
          <ServiceCard title='복지 지원금 신청하기' onPress={handleWelfareApply} />
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
    backgroundColor: '#eff0ff',
    marginHorizontal: 0,
    paddingHorizontal: 20,
    paddingTop: 48,
    paddingBottom: 30,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  noScheduleText: {
    fontSize: 22,
    fontWeight: '600',
    color: '#60656e',
    textAlign: 'center',
  },
  addScheduleText: {
    fontSize: 14,
    color: '#8e9198',
    textAlign: 'right',
  },
  serviceSection: {
    paddingHorizontal: 20,
    paddingVertical: 20,
    gap: 20,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: '#000',
    marginBottom: 0,
  },
  serviceCard: {
    backgroundColor: '#f5f6f7',
    borderRadius: 10,
    paddingHorizontal: 19,
    paddingVertical: 17,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 16,
  },
  familyRegisterCard: {
    height: 94,
  },
  serviceCardText: {
    fontSize: 20,
    fontWeight: '500',
    color: '#000',
    textAlign: 'center',
  },
  serviceGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  gridCard: {
    width: '47%', // 조금 더 여백을 위해 47%
    height: 120,
    marginBottom: 16,
  },
});
