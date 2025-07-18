import { NavigationContainer } from '@react-navigation/native';
import BottomNavigation from './src/navigation/BottomNavigation';
import * as Font from 'expo-font';
import AppLoading from 'expo-app-loading';
import { useState } from 'react';

const fetchFonts = () => {
  return Font.loadAsync({
    'NanumSquareRoundOTFB': require('./assets/fonts/NanumSquareRoundOTFB.otf'),
    'NanumSquareRoundOTFEB': require('./assets/fonts/NanumSquareRoundOTFEB.otf'),
    'NanumSquareRoundOTFL': require('./assets/fonts/NanumSquareRoundOTFL.otf'),
    'NanumSquareRoundOTFR': require('./assets/fonts/NanumSquareRoundOTFR.otf'),
  });
};

export default function App() {
  const [fontLoaded, setFontLoaded] = useState(false);

  if(!fontLoaded){
    return (
      <AppLoading 
        startAsync={fetchFonts}
        onFinish={() => setFontLoaded(true)}
        onError={console.warn}
      />
    )
  }
  return (
    <NavigationContainer>
      <BottomNavigation />
    </NavigationContainer>
  );
}
