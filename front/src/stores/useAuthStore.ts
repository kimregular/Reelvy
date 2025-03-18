import { defineStore } from 'pinia'
import { setCookie, getCookie, deleteCookie } from '@/utils/cookieUtil.ts'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getCookie('authToken'), // 쿠키에서 토큰을 초기화
  }),
  actions: {
    setToken(token: string) {
      this.token = token
      setCookie('authToken', token, new Date()) // 쿠키에 3시간 저장
    },
    clearToken() {
      this.token = null
      localStorage.removeItem('username')
      deleteCookie('authToken') // 쿠키 삭제
    },
  },
})
