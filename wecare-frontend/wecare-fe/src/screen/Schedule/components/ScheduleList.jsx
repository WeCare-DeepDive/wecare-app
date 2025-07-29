// ScheduleList.jsx
import React from 'react';
import { View, StyleSheet } from 'react-native';
import ScheduleItem from './ScheduleItem';
import { Gap, Colors} from '@styles/theme';

const ScheduleList = () => {
  const scheduleData = [
    {
      title: '미영과 점심 약속',
      time: '오후 3시',
      icon: 'Iconspoon',
    },
    {
      title: '병원 내원',
      time: '오후 5시',
      icon: 'Iconspoon1',
    },
  ];

  return (
    <View style={styles.routinecards}>
      {scheduleData.map((schedule, index) => (
        <ScheduleItem key={index} title={schedule.title} time={schedule.time} icon={schedule.icon} />
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  routinecards: {
    shadowColor: Colors.colorDarkgray,
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowRadius: 20,
    elevation: 20,
    gap: Gap.gap_22,
    width: 372,
    shadowOpacity: 1,
  },
});

export default ScheduleList;
