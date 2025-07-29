import React from 'react';
import { View, TouchableOpacity, Text, StyleSheet } from 'react-native';
import { Theme } from '../../styles/theme';

const FindLinks = ({ onFindId, onFindPassword }) => {
  return (
    <View style={styles.findContainer}>
      <TouchableOpacity onPress={onFindId}>
        <Text style={styles.findText}>아이디 찾기</Text>
      </TouchableOpacity>
      <View style={styles.separator} />
      <TouchableOpacity onPress={onFindPassword}>
        <Text style={styles.findText}>비밀번호 찾기</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  findContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 10,
    marginVertical: 20,
  },
  findText: {
    color: Theme.Colors.gray10,
    fontFamily: Theme.FontFamily.pretendard,
    fontSize: Theme.FontSize.size_14,
    lineHeight: 20,
  },
  separator: {
    width: 1,
    height: 12,
    backgroundColor: Theme.Colors.gray5,
  },
});

export default FindLinks;
