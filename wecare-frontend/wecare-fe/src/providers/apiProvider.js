import { API_BASE_URL, API_TIMEOUT, LOG_LEVEL } from '../config/environment';

class ApiProvider {
  constructor() {
    this.baseURL = API_BASE_URL;
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

  // HTTP 요청 메서드
  async request(endpoint, options = {}) {
    try {
      const url = `${this.baseURL}${endpoint}`;
      const config = {
        headers: this.getHeaders(),
        ...options,
      };

      // 개발 환경에서만 상세 로깅
      if (LOG_LEVEL === 'debug') {
        console.log('🌐 API Request:', url);
        console.log('📤 Request Config:', config);
      }

      // 타임아웃 설정
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), API_TIMEOUT);

      const response = await fetch(url, {
        ...config,
        signal: controller.signal,
      });

      clearTimeout(timeoutId);
      
      if (LOG_LEVEL === 'debug') {
        console.log('📥 API Response status:', response.status);
      }
      
      // 응답 본문을 먼저 텍스트로 읽기
      const responseText = await response.text();
      
      if (!response.ok) {
        let errorMessage = `HTTP error! status: ${response.status}`;
        try {
          const errorJson = JSON.parse(responseText);
          errorMessage = errorJson.message || errorJson.error || errorMessage;
        } catch {
          // JSON 형식이 아니면 평문 텍스트 사용
          errorMessage = responseText || errorMessage;
        }
        throw new Error(errorMessage);
      }

      // 성공 응답의 경우 JSON 파싱 시도
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (parseError) {
        // JSON 파싱 실패 시 빈 객체 반환 (회원가입 성공 응답의 경우)
        console.log('Response is not JSON format, treating as success:', responseText);
        data = { success: true, message: responseText };
      }
      
      if (LOG_LEVEL === 'debug') {
        console.log('✅ API Response data:', data);
      }
      
      return data;
    } catch (error) {
      if (error.name === 'AbortError') {
        console.error('⏰ API Request Timeout');
        throw new Error('요청 시간이 초과되었습니다. 다시 시도해주세요.');
      }
      
      if (LOG_LEVEL !== 'error') {
        console.error('❌ API Request Error:', error);
      }
      throw error;
    }
  }

  // 로그인 API
  async login(credentials) {
    console.log('Login request payload:', credentials);
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        username: credentials.userId,
        password: credentials.password,
      }),
    });
  }

  // 회원가입 API
  async signup(userData) {
    console.log('Signup request payload:', userData);
    return this.request('/auth/signup', {
      method: 'POST',
      body: JSON.stringify({
        username: userData.username,
        password: userData.password,
        name: userData.name,
        gender: userData.gender,
        birthDate: userData.birthDate,
        role: userData.role,
      }),
    });
  }

  // 기존 register API (호환성을 위해 유지)
  async register(userData) {
    return this.signup(userData);
  }

  // 사용자 정보 조회 API
  async getUserInfo(token) {
    // 여러 가능한 엔드포인트와 메서드 시도
    const endpoints = [
      { path: '/auth/reissue', method: 'POST', useBearer: true },
      { path: '/api/members/me', method: 'GET', useBearer: true },
    ];
    
    for (const endpoint of endpoints) {
      try {
        console.log(`Trying endpoint: ${endpoint.method} ${endpoint.path}`);
        
        // 엔드포인트에 따라 다른 헤더 사용
        const headers = endpoint.useBearer 
          ? this.getAuthHeaders(token)
          : {
              'Content-Type': 'application/json',
              'Authorization': token, 
            };
        const result = await this.request(endpoint.path, {
          method: endpoint.method,
          headers,
        });
        console.log(`Success with endpoint: ${endpoint.method} ${endpoint.path}`);
        return result;
      } catch (error) {
        console.log(`Failed with endpoint ${endpoint.method} ${endpoint.path}:`, error.message);
        if (endpoint === endpoints[endpoints.length - 1]) {
          // 마지막 엔드포인트까지 실패한 경우
          throw error;
        }
      }
    }
  }

  // 토큰 갱신 API
  async refreshToken(refreshToken) {
    return this.request('/auth/refresh', {
      method: 'POST',
      body: JSON.stringify({ refreshToken }),
    });
  }
}

export default new ApiProvider(); 