import React from 'react';
import { StyleSheet, View, Text, TouchableOpacity, Platform, Dimensions } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import IconDateYesterday from '../../../assets/Iconsvg/Idea/IconDateYesterday.svg';
import { Theme } from '../../styles/theme';
import { useNavigation } from '@react-navigation/native';

const { width: screenWidth } = Dimensions.get('window');

export default function HeaderRoutine({
  backgroundType = 'fill',
  title,
  saveButton = false,
  backButton = true,
  saveTitle,
  titleColored = false,
}) {
  const navigation = useNavigation();
  return (
    <SafeAreaView
      style={[backgroundType === 'fill' ? styles.fillBackground : styles.transparentBackground]}
      edges={['top']}>
      <View style={styles.shadowWrapper}>
        <View style={styles.header}>
          <View style={styles.headeritem}>
            <View style={[styles.view, styles.viewFlexBox]}>
              <View style={[styles.body, styles.viewFlexBox]}>
                {backButton && (
                  <TouchableOpacity
                    onPress={() => {
                      navigation.goBack();
                    }}>
                    <IconDateYesterday style={styles.icon} />
                  </TouchableOpacity>
                )}
                <View style={styles.viewCenterBox}>
                  <Text style={[styles.title, titleColored ? styles.titleColored : styles.titleTransparent]}>
                    {title}
                  </Text>
                </View>
              </View>
            </View>
          </View>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  headerContainer: {
    backgroundColor: '#fff',
    height: 60,
  },
  fillBackground: {
    backgroundColor: '#fff',
  },
  transparentBackground: {
    backgroundColor: Theme.Colors.purple300,
  },
  viewFlexBox: {
    justifyContent: 'flex-start',
    alignItems: 'center',
    flexDirection: 'row',
    flex: 1,
  },
  viewCenterBox: {
    width: '80%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row',
  },
  icon: {
    marginRight: 12,
  },
  title: {
    fontSize: Theme.FontSize.size_22,
    lineHeight: 32,
    fontFamily: Theme.FontFamily.nanumR,
    fontWeight: '700',
  },
  titleColored: {
    color: Theme.Colors.purple500,
  },
  titleTransparent: {
    color: Theme.Colors.colorBlack,
  },
  headeritem: {
    // width: screenWidth - 40,
    justifyContent: 'space-between',
    flexDirection: 'row',
    alignItems: 'center',
  },
  header: {
    paddingHorizontal: Theme.Padding.padding_20 || 20,
    paddingVertical: Theme.Padding.padding_14 || 14,
    //  backgroundColor: Colors.purple300 || '#d6d8ff',
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    minHeight: 60,
  },
  shadowWrapper: {
    backgroundColor: '#fff',
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 }, // ✅ 아래로만
        shadowOpacity: 0.1,
        shadowRadius: 6,
      },
      android: {
        elevation: 6, // ✅ 그림자가 아래로만 떨어짐
      },
    }),
    zIndex: 10,
  },
});
