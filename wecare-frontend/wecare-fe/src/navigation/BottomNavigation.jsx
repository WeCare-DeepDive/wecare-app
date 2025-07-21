import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
// Screen import
import RoutineScreen from '../screen/Routine/RoutineScreen';
import ScheduleScreen from '../screen/Scedule/ScheduleScreen';
import ReportScreen from '../screen/Report/ReportScreen';
import MyPageScreen from '../screen/My/MyPageScreen';
import HomeScreen from '../screen/Home/HomeScreen';
import Header from '../components/common/Header';
// SVG import
import TabBarIcon from '../components/icons/TabBarIcon';
// Style
import { StyleSheet, Text } from 'react-native';
import { Colors, FontFamily, FontSize, Gap, LineHeight, Padding } from '../styles/theme';

const Tab = createBottomTabNavigator();

export default function BottomNavigation() {
  return (
    <Tab.Navigator
      initialRouteName='Home'
      screenOptions={({ route }) => ({
        header: () => (
          <Header
            title='위케어'
            onBellPress={() => console.log('Bell pressed')}
            onNotificationPress={() => console.log('Noti pressed')}
          />
        ),
        tabBarIcon: ({ focused, size }) => {
          // console.log('BottomNavigation : ', route.name);
          return (
            <TabBarIcon
              routeName={route.name}
              focused={focused}
              size={size}
              color={focused ? Colors.purple500 : Colors.gray7}
            />
          );
        },
        tabBarLabel: ({ focused }) => {
          const labels = {
            Home: '홈',
            Schedule: '일정',
            Routine: '하루',
            Report: '리포트',
            My: '마이',
          };
          return <Text style={focused ? styles.textFocused : styles.textUnfocused}>{labels[route.name]}</Text>;
        },
      })}>
      <Tab.Screen name='Home' component={HomeScreen} />
      <Tab.Screen name='Schedule' component={ScheduleScreen} />
      <Tab.Screen name='Routine' component={RoutineScreen} />
      <Tab.Screen name='Report' component={ReportScreen} />
      <Tab.Screen name='My' component={MyPageScreen} />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  textFocused: {
    lineHeight: 16,
    color: Colors.purple500,
    fontSize: FontSize.size_16,
    textAlign: 'center',
    fontFamily: FontFamily.nanumEB,
    fontWeight: 900,
  },
  textUnfocused: {
    lineHeight: 16,
    fontSize: FontSize.size_16,
    textAlign: 'center',
    fontFamily: FontFamily.nanumR,
    color: Colors.gray600,
  },
});
