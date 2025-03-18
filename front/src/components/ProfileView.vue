<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios, { type AxiosError } from 'axios'
import { BASE_URL, NOT_FOUND_RESPONSE } from '@/constants/server.ts'
import User from '@/entities/user.ts'
import Video, { type VideoResponseData } from '@/entities/video.ts'

const route = useRoute()
const username = ref(route.params.username as string)
const user = ref<User | null>(null)
const noVideo = ref(true)

const getUserInfoOf = async (username: string) => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/users/${username}/info`)
    user.value = User.of(response.data)
    return
  } catch (error: unknown) {
    // Narrow the type to AxiosError
    if (error instanceof Error && 'response' in error) {
      // Type guard
      const axiosError = error as AxiosError
      if (axiosError.response?.status === NOT_FOUND_RESPONSE) {
        console.log(`해당하는 유저를 찾을 수 없습니다 : ${username}`)
      } else {
        console.log('error occurred!')
      }
    } else {
      console.log('Unexpected error occurred!')
    }
  }
}

const videos = ref<Video[]>([])

const requestVideos = async () => {
  if (!user.value) return

  try {
    const response = await axios.get(`${BASE_URL}/v1/videos/${user.value.username}`)
    const videosData = response.data
    videos.value = videosData.map((v: VideoResponseData) => Video.of(v, User.of(v.user)))
    noVideo.value = videos.value.length === 0
  } catch (error) {
    console.log('비디오 로딩 실패!', error)
  }
}

watch(user, (newUser) => {
  if (newUser) requestVideos()
})

onMounted(() => getUserInfoOf(username.value))
</script>

<template>
  <div class="container mt-5">
    <div style="padding: 15px">
      <div v-if="noVideo">No video has been found</div>
      <div
        v-else
        class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-4"
      >
        <div class="col video-link" v-for="video in videos" :key="video.id" @click="video.watch">
          <div class="card">
            <img
              src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvX7ghSY75PvK5S-RvhkFxNz88MWEALSBDvA&s"
              class="card-img-top"
              alt="..."
            />
            <div class="card-body">
              <h5 class="card-title">{{ video.title }}</h5>
              <p class="card-text">{{ video.user.nickname }}</p>
              <p class="card-text">{{ video.desc }}</p>
              <p class="card-text">{{ video.videoView }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
