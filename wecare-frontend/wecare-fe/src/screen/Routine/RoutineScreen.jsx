// screens/Home/HomeScreen.jsx
import React from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { FontFamily } from '../../styles/theme';
import CustomButton from '../../components/buttons/Button';
import { useNavigation } from '@react-navigation/native';

export default function RoutineScreen() {
  const navigation = useNavigation();

  return (
    <SafeAreaView style={styles.safeareaview}>
      <View style={styles.view}>
        <View style={styles.invitetext}>
          <View style={styles.iconguardianWrapper}>
            <Image
              style={styles.iconguardian}
              resizeMode='cover'
              source={require('@assets/images/IconDependent.png')}
            />
          </View>
          <Text style={[styles.text, styles.textTypo2]}>케어를 함께할 가족을 초대해 보세요!</Text>
          <CustomButton
            title='초대하기'
            size='large'
            variant='filled'
            isActive={true}
            onPress={() => {
              navigation.navigate('InvitationScreen');
            }}
          />
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeareaview: {
    backgroundColor: '#fff',
    flex: 1,
  },
  view: {
    height: 917,
    overflow: 'hidden',
    width: '100%',
    backgroundColor: '#fff',
    flex: 1,
  },
  invitetext: {
    marginTop: -121.5,
    marginLeft: -150,
    top: '50%',
    left: '50%',
    width: 299,
    gap: 18,
    alignItems: 'center',
    position: 'absolute',
  },
  iconguardian: {
    width: 204,
    top: -38,
    height: 204,
    zIndex: 0,
    left: 0,
    position: 'absolute',
    transform: [{ rotate: '22deg' }], // 5도 회전
  },
  iconguardianWrapper: {
    width: 203,
    height: 128,
    flexDirection: 'row',
    alignItems: 'center',
  },
  text: {
    letterSpacing: -0.4,
    fontWeight: '500',
    fontFamily: FontFamily.pretendard,
    color: '#000',
    textAlign: 'center',
    lineHeight: 32,
    fontSize: 20,
    alignSelf: 'stretch',
  },
  textTypo2: {
    textAlign: 'center',
    lineHeight: 32,
    fontSize: 20,
  },
});
