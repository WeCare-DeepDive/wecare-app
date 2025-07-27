import { useCallback, useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import BottomNavigation from './src/navigation/BottomNavigation';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import { View, Text, Image } from 'react-native';
import { useAuthStore } from './src/store/authStore';
import AuthNavigatino from './src/navigation/AuthNavigatino';

const fetchFonts = async () => {
  try {
    await Font.loadAsync({
      PretendardVariable: require('./assets/fonts/PretendardVariable.ttf'),
      NanumSquareRoundOTFB: require('./assets/fonts/NanumSquareRoundOTFB.otf'),
      NanumSquareRoundOTFEB: require('./assets/fonts/NanumSquareRoundOTFEB.otf'),
      NanumSquareRoundOTFL: require('./assets/fonts/NanumSquareRoundOTFL.otf'),
      NanumSquareRoundOTFR: require('./assets/fonts/NanumSquareRoundOTFR.otf'),
    });
    console.log('Fonts loaded successfully');
  } catch (error) {
    console.error('Font loading error:', error);
    throw error;
  }
};

// Keep the splash screen visible while we fetch resources
SplashScreen.preventAutoHideAsync();

export default function App() {
  const [appIsReady, setAppIsReady] = useState(false);
  const [error, setError] = useState(null);
  // 로그인 정보
  const { isAuthenticated } = useAuthStore();
  console.log('로그인정보 확인: ', isAuthenticated);

  useEffect(() => {
    async function prepare() {
      try {
        console.log('Starting app preparation...');
        await fetchFonts();
        console.log('App preparation complete');
      } catch (e) {
        console.error('App preparation error:', e);
        setError(e);
      } finally {
        setAppIsReady(true);
      }
    }

    prepare();
  }, []);

  // 앱이 준비되면 스플래시 스크린 숨기기
  useEffect(() => {
    const hideSplashScreen = async () => {
      if (appIsReady) {
        await SplashScreen.hideAsync();
      }
    };

    hideSplashScreen();
  }, [appIsReady]);

  if (!appIsReady) {
    console.log('Rendering splash screen');
    return (
      <View
        style={{
          flex: 1,
          backgroundColor: '#3D1BFF',
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <Image
          source={require('./assets/splash-full.png')}
          style={{ width: '100%', height: '100%', resizeMode: 'cover' }}
        />
      </View>
    );
  }

  // 에러가 있다면 에러 화면 표시
  if (error) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <Text>Error loading app: {error.message}</Text>
      </View>
    );
  }

  return <NavigationContainer>{isAuthenticated ? <BottomNavigation /> : <AuthNavigatino />}</NavigationContainer>;
}
