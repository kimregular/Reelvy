import router from '@/router'

export const watchOf = async (id: number) => {
  await router.push({ name: 'WATCH', query: { videoId: id } })
}
