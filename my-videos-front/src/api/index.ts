import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'
import {useUserStore} from "@/stores/useUserStore.ts";
import router from "@/router";

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
})

// 응답 인터셉터 추가
api.interceptors.response.use(
  response => response,
  async (error) => {
    const originalRequest = error.config
    const userStore = useUserStore()

    // refresh 요청에서 또 refresh 시도하지 않음
    if (originalRequest.url?.includes('/v1/users/public/refresh')) {
      return Promise.reject(error)
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        await api.post('/v1/users/public/refresh')
        return api(originalRequest)
      } catch (e) {
        userStore.clearUser()
        await router.push({name: 'LOGIN'})
        return Promise.reject(e)
      }
    }

    return Promise.reject(error)
  }
)

export default api
