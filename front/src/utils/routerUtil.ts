import type {
  NavigationGuardNext,
  RouteLocationNormalizedGeneric,
  RouteLocationNormalizedLoadedGeneric,
} from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore.ts'

export const requireAnonymous = (
  to: RouteLocationNormalizedGeneric,
  from: RouteLocationNormalizedLoadedGeneric,
  next: NavigationGuardNext,
) => {
  const authStore = useAuthStore()
  if (authStore.token) {
    // 로그인했다면 홈으로
    next({ name: 'HOME' })
  } else {
    // 아니라면 계속 진행
    next()
  }
}

export const requireAuth = (
  to: RouteLocationNormalizedLoadedGeneric,
  from: RouteLocationNormalizedLoadedGeneric,
  next: NavigationGuardNext,
) => {
  const authStore = useAuthStore()
  if (authStore.token) {
    next()
  } else {
    next({ name: 'LOGIN' })
  }
}
