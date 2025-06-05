import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
})

// 응답 인터셉터 추가
api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config

    // access-token 만료
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        // refresh 요청
        await api.post('/v1/users/refresh')
        // 재요청
        return api(originalRequest)
      } catch (refreshError) {
        // refresh 실패 → 로그아웃 처리
        console.error('리프레시 실패, 로그아웃 필요')
        window.location.href = '/login' // 또는 store.dispatch('logout')
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  }
)

export default api
