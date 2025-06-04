<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { type AxiosError } from 'axios'
import {
  DEFAULT_BACKGROUND_IMAGE,
  DEFAULT_PROFILE_IMAGE,
  NOT_FOUND_RESPONSE,
} from '@/constants/server.ts'
import User from '@/entities/user.ts'
import Video from '@/entities/video.ts'
import VideoCardList from '@/components/video/VideoCardList.vue'
import { useRoute } from 'vue-router'
import { getVideosOf } from '@/utils/videoUtils.ts'
import { useUserStore } from '@/stores/useUserStore.ts'
import { api } from '@/api'

const userStore = useUserStore()
const user = ref<User | null>(null)
const noVideo = ref(true)

const route = useRoute()
const profileUser = ref(route.params.username)
const currentUser = ref(userStore.username)

const isLoggedIn = ref(userStore.isLoggedIn)
const isProfileOwner = ref(profileUser.value === currentUser.value)

const getUserInfoOf = async () => {
  try {
    const response = await api.get(`/v1/users/${profileUser.value}/info`)
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

watch(user, (newUser) => {
  if (newUser?.username) requestVideos()
})

onMounted(() => getUserInfoOf())
</script>

<template>
  <div v-if="isLoggedIn" class="profile-container">
    <!-- 배경 이미지 -->
    <div
      class="background-image"
      :style="{
        backgroundImage: `url(${userStore.backgroundImageUrl ? `${userStore.backgroundImageUrl}` : DEFAULT_BACKGROUND_IMAGE})`,
      }"
    ></div>

    <!-- 프로필 정보 -->
    <div class="profile-info">
      <img
        :src="userStore.profileImageUrl ? `${userStore.profileImageUrl}` : DEFAULT_PROFILE_IMAGE"
        class="profile-image"
        alt="profile"
      />
      <div class="nickname-container">
        <h2 class="nickname">{{ userStore.nickname }}</h2>
      </div>
    </div>

    <div v-if="userStore.desc" class="container description-container">
      <p class="description text-muted fs-6 mb-0">{{ userStore.desc }}</p>
    </div>
  </div>

  <div class="container">
    <div v-if="isProfileOwner" class="button-group">
      <router-link
        class="btn btn-primary"
        :to="{ name: 'PROFILE_EDIT', params: { username: profileUser.value } }"
        >edit profile
      </router-link>
      <router-link
        class="btn btn-primary"
        :to="{ name: 'VIDEO_EDIT', params: { username: userStore.username } }"
        >edit videos
      </router-link>
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
