import router from '@/router'
import Video, { type VideoResponseData } from '@/entities/video.ts'
import api from "@/api";

export const watchOf = async (id: number) => {
  await router.push({ name: 'WATCH', query: { videoId: id } })
}

export const getVideosOf = async (username: string) => {
  try {
    const response = await api.get(`/v1/videos/public/${username}`)
    const { data: videoData } = response
    return videoData.map((v: VideoResponseData) => Video.of(v))
  } catch (error) {
    console.log('비디오 가져오기 실패!', error)
    return []
  }
}
