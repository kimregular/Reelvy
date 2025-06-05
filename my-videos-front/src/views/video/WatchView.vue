<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { BASE_URL } from '@/constants/server.ts'
import { useRoute } from 'vue-router'
import type Video from '@/entities/video.ts'
import { api } from '@/api'
import CommentSection from "@/components/comment/CommentSection.vue";

const video = ref<Video>()
const loading = ref(true)
const streamUrl = ref<string>('')
const route = useRoute()
const videoId = route.query.videoId as string

const fetchVideoInfo = async () => {
  try {
    const response = await api.get(`/v1/videos/${videoId}/info`)
    video.value = response.data
    loading.value = false
  } catch (error) {
    console.error('Failed to load video info:', error)
  }
}

const fetchVideoStreaming = async () => {
  try {
    streamUrl.value = `${BASE_URL}/v1/videos/${videoId}/stream`
  } catch (error) {
    console.error('Failed to load video stream:', error)
  }
}

onMounted(() => {
  fetchVideoInfo()
  fetchVideoStreaming()
})
</script>

<template>
  <div class="container my-4">
    <!-- Loading State -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <p>Loading...</p>
    </div>

    <!-- Video Found -->
    <div v-else-if="video">
      <div class="row justify-content-center">
        <div class="col-12 col-lg-12">
          <!-- Responsive Video Embed -->
          <div class="ratio ratio-16x9 mb-3">
            <video v-if="streamUrl" controls autoplay>
              <source :src="streamUrl" type="video/mp4" />
              Your browser does not support the video tag.
            </video>
            <p v-else class="text-muted">Loading video stream...</p>
          </div>

          <!-- Video Info -->
          <h2 class="h4">{{ video.title }}</h2>
          <div class="mb-3">
            <router-link :to="{ name: 'PROFILE', params: { username: video.user.username } }">
              {{ video.user.username }}
            </router-link>
          </div>
          <p class="text-muted">{{ video.desc }}</p>
          <p class="text-muted">{{ video.videoView }} views</p>

          <CommentSection :video-id="videoId"/>
        </div>
      </div>
    </div>

    <!-- No Video Found -->
    <div v-else class="text-center">
      <p class="text-danger">No video found</p>
    </div>
  </div>
</template>

<style scoped>
a {
  cursor: pointer;
}
</style>
