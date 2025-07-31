import { create } from 'zustand';
import { getInviteCode , userInviteAccept, connectoinsDelete, connectionsReactivate} from '../api/inviteApi';
import useAuthStore from '../../../store/authStore';

// Zustand store의 getState를 직접 가져오기
const authStore = useAuthStore;

const useInviteStore = create((set) => ({
    inviteCode: null, // 초대 코드  
    inviteCodeError: null, // 초대 코드 에러
    isLoading: false, // 로딩 상태
    isSuccess: false, // 초대 코드 수락 성공 여부
    // 초대코드 불러오기
    fetchInviteCode: async () => {
        set({ isLoading: true });
        try {
            console.log('🔍 초대코드 불러오기 시작: inviteStore');
            const response = await getInviteCode();
            console.log('🔍 초대코드 불러오기 성공:', response);
            set({ inviteCode: response.data, inviteCodeError: null });
        } catch (error) {
            set({ inviteCodeError: error });
        } finally {
            set({ isLoading: false });
        }
    },
    // 초대 코드 수락
    fetchInviteAccept: async (data) => {
        set({ isLoading: true });
        try {
            console.log('🔍 초대 코드 수락 시작: inviteStore');
            const response = await userInviteAccept(data);
            console.log('🔍 초대 코드 수락 성공:', response);
            set({ 
                inviteCode: response.data,
                isSuccess: true,
                inviteCodeError: null,
            });
            // 가족 목록 업데이트 => DB 호출 후 업데이트
            // fetchUserInfo 호출
            const { fetchUserInfo } = authStore.getState();
            await fetchUserInfo();
            
        }catch (error) {
            set({ inviteCodeError: error });
        }finally {
            set({ isLoading: false });
        }
    },
    // 연결 삭제
    fetchConnectionsDelete: async (targetUserId) => {
        set({ isLoading: true });
        try {
            console.log('🔍 연결 삭제 시작: inviteStore');
            const response = await connectoinsDelete(targetUserId);
            console.log('🔍 연결 삭제 성공:', response);
        }catch (error) {
            set({ inviteCodeError: error });
        }finally {
            set({ isLoading: false });
        }
    },
    // 연결 재활성화
    fetchConnectionsReactivate: async (targetUserId) => {
        set({ isLoading: true });
        try {
            console.log('🔍 연결 재활성화 시작: inviteStore');
            const response = await connectionsReactivate(targetUserId);
            console.log('🔍 연결 재활성화 성공:', response);
        }catch (error) {
            set({ inviteCodeError: error });
        }finally {
            set({ isLoading: false });
        }
    }
}));



export default useInviteStore;
