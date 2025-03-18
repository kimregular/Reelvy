import router from '@/router'

export const handleProfile = (username: string) => {
  router.push({
    name: 'PROFILE',
    params: {
      username,
    },
  })
}
