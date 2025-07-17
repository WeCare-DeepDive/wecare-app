import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import RoutineScreen from '../screen/Routine/RoutineScreen';
import ScheduleScreen from '../screen/Scedule/ScheduleScreen';
import ReportScreen from '../screen/Report/ReportScreen';
import MyPageScreen from '../screen/My/MyPageScreen';
import HomeScreen from '../screen/Home/HomeScreen';

const Tab = createBottomTabNavigator();

export default function BottomNavigation() {
    return (
        <Tab.Navigator initialRouteName='Home' screenOptions={{ headerTitle: '위케어' }}>
            <Tab.Screen name='Home' component={HomeScreen} />
            <Tab.Screen name='Routine' component={RoutineScreen} />
            <Tab.Screen name='Schedule' component={ScheduleScreen} />
            <Tab.Screen name='Report' component={ReportScreen} />
            <Tab.Screen name='My' component={MyPageScreen} />
        </Tab.Navigator>
    )
};  