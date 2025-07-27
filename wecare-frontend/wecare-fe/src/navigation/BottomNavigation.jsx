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
import TabBarIcon from '../components/Iconsvg/TabBarIcon';
// Style
import { StyleSheet, Text, Platform } from 'react-native';
import { Colors, FontSize } from '../styles/theme';

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
          return (
            <Text
              style={[
                styles.tabLabel,
                focused ? styles.tabLabelActive : styles.tabLabelInactive,
              ]}>
              {labels[route.name]}
            </Text>
          );
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
  container: {
    flex: 1,
    backgroundColor: Colors.gray1,
  },
  mainContent: {
    flex: 1,
  },
  tabBar: {
    flexDirection: 'row',
    backgroundColor: Colors.customWhite,
    borderTopWidth: 1,
    borderTopColor: Colors.gray5,
    paddingBottom: Platform.OS === 'ios' ? 20 : 8,
    paddingTop: 8,
    ...Platform.select({
      ios: {
        shadowColor: Colors.customBlack,
        shadowOffset: {width: 0, height: -2},
        shadowOpacity: 0.1,
        shadowRadius: 3,
      },
      android: {
        elevation: 8,
      },
    }),
  },
  tabItem: {
    flex: 1,
    alignItems: 'center',
    paddingVertical: 4,
  },
  tabIcon: {
    fontSize: FontSize.size_24,
    marginBottom: 4,
  },
  tabLabel: {
    fontSize: FontSize.size_16,
    textAlign: 'center',
  },
  tabLabelActive: {
    color: Colors.purple500,
    fontWeight: 'bold',
  },
  tabLabelInactive: {
    color: Colors.gray7,
  },
  smallScreenText: {
    fontSize: FontSize.size_16 - 2,
  },
});
