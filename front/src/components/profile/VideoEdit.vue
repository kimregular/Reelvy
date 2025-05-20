<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import Video from '@/entities/video.ts'
import { getVideosOf } from '@/utils/videoUtils.ts'
import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'
import { useUserStore } from '@/stores/useUserStore.ts'

const noVideo = ref(true)
const videos = ref<Video[]>([])

const selectedVideos = ref<number[]>([])
const statuses = [
  { value: 'AVAILABLE', label: '시청' },
  { value: 'ARCHIVED', label: '보관' },
  { value: 'DELETED', label: '삭제' },
]
const selectedStatus = ref('AVAILABLE')

const router = useRouter()

const updateStatus = async () => {
  if (selectedVideos.value.length === 0) return
  try {
    const userStore = useUserStore()
    await axios.patch(
      `${BASE_URL}/v1/videos/status`,
      {
        videoIds: selectedVideos.value,
        videoStatus: selectedStatus.value,
      },
      {
        withCredentials: true,
      },
    )
    alert('상태가 변경되었습니다.')
    const username = userStore.username
    if (username) {
      await requestVideos(username)
    }
  } catch (error) {
    console.error('상태 변경 실패:', error)
    alert('상태 변경에 실패했습니다.')
  }
}

const requestVideos = async (username: string) => {
  videos.value = await getVideosOf(username)
  noVideo.value = videos.value.length === 0
  console.log(videos.value)
}

onMounted(() => {
  const userStore = useUserStore()
  const username = userStore.username
  console.log(username)
  if (!username) return
  requestVideos(username)
})
</script>

<template>
  <div class="video-list">
    <h1>My Videos</h1>
    <p v-if="noVideo">업로드한 비디오가 없습니다.</p>
    <div v-else class="bulk-actions">
      <label>
        상태 변경:
        <select v-model="selectedStatus">
          <option v-for="status in statuses" :key="status.value" :value="status.value">
            {{ status.label }}
          </option>
        </select>
      </label>
      <button
        @click="updateStatus"
        :disabled="selectedVideos.length === 0"
        class="btn btn-primary btn-sm"
      >
        적용
      </button>
    </div>
    <ul>
      <li v-for="video in videos" :key="video.id">
        <input type="checkbox" :value="video.id" v-model="selectedVideos" />
        <h3>{{ video.title }}</h3>
        <p>{{ video.desc }}</p>
        <p>조회수: {{ video.videoView }}</p>
        <p>상태 : {{ video.status }}</p>
        <button
          class="btn btn-primary btn-sm"
          @click="router.push({ name: 'VIDEO_EDIT_DETAIL', params: { videoId: video.id } })"
        >
          수정
        </button>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.video-list {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  border: 1px solid #ddd;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
}

.bulk-actions {
  margin-bottom: 15px;
}

.bulk-actions label {
  margin-right: 10px;
}
</style>
