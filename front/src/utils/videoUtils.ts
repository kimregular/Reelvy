import router from '@/router'
import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'
import Video, { type VideoResponseData } from '@/entities/video.ts'

export const watchOf = async (id: number) => {
  await router.push({ name: 'WATCH', query: { videoId: id } })
}

export const getVideosOf = async (username: string) => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/videos/${username}`)
    const { data: videoData } = response
    return videoData.map((v: VideoResponseData) => Video.of(v))
  } catch (error) {
    console.log('비디오 가져오기 실패!', error)
    return []
  }
}
