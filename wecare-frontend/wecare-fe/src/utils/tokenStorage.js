// 간단한 메모리 기반 토큰 저장소
class TokenStorage {
  constructor() {
    this.accessToken = null;
    this.refreshToken = null;
  }

  // 토큰 저장
  async setTokens(accessToken, refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    console.log('Tokens saved to memory');
  }

  // 액세스 토큰 가져오기
  async getAccessToken() {
    return this.accessToken;
  }

  // 리프레시 토큰 가져오기
  async getRefreshToken() {
    return this.refreshToken;
  }

  // 토큰 삭제
  async clearTokens() {
    this.accessToken = null;
    this.refreshToken = null;
    console.log('Tokens cleared from memory');
  }

  // 토큰 존재 여부 확인
  async hasTokens() {
    return !!(this.accessToken && this.refreshToken);
  }
}

export default new TokenStorage(); 