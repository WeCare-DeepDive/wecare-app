import React from 'react';
import { SafeAreaView, View } from 'react-native';
import Header from '../common/Header';

function Layout({ children, showHeader = true, headerProps = {} }) {
  return (
    <SafeAreaView className='flex-1 bg-white'>
      {showHeader && <Header {...headerProps} />}
      <View className='flex-1' style={{ marginTop: showHeader ? 66 : 0 }}>
        {children}
      </View>
    </SafeAreaView>
  );
}

export default Layout;
