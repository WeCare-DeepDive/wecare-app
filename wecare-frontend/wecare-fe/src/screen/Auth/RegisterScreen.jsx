import React from 'react';
import { View, Text, Pressable, Image, StyleSheet } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Colors, FontFamily, FontSize, Gap, Padding } from '../../styles/theme';
import { useNavigation } from '@react-navigation/native';

export default function RegisterScreen() {
  const navigation = useNavigation();

  const navigateForm = (isPretender) => {
    navigation.navigate('RegisterForm', { isPretender }); // params로 전달
  };

  return (
    <SafeAreaView style={styles.viewBg}>
      <View style={[styles.view, styles.viewBg]}>
        <Text style={styles.text}>회원님의 역할을 선택해 주세요.</Text>
        <View style={styles.iconframeParent}>
          {/* 보호자 페이지로 이동 */}
          <Pressable
            style={styles.iconframe}
            onPress={() => {
              navigateForm(true);
            }}>
            <Image
              style={styles.iconguardian}
              resizeMode='contain'
              source={require('../../../assets/images/ProfileDependent.png')}
            />
          </Pressable>
          <Text style={[styles.text1, styles.textTypo]}>돌봄을 주고 싶어요</Text>
        </View>
        <View style={styles.iconframeParent}>
          {/* 피보호자 페이지로 이동 */}
          <Pressable
            style={styles.iconframe}
            onPress={() => {
              navigateForm(false);
            }}>
            <Image
              style={styles.iconguardian}
              resizeMode='contain'
              source={require('../../../assets/images/ProfileProtector.png')}
            />
          </Pressable>
          <Text style={styles.textTypo}>돌봄을 받고 싶어요</Text>
        </View>
      </View>
    </SafeAreaView>
  );
}
const styles = StyleSheet.create({
  parent: {
    flex: 1,
    backgroundColor: '#fff',
  },
  viewBg: {
    backgroundColor: '#fff',
    flex: 1,
  },
  textTypo: {
    color: Colors.colorBlack,
    textAlign: 'center',
    fontFamily: FontFamily.pretendard,
    fontWeight: '600',
    lineHeight: 32,
    letterSpacing: -0.7,
    fontSize: FontSize.size_20,
  },
  text: {
    width: 372,
    color: Colors.gray900,
    height: 29,
    textAlign: 'center',
    fontFamily: FontFamily.pretendard,
    fontWeight: '500',
    lineHeight: 32,
    letterSpacing: -0.7,
    fontSize: FontSize.size_20,
  },
  iconguardian: {
    width: 140,
    height: 140,
  },
  iconframe: {
    borderRadius: 999,
    backgroundColor: Colors.colorLavender,
    flexDirection: 'row',
    alignItems: 'center',
  },
  text1: {
    alignSelf: 'stretch',
    height: 29,
  },
  iconframeParent: {
    gap: Gap.sm,
    alignSelf: 'stretch',
    alignItems: 'center',
  },
  view: {
    width: '100%',
    justifyContent: 'center',
    paddingHorizontal: Padding.lg,
    paddingVertical: 143,
    gap: 32,
    alignItems: 'center',
  },
});
