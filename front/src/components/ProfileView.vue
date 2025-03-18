<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios, { type AxiosError } from 'axios'
import {
  BASE_URL,
  DEFAULT_BACKGROUND_IMAGE,
  DEFAULT_PROFILE_IMAGE,
  NOT_FOUND_RESPONSE,
} from '@/constants/server.ts'
import User from '@/entities/user.ts'
import Video, { type VideoResponseData } from '@/entities/video.ts'
import VideoCardList from '@/components/VideoCardList.vue'

const route = useRoute()
const username = ref(route.params.username as string)
const user = ref<User | null>(null)
const noVideo = ref(true)

const getUserInfoOf = async (username: string) => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/users/${username}/info`)
    user.value = User.of(response.data)
  } catch (error: unknown) {
    if (error instanceof Error && 'response' in error) {
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
    const videosData = response.data as VideoResponseData[]
    videos.value = videosData.map((v: VideoResponseData) => Video.of(v))
    noVideo.value = videos.value.length === 0
  } catch (error) {
    console.log('비디오 로딩 실패!', error)
  }
}

watch(user, (newUser) => {
  if (newUser?.username) requestVideos()
})

onMounted(() => getUserInfoOf(username.value))
</script>

<template>
  <div v-if="user" class="profile-container">
    <!-- 배경 이미지 -->
    <div
      class="background-image"
      :style="{
        backgroundImage: `url(${user.backgroundImageUrl ? `${BASE_URL}/${user.backgroundImageUrl}` : DEFAULT_BACKGROUND_IMAGE})`,
      }"
    ></div>

    <!-- 프로필 정보 -->
    <div class="profile-info">
      <img
        :src="user.profileImageUrl ? `${BASE_URL}/${user.profileImageUrl}` : DEFAULT_PROFILE_IMAGE"
        class="profile-image"
        alt="profile"
      />
      <div class="nickname-container">
        <h2 class="nickname">{{ user.nickname }}</h2>
      </div>
    </div>
  </div>

  <!-- 비디오 리스트 -->
  <VideoCardList :videos="videos" :no-video="noVideo"></VideoCardList>
</template>

<style scoped>
.profile-container {
  text-align: center;
  padding-bottom: 20px;
}

.background-image {
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
}

.profile-info {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -50px;
}

.profile-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 4px solid #fff;
}
.nickname-container {
  background-color: rgba(254, 254, 254, 1);
  padding: 5px 15px;
  border-radius: 10px;
  margin-top: 5px;
  margin-left: 15px;
}
.nickname {
  font-size: 24px;
  margin: 0;
}

.subscriber-count {
  color: #777;
  font-size: 14px;
}
</style>
