import { defineStore } from 'pinia'
import type User from '@/entities/user.ts'
import api from "@/api";

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
  }),
  actions: {
    setUser(user: User) {
      this.user = user
    },

    clearUser() {
      this.user = null
    },

    async init() {
      try {
        const res = await api.get(`/v1/users/me`)
        this.setUser(res.data)
      } catch {
        console.info('userStore error!')
        this.clearUser()
      }
    },
  },
  getters: {
    isLoggedIn: (state): boolean => state.user !== null,
    username: (state): string => state.user?.username ?? '',
    nickname: (state): string => state.user?.nickname ?? '',
    desc: (state): string => state.user?.desc ?? '',
    profileImageUrl: (state): string => state.user?.profileImageUrl ?? '',
    backgroundImageUrl: (state): string => state.user?.backgroundImageUrl ?? '',
  },
})
