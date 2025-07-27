import { View, Text, StyleSheet } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Colors, FontFamily, FontSize, LineHeight } from '../../styles/theme';

const AuthHeader = ({ title = '로그인' }) => {
  return (
    <SafeAreaView style={styles.safeareaview} edges={['top']}>
      <View style={[styles.view, styles.viewFlexBox]}>
        <View style={[styles.body, styles.viewFlexBox]}>
          <Text style={styles.titleText}>{title}</Text>
          {/* <View style={[styles.bodyChild, styles.bodyChildLayout]} /> */}
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeareaview: {
    backgroundColor: Colors.customWhite,
  },
  viewFlexBox: {
    justifyContent: 'center',
    flexDirection: 'row',
    alignItems: 'center',
  },
  bodyChildLayout: {
    width: 37,
    alignSelf: 'stretch',
  },
  iconcontainer: {
    maxHeight: '100%',
  },
  titleText: {
    fontSize: FontSize.size_24,
    lineHeight: LineHeight['34'],
    fontFamily: FontFamily.nanumB,
    color: Colors.purple500,
    textAlign: 'center',
  },
  bodyChild: {
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'stretch',
  },
  view: {
    width: '100%',
    shadowColor: 'rgba(76, 76, 76, 0.25)',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowRadius: 6,
    elevation: 6,
    shadowOpacity: 1,
    height: 56,
    paddingHorizontal: 20,
    paddingVertical: 10,
    backgroundColor: Colors.customWhite,
  },
  body: {
    gap: 0,
  },
});

export default AuthHeader;
