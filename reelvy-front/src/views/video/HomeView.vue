<script setup lang="ts">
import { onBeforeMount, ref } from 'vue'
import Video from '@/entities/video.ts'
import VideoCardList from '@/components/video/VideoCardList.vue'
import api from "@/api";

const videos = ref<Video[]>([])

const requestVideos = async () => {
  try {
    const response = await api.get(`/v1/videos/public`)
    const { data } = response
    videos.value = data.map(Video.of)
  } catch (error) {
    console.log('비디오 로딩 실패!', error)
  }
}
onBeforeMount(requestVideos)
</script>

<template>
  <div v-if="videos.length == 0">
    <div class="d-flex justify-content-center align-items-center flex-column">
      <img class="no-video" src="@/assets/NO_VIDEO_FOR_NOW.png" alt="No Video for Now" />
      <div>No video has been found</div>
    </div>
  </div>
  <div v-else>
    <VideoCardList :videos="videos"></VideoCardList>
  </div>
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

.no-video {
  max-width: 40%;
  height: auto;
  background-color: white;
}
</style>
