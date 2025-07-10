import React, { useEffect } from 'react';
import { View, Text,  Button, Alert } from 'react-native';
import PushNotification from 'react-native-push-notification';
import PushController from '../../../utils/PushController';

function PushAlertTestRN(props) {

  const scheduleNotification = () => {
    Alert.alert('1분 뒤 푸시 알림을 띄웁니다.', '확인하고 버튼을 눌러주세요.');  
    PushNotification.localNotificationSchedule({
      message: "버튼누르고 1분 지남!", // (required)ㅇ
      date: new Date(Date.now() + 60 * 1000) // in 60 secs
    });
  };

  // 5분 간격 3회 반복
  let count = 0;
  const scheduleNotificationInterval = async () => {
    Alert.alert('5분 간격 3회 반복 푸시 알림을 띄웁니다.', '확인하고 버튼을 눌러주세요.');  
    PushNotification.localNotificationSchedule({
      message: `5분간격 3회 반복중 ${count}`, // (required)
      date: new Date(Date.now() + 5 * 60 * 1000), // in 5 mins
    }); 
    count++;
    if (count < 3) {
      await new Promise(resolve => setTimeout(resolve, 5 * 60 * 1000));
      scheduleNotificationInterval();
    }
  };

  return (
    <View>
      <Text>푸시알림 테스트진행</Text>
      <Button title="1분 뒤 푸시 알림 뜨기" onPress={scheduleNotification} />
      <Button title="5분 간격 3회 반복" onPress={scheduleNotificationInterval} />
      <PushController />
    </View>
  );
}

export default PushAlertTestRN;