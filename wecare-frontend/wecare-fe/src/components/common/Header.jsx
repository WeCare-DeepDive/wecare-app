import React from 'react';
import { Text, View, Image, StyleSheet } from 'react-native';
// import Pictureframe from '../../../assets/pictureframe.svg';

const Header = ({ title = '', showBackButton = false, onBackPress, onBellPress, onNotificationPress }) => {
  return (
    <View style={styles.header}>
      <View style={styles.headeritem}>
        <View style={styles.logocontainer}>
          <Image style={styles.logoIcon} 
          resizeMode="contain" 
          source={require('../../../assets/pictureframe.png')}
          />
          <Text style={[styles.text2, styles.textTypo1]}>{title}</Text>
      </View>
      <View style={styles.container}>
          {/* <Pictureframe style={styles.iconbell} width={24} height={24} />
          <Pictureframe style={styles.iconbell} width={24} height={24} /> */}
      </View>
    </View>
  </View>
  );
};

const styles = StyleSheet.create({
  textTypo1: {
    fontWeight: "700",
    fontFamily: "NanumSquareRoundOTF"
  },
  logoIcon: {
    width: 32,
    // borderRadius: 24,
    height: 32,
    // opacity: 0.77
  },
  text2: {
    fontSize: 24,
    lineHeight: 38,
    textAlign: "left",
    color: "#000"
  },
  logocontainer: {
    gap: 6,
    flexDirection: "row",
    alignItems: "center"
  },
  iconbell: {
    overflow: "hidden"
  },
  container: {
    gap: 20,
    flexDirection: "row",
    alignItems: "center"
  },
  headeritem: {
    width: 353,
    justifyContent: "space-between",
    gap: 0,
    flexDirection: "row",
    alignItems: "center"
  },
  header: {
    top: 54,
    shadowColor: "rgba(0, 0, 0, 0.08)",
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowRadius: 4,
    elevation: 4,
    height: 66,
    paddingHorizontal: 20,
    paddingVertical: 14,
    backgroundColor: "#d6d8ff",
    left: 0,
    shadowOpacity: 1,
    flexDirection: "row",
    alignItems: "center",
    position: "absolute"
  }
});

export default Header;
