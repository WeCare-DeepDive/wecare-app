// screens/Home/HomeScreen.jsx
import React from 'react';
import { View, Text } from 'react-native';
import TimePicker from '../../components/dateTimePicker/TimePicker';

export default function HomeScreen() {
  return (
    <View testID="home-screen">
      <Text>Home Screen</Text>
      <TimePicker />
    </View>
  );
}
