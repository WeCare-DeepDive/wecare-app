import React, { useEffect } from 'react';
import PushNotification from 'react-native-push-notification';

function PushController() {
  useEffect(() => {
    PushNotification.configure({
      onNotification: function(notification) {
        console.log('NOTIFICATION:', notification);
      }
    });
  }, []);

  return null;
}

export default PushController;

