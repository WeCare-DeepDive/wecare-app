import { create } from 'zustand';

const useAuthStore = create((set, get) => ({
  user: {}, // 유저 정보
  isAuthenticated: false, // 인증 상태
  login: (user) => set({ user, isAuthenticated: true }), // 로그인(로그인 성공 시)
  register: (user) => set({ user, isAuthenticated: true }), // 회원가입(회원가입 성공 시)
  setIsAuthenticated: (isAuthenticated) => set({ isAuthenticated }), // 인증 상태 설정
  logout: () => set({ user: {}, isAuthenticated: false }), // 로그아웃
}));

export { useAuthStore };

/*
{               // 유저 정보
    userId: '',
    password: '',
    username: '',
    name: '',
    gender: '',
    birthDate: '',
    role: '',
    phone: '',
  } 
*/
