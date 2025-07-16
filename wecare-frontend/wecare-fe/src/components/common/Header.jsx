import React from 'react';
import { Text, View, Image, TouchableOpacity } from 'react-native';
import Pictureframe from '../assets/pictureframe.svg';

const Header = ({ title = '', showBackButton = false, onBackPress, onBellPress, onNotificationPress }) => {
  return (
    <View
      className='absolute top-[54px] left-0 right-0 h-[66px] bg-purple-300 px-5 py-[14px] flex-row items-center'
      style={{
        shadowColor: 'rgba(0, 0, 0, 0.08)',
        shadowOffset: { width: 0, height: 2 },
        shadowRadius: 4,
        elevation: 4,
        shadowOpacity: 1,
      }}>
      <View className='flex-1 flex-row items-center justify-between'>
        {/* 로고 섹션 */}
        <View className='flex-row items-center'>
          <Image
            className='w-8 h-8 rounded-[24px] opacity-[0.77] mr-[6px]'
            resizeMode='cover'
            source={require('../assets/logo.png')}
          />
          <Text className='text-2xl leading-[38px] text-labels-primary font-bold font-nanum'>위케어</Text>
        </View>

        {/* 아이콘 섹션 */}
        <View className='flex-row items-center space-x-5'>
          <TouchableOpacity className='overflow-hidden' onPress={onBellPress}>
            <Pictureframe width={24} height={24} />
          </TouchableOpacity>
          <TouchableOpacity className='overflow-hidden' onPress={onNotificationPress}>
            <Pictureframe width={24} height={24} />
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default Header;
