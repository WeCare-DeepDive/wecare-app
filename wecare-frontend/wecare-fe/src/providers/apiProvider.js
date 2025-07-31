import axios from 'axios';
import { API_BASE_URL, API_TIMEOUT, LOG_LEVEL } from '../config/environment';
import { useAuthStore } from '../store/authStore';

// Zustand store의 getState와 setState를 직접 가져오기
const authStore = useAuthStore;

class ApiProvider {
  constructor() {
    this.baseURL = API_BASE_URL;
    this.isRefreshing = false;
    this.failedQueue = [];
    
    // axios 인스턴스 생성
    this.axiosInstance = axios.create({
      baseURL: this.baseURL,
      timeout: API_TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });

        // 요청 인터셉터
    this.axiosInstance.interceptors.request.use(
      async (config) => {
        if(config.useAuth != null && config.useAuth === true) {
            // access token 사용
            console.log("useAuth가 적용된 요청입니다.")
            const { accessToken } = authStore.getState();
            console.log('📌 accessToken:', accessToken); // 추가해보기
            if (accessToken) {
                config.headers.Authorization = `Bearer ${accessToken}`;
            }
        }

        if (LOG_LEVEL === 'debug') {
          console.log('🌐 API Request:', config.method?.toUpperCase(), config.url);
          console.log('📤 Request Config:', config);
        }
        return config;
      },
      (error) => {
        console.error('❌ Request Error:', error);
        return Promise.reject(error);
      }
    );

    // 응답 인터셉터
    this.axiosInstance.interceptors.response.use(
      (response) => {
        if (LOG_LEVEL === 'debug') {
          console.log('📥 API Response status:', response.status);
          console.log('✅ API Response data:', response.data);
        }
        return response;
      },
      async (error) => {
        const originalRequest = error.config;
        console.log('🔍 Full Error Response:', error.response);
        // 401 에러이고 토큰 갱신을 시도하지 않은 경우
        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          
          try {
            const { refreshToken } = authStore.getState();
            if (!refreshToken) {
              throw new Error('리프레시 토큰이 없습니다.');
            }

            console.log('🔄 토큰 갱신 시도...');
            const response = await this.axiosInstance.post('/auth/refresh', {
              refreshToken,
            });

            const { accessToken, newRefreshToken } = response.data;
            
            // 새 토큰을 authStore에 저장
            authStore.getState().setTokens(accessToken, newRefreshToken || refreshToken);
            
            // 원래 요청 재시도
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
            
            console.log('✅ 토큰 갱신 성공');
            return this.axiosInstance(originalRequest);
          } catch (refreshError) {
            console.error('❌ 토큰 갱신 실패:', refreshError);
            
            // 토큰 삭제 및 강제 로그아웃 실행
            authStore.getState().forceLogout();
            
            throw new Error('세션이 만료되었습니다. 다시 로그인해주세요.');
          }
        }
        
        if (LOG_LEVEL !== 'error') {
          console.error('❌ API Response Error:', error);
        }
        
        // 타임아웃 에러 처리
        if (error.code === 'ECONNABORTED') {
          throw new Error('요청 시간이 초과되었습니다. 다시 시도해주세요.');
        }
        
        // HTTP 에러 처리
        if (error.response) {
          const { status, data } = error.response;
          let errorMessage = `HTTP error! status: ${status}`;
          
          if (data) {
            if (typeof data === 'string') {
              errorMessage = data;
            } else if (data.message) {
              errorMessage = data.message;
            } else if (data.error) {
              errorMessage = data.error;
            }
          }
          
          throw new Error(errorMessage);
        }
        
        throw error;
      }
    );
  }

  // 공통 헤더 설정
  getHeaders() {
    return {
      'Content-Type': 'application/json',
    };
  }

  // 인증 헤더 설정 (토큰이 있을 때)
  getAuthHeaders(token) {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    };
  }

  // 로그인 API
  async login(credentials) {
    console.log('Login request payload:', credentials);
    const response = await this.axiosInstance.post('/auth/login', {
      username: credentials.userId,
      password: credentials.password,
    });
    return response.data;
  }

  // 회원가입 API
  async signup(userData) {
    console.log('Signup request payload:', userData);
    const response = await this.axiosInstance.post('/auth/signup', {
      username: userData.username,
      password: userData.password,
      name: userData.name,
      gender: userData.gender,
      birthDate: userData.birthDate,
      role: userData.role,
    });
    return response.data;
  }

  // 기존 register API (호환성을 위해 유지)
  async register(userData) {
    return this.signup(userData);
  }

  // 사용자 정보 조회 API
  async getUserInfo() {
    console.log('Fetching user info from /api/members/me');
    // 인터셉터에서 자동으로 토큰 추가 됨
    console.log('📤 Request Headers:', this.axiosInstance.defaults.headers);
    const response = await this.axiosInstance.get('/api/members/me', {
      useAuth: true
    });
    console.log('Successfully fetched user info', response.data);
    return response.data;
  }

  // 토큰 갱신 API
  async refreshToken(refreshToken) {
    const response = await this.axiosInstance.post('/auth/refresh', {
      refreshToken,
    });
    return response.data;
  }
}

export default new ApiProvider(); 