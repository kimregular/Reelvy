<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {BASE_URL} from "@/constants/server.ts";

const videos = ref([]);
const loading = ref(true);

const requestVideos = async () => {
  try {
    let {data: videoDatas} = await axios.get(`${BASE_URL}/v1/video/videos`);
    console.log(videoDatas);
    loading.value = false;
    videos.value = videoDatas;
  } catch (error) {
    console.log("비디오 로딩 실패!")
  }
}
onMounted(requestVideos);
</script>

<template>
  <div>
    <div v-if="loading">loading...</div>
    <div v-else>
      <div v-for="(video, idx) in videos"
         :key="idx"
         class="video-link">
        <div class="video-container">
          <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvX7ghSY75PvK5S-RvhkFxNz88MWEALSBDvA&s" />
          <div>{{ video.title }}</div>
          <div>{{ video.user.name }}</div>
          <div>{{ video.desc }}</div>
          <div>{{ video.videoView }}</div>
        </div>
      </div>
    </div>
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
</style>
