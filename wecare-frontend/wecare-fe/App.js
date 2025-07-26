import { useCallback, useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import BottomNavigation from './src/navigation/BottomNavigation';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import { View, Text } from 'react-native';

const fetchFonts = async () => {
  try {
    await Font.loadAsync({
      'PretendardVariable': require('./assets/fonts/PretendardVariable.ttf'),
      'NanumSquareRoundOTFB': require('./assets/fonts/NanumSquareRoundOTFB.otf'),
      'NanumSquareRoundOTFEB': require('./assets/fonts/NanumSquareRoundOTFEB.otf'),
      'NanumSquareRoundOTFL': require('./assets/fonts/NanumSquareRoundOTFL.otf'),
      'NanumSquareRoundOTFR': require('./assets/fonts/NanumSquareRoundOTFR.otf'),
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
    return null;
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
    <NavigationContainer>
      <BottomNavigation />
    </NavigationContainer>
  );
}

// import { useCallback, useEffect, useState } from 'react';
// import { NavigationContainer } from '@react-navigation/native';
// import BottomNavigation from './src/navigation/BottomNavigation';
// import * as Font from 'expo-font';
// import * as SplashScreen from 'expo-splash-screen';

// const fetchFonts = () => {
//   return Font.loadAsync({
//     'PretendardVariable': require('./assets/fonts/PretendardVariable.ttf'),
//     'NanumSquareRoundOTFB': require('./assets/fonts/NanumSquareRoundOTFB.otf'),
//     'NanumSquareRoundOTFEB': require('./assets/fonts/NanumSquareRoundOTFEB.otf'),
//     'NanumSquareRoundOTFL': require('./assets/fonts/NanumSquareRoundOTFL.otf'),
//     'NanumSquareRoundOTFR': require('./assets/fonts/NanumSquareRoundOTFR.otf'),
//   });
// };

// // Keep the splash screen visible while we fetch resources
// SplashScreen.preventAutoHideAsync();

// export default function App() {
//   const [appIsReady, setAppIsReady] = useState(false);

//   useEffect(() => {
//     async function prepare() {
//       try {
//         // Pre-load fonts, make any API calls you need to do here
//         await fetchFonts();
//         // Artificially delay for two seconds to simulate a slow loading
//         // experience. Remove this if you copy and paste the code!
//         await new Promise(resolve => setTimeout(resolve, 2000));
//       } catch (e) {
//         console.warn(e);
//       } finally {
//         // Tell the application to render
//         setAppIsReady(true);
//       }
//     }

//     prepare();
//   }, []);

//   const onLayoutRootView = useCallback(() => {
//     if (appIsReady) {
//       SplashScreen.hide();
//     }
//   }, [appIsReady]);

//   if (!appIsReady) {
//     return null;
//   }

//   return (
//     <NavigationContainer onLayout={onLayoutRootView}>
//       <BottomNavigation />
//     </NavigationContainer>
//   );
// }
