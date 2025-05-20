<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import axios, { type AxiosError } from 'axios'
import {
  BASE_URL,
  DEFAULT_BACKGROUND_IMAGE,
  DEFAULT_PROFILE_IMAGE,
  NOT_FOUND_RESPONSE,
} from '@/constants/server.ts'
import User from '@/entities/user.ts'
import Video from '@/entities/video.ts'
import VideoCardList from '@/components/video/VideoCardList.vue'
import { useRoute } from 'vue-router'
import { getUsername } from '@/utils/userUtils.ts'
import router from '@/router'
import { getVideosOf } from '@/utils/videoUtils.ts'

const user = ref<User | null>(null)
const noVideo = ref(true)

const route = useRoute()
const profileUser = ref(route.params.username)
const currentUser = ref(getUsername())

const isProfileOwner = ref(profileUser.value === currentUser.value)

const getUserInfoOf = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/users/${profileUser.value}/info`)
    user.value = User.of(response.data)
  } catch (error: unknown) {
    if (error instanceof Error && 'response' in error) {
      const axiosError = error as AxiosError
      if (axiosError.response?.status === NOT_FOUND_RESPONSE) {
        console.log(`해당하는 유저를 찾을 수 없습니다 : ${profileUser.value}`)
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

  videos.value = await getVideosOf(user.value.username)
  noVideo.value = videos.value.length === 0
}

const handleProfileEditView = () => {
  router.push({ name: 'PROFILE_EDIT', params: { username: profileUser.value } })
}

const handleVideoEditView = () => {
  if (!user.value) {
    console.log('User 정보가 없습니다.')
    return
  }
  router.push({ name: 'VIDEO_EDIT', params: { username: user.value.username } })
}

watch(user, (newUser) => {
  if (newUser?.username) requestVideos()
})

onMounted(() => getUserInfoOf())
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

    <div v-if="user.desc" class="container description-container">
      <p class="description text-muted fs-6 mb-0">{{ user.desc }}</p>
    </div>
  </div>

  <div class="container">
    <div v-if="isProfileOwner" class="button-group">
      <button class="btn btn-primary" @click="handleProfileEditView">edit profile</button>
      <button class="btn btn-primary" @click="handleVideoEditView">edit videos</button>
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

.button-group {
  display: flex;
  gap: 10px; /* 버튼 사이 간격 */
  flex-wrap: wrap; /* 작은 화면에서 줄바꿈 허용 */
  justify-content: center;
}

.btn {
  min-width: 120px; /* 버튼 최소 너비 설정 */
  padding: 8px 16px;
  transition: transform 0.2s; /* 버튼 클릭 시 애니메이션 */
}

.btn:hover {
  transform: translateY(-2px); /* 호버 시 약간 올라가는 효과 */
}

@media (max-width: 480px) {
  .button-group {
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }

  .btn {
    width: 100%;
    max-width: 200px;
  }
}
.description-container {
  margin-top: 1rem;
}
</style>
