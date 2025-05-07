import router from '@/router'
import { jwtDecode } from 'jwt-decode'
import { useAuthStore } from '@/stores/useAuthStore.ts'

export const handleProfile = (username: string) => {
  router.push({
    name: 'PROFILE',
    params: {
      username,
    },
  })
}

export const isLoggedIn = () => {
  const authStore = useAuthStore()
  return !!authStore.token
}

export const getUsername = () => {
  const authStore = useAuthStore()

  try {
    return jwtDecode(authStore.token).username
  } catch (error) {
    console.log('error!', error)
  }
}
