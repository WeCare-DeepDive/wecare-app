// screens/Home/HomeScreen.jsx
import React from 'react';
import { View, Text } from 'react-native';
import PushAlertTestRN from './components/PushAlertTestRN';

export default function RoutineScreen() {
  
  return (
    <View testID="routine-screen" 
    className="flex-1 items-center justify-center">
      <Text  
      className="text-xl font-bold mb-20">
        Routine Screen</Text>
    <PushAlertTestRN />
    </View>
  );
}
