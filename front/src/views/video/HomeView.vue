<script setup lang="ts">
import { onBeforeMount, ref } from 'vue'
import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'
import Video, { type VideoResponseData } from '@/entities/video.ts'
import VideoCardList from '@/components/video/VideoCardList.vue'

const videos = ref<Video[]>([])

const requestVideos = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/videos`)
    const { data: videoData } = response
    videos.value = videoData.map((v: VideoResponseData) => Video.of(v))
  } catch (error) {
    console.log('비디오 로딩 실패!', error)
  }
}
onBeforeMount(requestVideos)
</script>

<template>
  <VideoCardList :videos="videos"></VideoCardList>
</template>

<style scoped>
a {
  text-decoration: none;
}

a:link {
  color: black;
}

a:visited {
  color: black;
}
</style>
