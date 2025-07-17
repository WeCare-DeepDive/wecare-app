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
import { Colors, FontFamily, FontSize, Gap, Padding } from '../styles/theme';

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
              style={{
                fontSize: 12,
                color: focused ? Colors.purple500 : Colors.gray7,
              }}>
              {labels[route.name]}
            </Text>
          );
        },
      })}>
      <Tab.Screen name='Home' component={HomeScreen} style={[styles.navimenu, styles.textTypo, styles.text13]} />
      <Tab.Screen
        name='Schedule'
        component={ScheduleScreen}
        style={[styles.navimenu, styles.textPosition, styles.text13]}
      />
      <Tab.Screen
        name='Routine'
        component={RoutineScreen}
        style={[styles.navimenu, styles.textPosition, styles.text15]}
      />
      <Tab.Screen name='Report' component={ReportScreen} style={[styles.navimenu, styles.textTypo, styles.text16]} />
      <Tab.Screen name='My' component={MyPageScreen} style={[styles.navimenu, styles.textPosition, styles.text14]} />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  navigation: {
    top: 746,
    shadowColor: Colors.gray2,
    shadowOffset: {
      width: 0,
      height: -1,
    },
    shadowRadius: 5,
    elevation: 5,
    paddingTop: Padding.p_10,
    paddingBottom: Padding.p_20,
    alignItems: 'center',
    flexDirection: 'row',
    shadowOpacity: 1,
    width: 393,
  },
  navigationPosition: {
    width: 393,
    backgroundColor: Colors.customWhite,
    left: 0,
    position: 'absolute',
  },
  navimenu: {
    width: 40,
    height: 16,
  },
  naviicons: {
    paddingHorizontal: Padding.p_8,
    paddingVertical: Padding.p_4,
    gap: Gap.gap_8,
    justifyContent: 'center',
    // alignSelf: "stretch",
    alignItems: 'center',
    flex: 1,
  },
  textTypo: {
    lineHeight: 16,
    color: Colors.purple500,
    fontSize: FontSize.size_16,
    textAlign: 'center',
    top: '0%',
    fontFamily: FontFamily.nanumSquareRoundOTF,
    position: 'absolute',
  },
  textPosition: {
    left: '13.33%',
    lineHeight: 16,
    fontSize: FontSize.size_16,
    textAlign: 'center',
    top: '0%',
    fontFamily: FontFamily.nanumSquareRoundOTF,
    position: 'absolute',
  },
  text13: {
    left: '33.33%',
    color: Colors.purple500,
  },
  text14: {
    color: Colors.purple500,
  },
  text15: {
    fontWeight: '800',
    color: Colors.purple500,
  },
  text16: {
    left: '-4.17%',
    color: Colors.purple500,
  },
});
