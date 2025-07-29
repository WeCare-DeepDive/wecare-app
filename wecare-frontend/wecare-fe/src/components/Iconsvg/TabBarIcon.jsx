import React from 'react';
import { View } from 'react-native';
// Svg import
import NaviHomeSolidIcon from '../../../assets/Iconsvg/NaviHome/NaviHomeSolid.svg';
import NaviHomeIcon from '../../../assets/Iconsvg/NaviHome/NaviHome.svg';
import NaviDailySolidIcon from '../../../assets/Iconsvg/NaviDaily/NaviDailySolid.svg';
import NaviDailyIcon from '../../../assets/Iconsvg/NaviDaily/NaviDaily.svg';
import NaviCalSolidIcon from '../../../assets/Iconsvg/NaviCal/_NaviCalSolid.svg';
import NaviCalIcon from '../../../assets/Iconsvg/NaviCal/_NaviCal.svg';
import NaviReportSolid from '../../../assets/Iconsvg/NaviReport/NaviReportSolid.svg';
import NaviReportIcon from '../../../assets/Iconsvg/NaviReport/NaviReport.svg';
import NaviMySolid from '../../../assets/Iconsvg/NaviMy/NaviMySolid.svg';
import NaviMyIcon from '../../../assets/Iconsvg/NaviMy/_NaviMy.svg';

const iconComponents = {
  'Home-focused': NaviHomeSolidIcon,
  'Home-unfocused': NaviHomeIcon,
  'Routine-focused': NaviDailySolidIcon,
  'Routine-unfocused': NaviDailyIcon,
  'Schedule-focused': NaviCalSolidIcon,
  'Schedule-unfocused': NaviCalIcon,
  'Report-focused': NaviReportSolid,
  'Report-unfocused': NaviReportIcon,
  'My-focused': NaviMySolid,
  'My-unfocused': NaviMyIcon,
};

const TabBarIcon = ({ routeName, focused, size = 24, color }) => {
  const iconKey = `${routeName}-${focused ? 'focused' : 'unfocused'}`;
  const IconComponent = iconComponents[iconKey];
  if (!IconComponent) {
    console.warn(`❌ Icon not found for key: ${iconKey}`);
    return <View style={{ width: size, height: size }} />;
  }

  return <IconComponent width={size} height={size} color={color} />;
};

export default TabBarIcon;
