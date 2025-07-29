# WeCare 앱 배포 가이드

## 환경별 설정
<!-- 여기 URL 수정해야함 -->
### 1. 개발 환경 (Development)
- **API URL**: `https://wecare.mobidic.shop`
- **용도**: 개발 및 테스트
- **로그 레벨**: debug

### 2. 스테이징 환경 (Staging)
- **API URL**: `https://staging.wecare.com`
- **용도**: 배포 전 테스트
- **로그 레벨**: info

### 3. 프로덕션 환경 (Production)
- **API URL**: `https://api.wecare.com`
- **용도**: 실제 서비스
- **로그 레벨**: error

## 서버 URL 변경 방법

### 방법 1: 환경 설정 파일 수정
`src/config/environment.js` 파일에서 각 환경의 `API_BASE_URL`을 수정:

```javascript
const ENV = {
  development: {
    API_BASE_URL: 'https://your-dev-server.com',
  },
  staging: {
    API_BASE_URL: 'https://your-staging-server.com',
  },
  production: {
    API_BASE_URL: 'https://your-production-server.com',
  },
};
```

### 방법 2: 환경 변수 사용 (권장)
EAS 빌드 시 환경 변수로 설정:

```bash
# 개발 빌드
eas build --profile development --platform android

# 스테이징 빌드
eas build --profile staging --platform android

# 프로덕션 빌드
eas build --profile production --platform android
```

## 배포 체크리스트

### ✅ 서버 연결 확인
- [ ] API 엔드포인트 접근 가능
- [ ] CORS 설정 확인
- [ ] SSL 인증서 유효성 확인

### ✅ 인증 시스템
- [ ] 로그인 API 테스트
- [ ] 회원가입 API 테스트
- [ ] 토큰 갱신 API 테스트

### ✅ 보안 설정
- [ ] HTTPS 사용
- [ ] API 키 관리
- [ ] 토큰 만료 시간 설정

### ✅ 모니터링
- [ ] 에러 로깅 설정
- [ ] 성능 모니터링
- [ ] 사용자 분석 도구

## 빌드 명령어

```bash
# 개발 빌드
eas build --profile development --platform android

# 스테이징 빌드
eas build --profile staging --platform android

# 프로덕션 빌드
eas build --profile production --platform android

# iOS 빌드
eas build --profile production --platform ios
```

## 문제 해결

### API 연결 실패
1. 네트워크 연결 확인
2. 서버 URL 정확성 확인
3. CORS 설정 확인
4. SSL 인증서 확인

### 빌드 실패
1. 환경 변수 설정 확인
2. 의존성 설치 확인
3. EAS CLI 버전 확인 