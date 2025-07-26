import React from 'react';
import { Text, View, Image, StyleSheet, Platform, Dimensions } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
// Svg import
import FaviconDark from '../../../assets/favicon/FaviconDark.svg';
import FaviconLight from '../../../assets/favicon/FaviconLight.svg';
import IconBell from '../../../assets/Iconsvg/IconBell.svg';
import IconPlus from '../../../assets/Iconsvg/IconPlus.svg';
// Style
import { Colors, FontFamily, FontSize, Gap, LineHeight, Padding } from '../../styles/theme';

const { width: screenWidth } = Dimensions.get('window');

const Header = ({ title = '', onBellPress, onNotificationPress }) => {
  return (
    <SafeAreaView style={styles.safeArea} edges={['top']}>
      <View style={styles.header}>
        <View style={styles.headeritem}>
          <View style={styles.logocontainer}>
            <Image 
              style={styles.logoIcon} 
              resizeMode="contain" 
              source={require('../../../assets/Icon.png')} 
            />
            <Text style={[styles.text2, styles.textTypo1]}>{title}</Text>
          </View>
          <View style={styles.container}>
            <IconBell onPress={onBellPress} width={24} height={24} />
            <IconPlus onPress={onNotificationPress} width={24} height={24} />
          </View>
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    backgroundColor: Colors.purple300 ,
  },
  textTypo1: {
    fontWeight: "700",
    fontFamily: FontFamily.pretendard
  },
  logoIcon: {
    width: 32,
    height: 32,
  },
  logocontainer: {
    gap: Gap.gap_6 || 6,
    flexDirection: "row",
    alignItems: "center"
  },
  text2: {
    fontSize: FontSize.size_32 || 24,
    lineHeight: LineHeight.lineHeight_38 || 38,
    textAlign: "left",
    color: Colors.purple500,
  },
  container: {
    gap: Gap.gap_20 || 20,
    flexDirection: "row",
    alignItems: "center"
  },
  headeritem: {
    width: screenWidth - 40,
    justifyContent: "space-between",
    flexDirection: "row",
    alignItems: "center",
  },
  header: {
    paddingHorizontal: Padding.padding_20 || 20,
    paddingVertical: Padding.padding_14 || 14,
    backgroundColor: Colors.purple300 || "#d6d8ff",
    flexDirection: "row",
    alignItems: "center",
    width: '100%',
    minHeight: 60,
    ...Platform.select({
      ios: {
        shadowColor: Colors.customBlack || "rgba(0, 0, 0, 0.08)",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
      },
      android: {
        elevation: 4,
      },
    }),
  }
});

export default Header;