<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {BASE_URL} from "@/constants/server.ts";
import type Video from "@/types/video.ts";
import router from "@/router";

const videos = ref<Video[]>([]);
const loading = ref(true);

const requestVideos = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/videos`);
    const videosData: Video[] = response.data;
    loading.value = false;
    videos.value = videosData;
  } catch (error) {
    console.log("비디오 로딩 실패!")
  }
}
onMounted(requestVideos);

const watch = (videoId: number) => {
  router.push({name: "WATCH", query: {videoId: videoId.toString()}});
}

</script>

<template>
  <div style="padding: 15px">
    <div v-if="loading">loading...</div>
    <div v-else class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-4">
      <div class="col video-link" v-for="video in videos" :key="video.id" @click="watch(video.id)">
        <div class="card">
          <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvX7ghSY75PvK5S-RvhkFxNz88MWEALSBDvA&s"
               class="card-img-top" alt="...">
          <div class="card-body">
            <h5 class="card-title">{{ video.title }}</h5>
            <p class="card-text">{{ video.user.name }}</p>
            <p class="card-text">{{ video.desc }}</p>
            <p class="card-text">{{ video.videoView }}</p>
          </div>
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
