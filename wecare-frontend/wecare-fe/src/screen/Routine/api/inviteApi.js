import axios from "axios";
import { API_BASE_URL, API_TIMEOUT, LOG_LEVEL } from '../../../config/environment';
import { useAuthStore } from "../../../store/authStore";

// Zustand store의 getState를 직접 가져오기
const authStore = useAuthStore;


const API_URL = API_BASE_URL;
const API_INVITE = '/api/invitations'

 // 공통 헤더 설정
 const getHeaders = () => {
    return {
      'Content-Type': 'application/json',
    };
  }

  // 인증 헤더 설정 (토큰이 있을 때)
  const getAuthHeaders = (token) => {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    };
  }

// 초대코드 생성
const getInviteCode = async () => {
    const { accessToken } = authStore.getState();
    console.log('🔍 초대코드 불러오기 시작: inviteApi/ 토큰 살아있는지 체크: ', accessToken);
    try{
        // 초대코드 생성
        console.log('🔍 초대코드 생성 시작:');
        if(accessToken) {
            const response = (await axios.post(`${API_URL}/api/invitations/generate`, {
                headers: getAuthHeaders(accessToken)
            }));
            if(LOG_LEVEL === 'debug') {
                console.log('🔍 초대코드 생성 성공:', response.data);
            }
            return response;
        } else {
            throw new Error('토큰이 없습니다.');
        }
    } catch(error) {
        // 에러 json타입으로 처리
        if(LOG_LEVEL === 'debug') {
            console.log('🌏 send request', `${API_URL}/api/invitations/generate`);
            console.log('📌 get response: ', error.response);
            console.log('🔍 에러 메시지:', error.response.data);
        }
        // 유효하지 않은 Access Token
        if(error.response.status === 401) {
            // 토큰 갱신 후 다시 진행
            const newAccessToken = await refreshAccessToken();
            if(newAccessToken) {
                return getInviteCode();
            }
            throw new Error('토큰 갱신 실패');
        }
        //JSON 타입으로 처리
        const jsonError = JSON.parse(error.response.data);
        throw jsonError.message;
    }
}

// 초대 코드 수락 요청
const userInviteAccept = async (data) => {
    const { accessToken } = authStore.getState();
    console.log('🔍 초대 코드 수락 시작: inviteApi/ 토큰 살아있는지 체크: ', accessToken);
    try {
        const response = (await axios.post(`${API_URL}/api/invitations/accept`, data, {
            headers: getAuthHeaders(accessToken)
        }));
        if(LOG_LEVEL === 'debug') {
            console.log('🔍 초대 코드 수락 성공:', response.data);
        }
        return response;
    } catch(error) {
        if(LOG_LEVEL === 'debug') {
            console.log('🌏 send request', `${API_URL}/api/invitations/accept`);
            console.log('📌 get response: ', error.response);
            console.log('🔍 에러 메시지:', error.response.data);
        }
        if(error.response.status === 401) {
            const newAccessToken = await refreshAccessToken();
            if(newAccessToken) {
                return userInviteAccept(data);
            }
            throw new Error('토큰 갱신 실패');
        }
        const jsonError = JSON.parse(error.response.data);
        throw jsonError.message;
    }
}

// 연결 삭제
const connectoinsDelete = async (targetUserId) => {
    const { accessToken } = authStore.getState();
    console.log('🔍 연결 삭제 시작: inviteApi/ 토큰 살아있는지 체크: ', accessToken);
    try {
        const response = (await axios.delete(`${API_URL}/api/invitations/connections/${targetUserId}`, {
            headers: getAuthHeaders(accessToken)
        }));
    }catch(error) {
        if(LOG_LEVEL === 'debug') {
            console.log('🌏 send request', `${API_URL}/api/invitations/connections/${targetUserId}`);
            console.log('📌 get response: ', error.response);
            console.log('🔍 에러 메시지:', error.response.data);
        }
        if(error.response.status === 401) {
            const newAccessToken = await refreshAccessToken();
            if(newAccessToken) {
                return connectoinsDelete(targetUserId);
            }
            throw new Error('토큰 갱신 실패');
        }
        const jsonError = JSON.parse(error.response.data);
        throw jsonError.message;
    }
}

// 연결 재활성화
const connectionsReactivate = async (targetUserId) => {
    const { accessToken } = authStore.getState();
    console.log('🔍 연결 재활성화 시작: inviteApi/ 토큰 살아있는지 체크: ', accessToken);
    try {
        const response = (await axios.post(`${API_URL}/api/invitations/connections/${targetUserId}/reactivate`, {
            headers: getAuthHeaders(accessToken)
        }));
        if(LOG_LEVEL === 'debug') {
            console.log('🔍 연결 재활성화 성공:', response.data);
        }
        return response;
    } catch(error) {
        if(LOG_LEVEL === 'debug') {
            console.log('🌏 send request', `${API_URL}/api/invitations/connections/${targetUserId}/reactivate`);
            console.log('📌 get response: ', error.response);
            console.log('🔍 에러 메시지:', error.response.data);
        }
        if(error.response.status === 401) {
            const newAccessToken = await refreshAccessToken();
            if(newAccessToken) {
                return connectionsReactivate(targetUserId);
            }
            throw new Error('토큰 갱신 실패');
        }
        const jsonError = JSON.parse(error.response.data);
        throw jsonError.message;
    }
}

// 토큰 갱신 함수 (임시로 여기에 정의)
const refreshAccessToken = async () => {
    try {
        const { refreshToken } = authStore.getState();
        if (!refreshToken) {
            throw new Error('리프레시 토큰이 없습니다.');
        }
        
        const response = await axios.post(`${API_URL}/auth/reissue`, {
            headers: getAuthHeaders(refreshToken),
        });
        
        const { accessToken, newRefreshToken } = response.data;
        authStore.getState().setTokens(accessToken, newRefreshToken || refreshToken);
        
        return accessToken;
    } catch (error) {
        console.error('토큰 갱신 실패:', error);
        authStore.getState().forceLogout();
        return null;
    }
};

export { getInviteCode, userInviteAccept, connectoinsDelete, connectionsReactivate };