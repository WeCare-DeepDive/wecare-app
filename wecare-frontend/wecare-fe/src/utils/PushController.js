import React, { useEffect } from 'react';
// import PushNotification from 'react-native-push-notification';
import * as Notifications from 'expo-notifications';


function PushController() {
  useEffect(() => {
    // 알림 권한 요청
    (async () => {
      const { status } = await Notifications.requestPermissionsAsync();
      if (status !== 'granted') {
        alert('알림 권한이 필요합니다!');
      }
    })();
    // 알림 설정 옵션
    Notifications.setNotificationHandler({
      handleNotification: async () => ({
        shouldShowBanner: true, // 알림 배너 표시
        shouldShowList: true,   // 알림 센터(목록)에 표시
        shouldPlaySound: true,
        shouldSetBadge: true,
      }),
    });
  }, []);

  return null;
}

export default PushController;

