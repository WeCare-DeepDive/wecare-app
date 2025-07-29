import { useCallback, useEffect, useState, useRef } from 'react';
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
  const navigationRef = useRef(null);
  
  // 로그인 정보
  const { isAuthenticated, setTokens, fetchUserInfo, setNavigationRef, forceLogout } = useAuthStore();
  console.log('로그인정보 확인: ', isAuthenticated);

  useEffect(() => {
    async function prepare() {
      try {
        console.log('Starting app preparation...');
        await fetchFonts();
        
        // 저장된 토큰 확인 (메모리 기반이므로 앱 재시작 시에는 없음)
        console.log('App started - no persistent tokens in memory storage');
        
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

  // 네비게이션 참조 설정
  useEffect(() => {
    setNavigationRef(navigationRef.current);
  }, [setNavigationRef]);

  // 강제 로그아웃 이벤트 감지
  useEffect(() => {
    const handleForceLogout = () => {
      console.log('🚨 강제 로그아웃 이벤트 감지');
      forceLogout();
    };

    // React Native에서는 window 객체가 없으므로 다른 방식으로 이벤트 처리
    // apiProvider에서 직접 forceLogout 호출하도록 수정
    return () => {
      // cleanup if needed
    };
  }, [forceLogout]);

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

  return (
    <NavigationContainer ref={navigationRef}>
      {isAuthenticated ? <BottomNavigation /> : <AuthNavigatino />}
    </NavigationContainer>
  );
}
