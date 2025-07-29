import {
  DEV_API_BASE_URL,
  STAGING_API_BASE_URL,
  PROD_API_BASE_URL,
  DEV_API_TIMEOUT,
  STAGING_API_TIMEOUT,
  PROD_API_TIMEOUT,
  DEV_LOG_LEVEL,
  STAGING_LOG_LEVEL,
  PROD_LOG_LEVEL,
} from '@env';

// 환경별 설정
const ENV = {
  development: {
    API_BASE_URL: DEV_API_BASE_URL,
    API_TIMEOUT: parseInt(DEV_API_TIMEOUT),
    LOG_LEVEL: DEV_LOG_LEVEL,
  },
  staging: {
    API_BASE_URL: STAGING_API_BASE_URL,
    API_TIMEOUT: parseInt(STAGING_API_TIMEOUT),
    LOG_LEVEL: STAGING_LOG_LEVEL,
  },
  production: {
    API_BASE_URL: PROD_API_BASE_URL,
    API_TIMEOUT: parseInt(PROD_API_TIMEOUT),
    LOG_LEVEL: PROD_LOG_LEVEL,
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