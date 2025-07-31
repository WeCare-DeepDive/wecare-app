import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import RoutineScreen from '../screen/Routine/RoutineScreen';
import InvitationScreen from '../screen/Routine/InvitationScreen';
import HeaderRoutine from '../components/common/HeaderRoutine';
import Header from '../components/common/Header';
import DailyRoutineScreen from '../screen/Routine/DailyRoutineScreen';

const Stack = createStackNavigator();

export default function RoutineStack() {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name='RoutineMain'
        component={RoutineScreen}
        options={{
          header: () => (
            <Header
              title='위케어'
              onBellPress={() => console.log('Bell pressed')}
              onNotificationPress={() => console.log('Noti pressed')}
            />
          ),
        }} // 또는 custom header
      />
      <Stack.Screen
        name='InvitationScreen'
        component={InvitationScreen}
        options={{
          header: () => (
            <HeaderRoutine
              backgroundType='fill'
              title='가족 추가하기'
              saveButton={false}
              backButton={true}
              titleColored={true}
            />
          ),
        }}
      />
      <Stack.Screen
        name='ScheduleScreen'
        component={DailyRoutineScreen}
        options={{
          header: () => (
            <Header
              title='위케어'
              onBellPress={() => console.log('Bell pressed')}
              onNotificationPress={() => console.log('Noti pressed')}
            />
          ),
        }}
      />
    </Stack.Navigator>
  );
}
