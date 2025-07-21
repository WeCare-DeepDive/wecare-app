import React, { useEffect, useState } from 'react';
import { View, Text,  Button, Alert } from 'react-native';
import PushController from '../../../utils/PushController';
import * as Notifications from 'expo-notifications';


function PushAlertTestRN(props) {

  const scheduleNotification = async() => {
    Alert.alert('1분 뒤 푸시 알림을 띄웁니다.', '확인하고 버튼을 눌러주세요.');  
    console.log('지금시간: ', new Date().toLocaleString());
    await Notifications.scheduleNotificationAsync({
      content: {
        title: '1분 뒤 푸시 알림',
        body: '버튼누르고 1분 지남!',
      },
      trigger: {
        type: {seconds: 60}
      }
    });
  }

  const scheduleNotificationInterval = async() => {
    Alert.alert('5분 간격 3회 반복 푸시 알림을 띄웁니다.', '확인하고 버튼을 눌러주세요.');  
    console.log('지금시간: ', new Date().toLocaleString());
    for(let i = 1; i <= 3; i++){
      await Notifications.scheduleNotificationAsync({
        content: {
          title: '5분 간격 3회 반복 푸시 알림',
          body: `5분간격 3회 반복중 ${i}`,
        },
        trigger: {type: {seconds: 5 * 60 * i}}
      });
      await new Promise(resolve => setTimeout(resolve, 5 * 60 * 1000));
    }
  }

  const scheduleNotificationInterval2 = async() => {
    Alert.alert('40초 간격 5회 반복 푸시 알림을 띄웁니다.', '확인하고 버튼을 눌러주세요.');  
    console.log('지금시간: ', new Date().toLocaleString());
    for(let i = 1; i <= 5; i++){
      await Notifications.scheduleNotificationAsync({
        content: {
          title: '40초 간격 5회 반복 푸시 알림',   
          body: `40초 간격 5회 반복중 ${i}`,
        },
        trigger: {type: {seconds: 40 * i}}
      });
      //await new Promise(resolve => setTimeout(resolve, 40 * 1000));
    }
  }



  return (
    <View>
      <Text>푸시알림 테스트진행</Text>
      <Button title="1분 뒤 푸시 알림 뜨기" onPress={scheduleNotification} />
      <Button title="40초 간격 5회 반복" onPress={scheduleNotificationInterval2} />
      <PushController />
    </View>
  );
}

export default PushAlertTestRN;