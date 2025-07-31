import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

const SOSButton = () => {
  const handleSOSPress = () => {
    // SOS 버튼 기능 구현
    console.log('SOS 버튼 pressed');
  };

  return (
    <TouchableOpacity style={styles.sosButton} onPress={handleSOSPress}>
      <Text style={styles.sosText}>SOS</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  sosButton: {
    position: 'absolute',
    right: 10,
    bottom: 10,
    width: 70,
    height: 70,
    backgroundColor: '#685eff',
    borderRadius: 35,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: 'rgba(0, 0, 0, 0.25)',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1,
    shadowRadius: 4,
    elevation: 4,
  },
  sosText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default SOSButton; // () 제거 - 함수 자체를 export
