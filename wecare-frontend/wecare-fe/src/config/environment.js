// 환경별 설정
const ENV = {
  development: {
    API_BASE_URL: 'https://wecare.mobidic.shop',
    API_TIMEOUT: 10000,
    LOG_LEVEL: 'debug',
  },
  staging: {
    API_BASE_URL: 'https://staging.wecare.com',
    API_TIMEOUT: 15000,
    LOG_LEVEL: 'info',
  },
  production: {
    API_BASE_URL: 'https://api.wecare.com',
    API_TIMEOUT: 20000,
    LOG_LEVEL: 'error',
  },
};

// 현재 환경 (개발/스테이징/프로덕션)
const CURRENT_ENV = __DEV__ ? 'development' : 'production';

// 환경 설정 가져오기
export const getConfig = () => ENV[CURRENT_ENV];

// API 기본 URL
export const API_BASE_URL = getConfig().API_BASE_URL;

// API 타임아웃
export const API_TIMEOUT = getConfig().API_TIMEOUT;

// 로그 레벨
export const LOG_LEVEL = getConfig().LOG_LEVEL;

// 환경 정보
export const ENVIRONMENT = CURRENT_ENV;

console.log(`🌍 Environment: ${CURRENT_ENV}`);
console.log(`🔗 API Base URL: ${API_BASE_URL}`); 