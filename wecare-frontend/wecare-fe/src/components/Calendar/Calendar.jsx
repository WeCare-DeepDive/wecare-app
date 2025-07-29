import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { FontSize, Colors } from '../../styles/theme';

// 달력 컴포넌트 분리
const Calendar = () => {
  const days = [
    { day: '일', date: 7 },
    { day: '월', date: 8 },
    { day: '화', date: 9 },
    { day: '수', date: 10, isToday: true },
    { day: '목', date: 11 },
    { day: '금', date: 12 },
    { day: '토', date: 13 },
  ];

  return (
    <View style={styles.calendar}>
      <View style={styles.calendarHeader}>
        <TouchableOpacity>
          <Ionicons name='chevron-back' size={24} color='#000' />
        </TouchableOpacity>
        <Text style={styles.weekText}>7월 첫째 주</Text>
        <TouchableOpacity>
          <Ionicons name='chevron-forward' size={24} color='#000' />
        </TouchableOpacity>
      </View>
      <View style={styles.daysContainer}>
        {days.map((item, index) => (
          <View key={index} style={styles.dayItem}>
            <Text style={[styles.dayText, item.isToday && styles.todayText]}>{item.day}</Text>
            <View style={[styles.dateContainer, item.isToday && styles.todayContainer]}>
              <Text style={[styles.dateText, item.isToday && styles.todayDateText]}>{item.date}</Text>
            </View>
          </View>
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  calendar: {
    backgroundColor: '#fff',
    paddingHorizontal: 20,
    paddingVertical: 20,
    marginHorizontal: 0,
    borderBottomLeftRadius: 20,
    borderBottomRightRadius: 20,
    shadowColor: 'rgba(137, 137, 137, 0.11)',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 1,
    shadowRadius: 10,
    elevation: 10,
  },
  calendarHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginBottom: 20,
  },
  weekText: {
    fontSize: 24,
    fontWeight: '700',
    color: '#000',
  },
  daysContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 0,
  },
  dayItem: {
    alignItems: 'center',
    gap: 9,
  },
  dayText: {
    fontSize: FontSize.size_18,
    fontWeight: '500',
    color: Colors.gray10,
    textAlign: 'center',
  },
  todayText: {
    color: '#352bff',
    fontWeight: '700',
  },
  dateContainer: {
    width: 37,
    height: 37,
    borderRadius: 999,
    justifyContent: 'center',
    alignItems: 'center',
  },
  todayContainer: {
    backgroundColor: '#685eff',
  },
  dateText: {
    fontSize: 18,
    fontWeight: '500',
    color: '#60656e',
    textAlign: 'center',
  },
  todayDateText: {
    color: '#fff',
  },
});

export default Calendar;
