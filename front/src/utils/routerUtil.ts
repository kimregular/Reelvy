import type {
  NavigationGuardNext,
  RouteLocationNormalizedGeneric,
  RouteLocationNormalizedLoadedGeneric,
} from 'vue-router'
import { getUsername, isLoggedIn } from '@/utils/userUtils.ts'

export const requireAnonymous = (
  to: RouteLocationNormalizedGeneric,
  from: RouteLocationNormalizedLoadedGeneric,
  next: NavigationGuardNext,
) => {
  if (isLoggedIn()) {
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
  if (isLoggedIn()) {
    next()
  } else {
    next({ name: 'LOGIN' })
  }
}

export const isPageOwner = (
  to: RouteLocationNormalizedLoadedGeneric,
  from: RouteLocationNormalizedLoadedGeneric,
  next: NavigationGuardNext,
) => {
  if (isLoggedIn()) {
    const requester = getUsername()
    const pageOwner = to.params.username
    if (requester === pageOwner) {
      next()
      return
    }
  }
  next({ name: 'HOME' })
}
