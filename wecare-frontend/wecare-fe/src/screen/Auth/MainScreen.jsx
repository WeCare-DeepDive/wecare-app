import React from 'react';
import { View, Text, StyleSheet, Pressable } from 'react-native';
import { Theme } from '../../styles/theme';
import CustomButton from '../../components/buttons/Button';
import { useNavigation } from '@react-navigation/native';
import Favicon from '../../../assets/favicon/FaviconLight.svg';

const MainScreen = () => {
  const navigation = useNavigation();

  return (
    <View style={styles.container}>
      {/* <View style={styles.gradientOverlay} /> */}
      <View style={styles.content}>
        <View style={styles.logocontainerParent}>
          <View style={styles.faviconContainer}>
            <Favicon style={styles.favicon} width={200} height={100} />
          </View>
          <Text style={styles.title}>WECARE</Text>
          <Text style={styles.subtitle}>언제 어디서나 돌봄이 이어질 수 있도록</Text>
        </View>
        <View style={styles.buttonContainer}>
          <CustomButton
            title='회원가입'
            onPress={() => navigation.navigate('Register')}
            size='large'
            variant='outlined'
          />
          {/* <CustomButton title='로그인' onPress={() => navigation.navigate('Login')} size='large' variant='outlined' /> */}
          <View style={[styles.group, styles.groupFlexBox]}>
            <Text style={[styles.text1, styles.textTypo]}>이미 계정이 있나요?</Text>
            <Pressable onPress={() => navigation.navigate('Login')}>
              <Text style={[styles.text2, styles.textTypo]}>로그인</Text>
            </Pressable>
          </View>
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.Colors.purple500, // 기본 배경색
  },
  gradientOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: '#5EA6FF',
    opacity: 0.5, // 그라디언트 효과를 위한 오버레이
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 60,
    zIndex: 1,
  },
  logocontainerParent: {
    alignSelf: 'stretch',
    alignItems: 'center',
    paddingBottom: 60,
  },
  favicon: {
    borderRadius: 17,
    marginBottom: 4,
  },
  title: {
    fontSize: 30,
    lineHeight: Theme.LineHeight['24'],
    fontFamily: Theme.FontFamily.nanumB,
    // fontWeight: '700',
    letterSpacing: -0.7,
    color: Theme.Colors.customWhite,
    textAlign: 'center',
    textShadowColor: 'rgba(0, 0, 0, 0.25)',
    textShadowOffset: {
      width: 0,
      height: 4,
    },
    textShadowRadius: 8,
    marginBottom: 20,
  },
  subtitle: {
    fontFamily: Theme.FontFamily.pretendard,
    fontSize: Theme.FontSize.size_18,
    lineHeight: 29,
    textAlign: 'center',
    color: Theme.Colors.customWhite,
  },
  buttonContainer: {
    paddingHorizontal: 20,
    gap: 12,
    paddingTop: 80,
    alignSelf: 'stretch',
    alignItems: 'center',
  },
  text1: {
    color: Theme.Colors.customWhite,
  },
  text2: {
    textDecorationLine: 'underline',
    fontWeight: '600',
    color: Theme.Colors.customWhite,
  },
  group: {
    gap: 8,
  },
  groupFlexBox: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  textTypo: {
    fontFamily: Theme.FontFamily.pretendard,
    lineHeight: 29,
    letterSpacing: -1,
    fontSize: Theme.FontSize.size_18,
    textAlign: 'center',
  },
});

export default MainScreen;
