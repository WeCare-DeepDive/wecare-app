// MyPageScreen.tsx
import React from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity, ScrollView } from 'react-native';
import { Colors, FontFamily, FontSize } from '../../styles/theme';
import IconNext from '../../../assets/Iconsvg/Idea/IconDateTomorrow.svg';


const MyPageScreen = () => {
  return (
    <ScrollView style={styles.container}>

      {/* 프로필 섹션 */}
      <View style={styles.profileContainer}>
        <Image
          source={require('../../../assets/images/ProfileDependent.png')} // 임시 이미지 URL
          style={styles.avatar}
        />
        <TouchableOpacity style={styles.editBtn}>
          <Text style={styles.editText}>편집</Text>
        </TouchableOpacity>
        <Text style={styles.username}>김보듬</Text>
      </View>

      {/* 메뉴 목록 */}
      <View style={styles.menuSection}>
        {['회원 정보 수정', '가족 연결 관리', '1:1 문의', '로그아웃'].map((item, idx) => (
          <TouchableOpacity key={idx} style={styles.menuItem}>
            <Text style={styles.menuText}>{item}</Text>
            <IconNext width={24} height={24}/>
          </TouchableOpacity>
        ))}
        <Text style={styles.quitText}>회원 탈퇴</Text>
      </View>
    </ScrollView>
  );
};

export default MyPageScreen;

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff' },

  profileContainer:
  {
    alignItems: 'center',
    marginVertical: 24,
  },

  avatar: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#eee',
  },

  editBtn: {

    backgroundColor: Colors.gray800,
    borderRadius: 6,
    paddingVertical: 4,
    paddingHorizontal: 4,
    marginTop: 8,
  },
  editText:
  {
    fontFamily: FontFamily.nanumB,
    fontSize: 12,
    color: Colors.customWhite,
  },

  username:
  {
    color: Colors.colorBlack,
    textAlign: 'center',
    fontFamily: FontFamily.pretendard,
    fontWeight: '700',
    fontSize: FontSize.size_24,
    marginTop: 8,
  },

  menuSection:
  {
    paddingHorizontal: 20,
    paddingVertical: 20,
    gap: 16,
  },
  menuItem:
  {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderBottomWidth: 1,
    borderBottomColor: Colors.gray4,
    paddingBottom: 20,
  },

  menuText:
  {
    fontFamily: FontFamily.pretendard,
    fontWeight: '500',
    color: Colors.gray900,
    fontSize: 20,

  },

  quitText:
  {
    fontFamily: FontFamily.pretendard,
    marginTop: 24,
    color: Colors.gray6,
    fontSize: 20,
  },
});
