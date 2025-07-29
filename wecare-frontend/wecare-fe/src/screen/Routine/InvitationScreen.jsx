import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import FormInput from '../../components/forms/FormInputs';

export default function InvitationScreen() {
  return (
    <SafeAreaView style={styles.safeareaview}>
      <View style={styles.view}>
        <View style={styles.codeshareParent}>
          <View style={styles.codeshare}>
            {/* <FormInput 
              control
              name
              label
              placeholder
              rules
              errors
              secureTextEntry = {false}
              keyboardType = 'default'
              
            /> */}
          </View>
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
  textFlexBox: {
    textAlign: 'left',
    color: '#000',
  },
  inputsinputcontentLayout: {
    height: 56,
    flexDirection: 'row',
  },
  btnctaFlexBox: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  btncta1Layout: {
    width: 372,
    borderRadius: 10,
  },
  textTypo1: {
    color: '#8a8d94',
    textAlign: 'left',
    fontFamily: 'Pretendard Variable',
    lineHeight: 29,
    fontSize: 18,
  },
  bodyFlexBox: {
    gap: 0,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
  },
  btnShadowBox: {
    shadowOpacity: 1,
    alignItems: 'center',
    paddingHorizontal: 20,
    left: 0,
    backgroundColor: '#fff',
  },
  textTypo: {
    fontFamily: 'NanumSquareRoundOTF',
    fontWeight: '700',
    lineHeight: 38,
    fontSize: 24,
    textAlign: 'center',
  },
  bottomBarLayout: {
    width: 412,
    position: 'absolute',
  },
  homeShadowBox: {
    height: 17,
    width: 17,
    elevation: 0.34,
    shadowRadius: 0.34,
    shadowOffset: {
      width: 0,
      height: 0,
    },
    shadowColor: 'rgba(255, 255, 255, 0.25)',
    shadowOpacity: 1,
    position: 'absolute',
  },
  rectanglePosition: {
    left: '0%',
    right: '0%',
    height: '100%',
    position: 'absolute',
    width: '100%',
  },
  rectangleLayout: {
    backgroundColor: '#8c8c8c',
    borderRadius: 1,
    bottom: '8.02%',
    top: '8.35%',
    width: '12.28%',
    height: '83.63%',
    position: 'absolute',
  },
  iconLayout: {
    maxHeight: '100%',
    maxWidth: '100%',
    position: 'absolute',
    overflow: 'hidden',
  },
  backgroundPosition: {
    top: 0,
    width: 412,
    left: 0,
    position: 'absolute',
  },
  text: {
    fontFamily: 'Pretendard Variable',
    textAlign: 'left',
    color: '#000',
    lineHeight: 29,
    fontSize: 18,
    alignSelf: 'stretch',
  },
  inputsinputcontent: {
    width: 284,
    paddingLeft: 20,
    paddingTop: 40,
    paddingBottom: 40,
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: '#edeeef',
    backgroundColor: '#f8f8f8',
    height: 56,
    alignItems: 'center',
    borderRadius: 10,
  },
  text2: {
    fontSize: 20,
    lineHeight: 32,
    fontWeight: '600',
    textAlign: 'center',
    color: '#fff',
    fontFamily: 'Pretendard Variable',
  },
  btncta: {
    width: 80,
    backgroundColor: '#d6d7d9',
    paddingHorizontal: 0,
    paddingVertical: 9,
    height: 56,
    flexDirection: 'row',
    borderRadius: 10,
  },
  sharecontent: {
    gap: 8,
    alignItems: 'center',
    flexDirection: 'row',
    alignSelf: 'stretch',
  },
  codeshare: {
    gap: 10,
    alignSelf: 'stretch',
  },
  text4: {
    flex: 1,
  },
  inputsinputcontent1: {
    paddingHorizontal: 10,
    paddingVertical: 8,
    height: 56,
    flexDirection: 'row',
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: '#edeeef',
    backgroundColor: '#f8f8f8',
    alignItems: 'center',
  },
  text6: {
    fontWeight: '500',
    fontFamily: 'Pretendard',
    lineHeight: 29,
    fontSize: 18,
    textAlign: 'left',
    color: '#000',
    flex: 1,
  },
  icondropdown: {
    width: 24,
    height: 24,
    overflow: 'hidden',
  },
  dropdown: {
    borderColor: '#dcdddf',
    paddingVertical: 10,
    height: 56,
    width: 372,
    borderRadius: 10,
    borderWidth: 1,
    borderStyle: 'solid',
    paddingHorizontal: 20,
    backgroundColor: '#fff',
  },
  codeshareParent: {
    top: 98,
    paddingVertical: 40,
    gap: 36,
    paddingHorizontal: 20,
    left: 0,
    position: 'absolute',
  },
  text7: {
    color: '#fff',
    fontFamily: 'NanumSquareRoundOTF',
    fontWeight: '700',
    lineHeight: 38,
    fontSize: 24,
  },
  btncta1: {
    backgroundColor: '#685eff',
    paddingHorizontal: 30,
    paddingVertical: 12,
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row',
    width: 372,
  },
  btn: {
    bottom: 48,
    shadowColor: 'rgba(0, 0, 0, 0.15)',
    shadowOffset: {
      width: 0,
      height: -1.0483460426330566,
    },
    shadowRadius: 5.24,
    elevation: 5.24,
    paddingTop: 17,
    paddingBottom: 20,
    position: 'absolute',
  },
  rectangle: {
    top: '100.25%',
    bottom: '-100.25%',
  },
  rectangle1: {
    right: '77.28%',
    left: '10.44%',
  },
  rectangle2: {
    right: '43.86%',
    left: '43.86%',
  },
  rectangle3: {
    right: '10.44%',
    left: '77.28%',
  },
  recentApps: {
    left: 82,
    top: 33,
    width: 17,
    elevation: 0.34,
    shadowRadius: 0.34,
    shadowOffset: {
      width: 0,
      height: 0,
    },
    shadowColor: 'rgba(255, 255, 255, 0.25)',
  },
  rectangle4: {
    top: '0%',
    bottom: '0%',
  },
  homeChild: {
    height: '91.81%',
    width: '91.81%',
    top: '96.07%',
    right: '3.63%',
    bottom: '-87.89%',
    left: '4.55%',
    borderRadius: 6,
    borderColor: '#8c8c8c',
    borderWidth: 2.1,
    borderStyle: 'solid',
    position: 'absolute',
  },
  home: {
    top: 15,
    left: 197,
  },
  vectorIcon: {
    height: '77.19%',
    width: '38.6%',
    top: '11.53%',
    right: '32.16%',
    bottom: '11.28%',
    left: '29.24%',
  },
  back: {
    left: 312,
    top: 33,
    width: 17,
    elevation: 0.34,
    shadowRadius: 0.34,
    shadowOffset: {
      width: 0,
      height: 0,
    },
    shadowColor: 'rgba(255, 255, 255, 0.25)',
  },
  bottomBar: {
    bottom: 0,
    left: -1,
    height: 48,
    overflow: 'hidden',
  },
  iconcontainer: {
    width: 37,
    alignSelf: 'stretch',
  },
  text8: {
    color: '#685eff',
  },
  body: {
    flex: 1,
  },
  headeralarminfo: {
    top: 42,
    shadowColor: 'rgba(76, 76, 76, 0.25)',
    shadowRadius: 6,
    elevation: 6,
    shadowOpacity: 1,
    alignItems: 'center',
    paddingHorizontal: 20,
    left: 0,
    backgroundColor: '#fff',
    paddingVertical: 10,
    height: 56,
    justifyContent: 'space-between',
    width: 412,
    flexDirection: 'row',
  },
  background: {
    height: 43,
  },
  text9: {
    top: 16,
    left: 24,
    fontSize: 16,
    fontFamily: 'SamsungOne',
    width: 30,
    height: 20,
    position: 'absolute',
  },
  screenshot20200924161348NytIcon: {
    height: '30%',
    width: '14.78%',
    top: '42.67%',
    right: '5.77%',
    bottom: '27.33%',
    left: '79.44%',
  },
  samsungStatus: {
    height: 42,
    overflow: 'hidden',
    backgroundColor: '#fff',
  },
  view: {
    height: 917,
    overflow: 'hidden',
    width: '100%',
    backgroundColor: '#fff',
    flex: 1,
  },
});
