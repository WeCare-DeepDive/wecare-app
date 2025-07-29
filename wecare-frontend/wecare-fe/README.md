# WeCare Frontend

React Native 기반의 WeCare 앱 프론트엔드 프로젝트입니다.

## 설치 및 설정

### 1. 의존성 설치
```bash
npm install
```

### 2. 환경변수 설정
프로젝트 루트에 `.env` 파일을 생성하고 다음 환경변수들을 설정하세요:

#### 필수 환경변수
```env
# Development Environment
DEV_API_BASE_URL=[DEV URL로 수정해주세요!!]

# Staging Environment
STAGING_API_BASE_URL=[STAGING URL로 수정해주세요!!]

# Production Environment
PROD_API_BASE_URL=[PROD URL로 수정해주세요!!] 

# API Timeout Settings
DEV_API_TIMEOUT=10000
STAGING_API_TIMEOUT=15000
PROD_API_TIMEOUT=20000

# Log Levels
DEV_LOG_LEVEL=debug
STAGING_LOG_LEVEL=info
PROD_LOG_LEVEL=error
```

### 3. 앱 실행
```bash
# 개발 모드로 실행
npm start

# iOS 시뮬레이터에서 실행
npm run ios

# Android 에뮬레이터에서 실행
npm run android
```

## 환경변수 관리

- `.env` 파일은 git에 커밋되지 않습니다 (`.gitignore`에 포함됨)
- `.env.example` 파일을 참고하여 필요한 환경변수를 설정하세요
- 환경변수는 `src/config/environment.js`에서 `@env`를 통해 import하여 사용됩니다

## 프로젝트 구조

```
src/
├── components/     # 재사용 가능한 컴포넌트
├── config/        # 설정 파일 (environment.js 등)
├── navigation/    # 네비게이션 관련
├── providers/     # Context Provider들
├── screen/        # 화면 컴포넌트들
├── store/         # 상태 관리
├── styles/        # 스타일 관련
└── utils/         # 유틸리티 함수들
```

## 주의사항

- `.env` 파일에는 민감한 정보가 포함될 수 있으므로 절대 git에 커밋하지 마세요
- 환경변수를 변경한 후에는 앱을 재시작해야 합니다 