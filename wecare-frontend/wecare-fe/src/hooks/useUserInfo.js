// hooks/useUserInfo.js
import { useState, useEffect } from 'react';
import apiProvider from '../providers/apiProvider';
import { getUserInfoMock, getUserInfoMockUnconnected } from '../mocks/getUserInfoMock';

export default function useUserInfo({useMock = false}) {
  const [user, setUser] = useState(null);
  const [isDependent, setIsDependent] = useState(false);
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        let userInfo = null;
        if(useMock) {
          userInfo = await getUserInfoMock(); // fallback
        } else {
          userInfo = await apiProvider.getUserInfo?.(); // 실제 API
          if(!userInfo) {
            userInfo = await getUserInfoMock(); // fallback
          }
        }
        console.log('🔍 userInfo', userInfo);
        setUser(userInfo);
        setIsDependent(userInfo.role === 'DEPENDENT');
      } catch (err) {
        console.error('❌ 유저 정보 가져오기 실패, mock 사용:', err);
        try {
          const mockData = await getUserInfoMock(); // fallback
          console.log('🔍 mockData', mockData);
          setUser(mockData);
          setIsDependent(mockData.role === 'DEPENDENT');
        } catch (mockError) {
          console.error('❌ mockData 가져오기 실패:', mockError);
          setError(mockError);
        }
        // setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, [useMock]);

  return { user, isDependent, loading, error };
}
