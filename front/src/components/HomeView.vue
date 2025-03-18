<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {BASE_URL} from "@/constants/server.ts";
import Video from "@/types/video.ts";
import router from "@/router";
import User from "@/types/user.ts";

const loading = ref(true);
const videos = ref<Video[]>([]);

const requestVideos = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/v1/videos`);
    const {data: videoData} = response;
    videos.value = videoData.map((v:any) => Video.of(v, User.of(v.user)))
    loading.value = videos.value.length === 0;
  } catch (error) {
    console.log("비디오 로딩 실패!")
  }
}
onMounted(requestVideos);



</script>

<template>
  <div class="container mt-5">
    <div style="padding: 15px">
      <div v-if="loading">loading...</div>
      <div v-else class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-4">
        <div class="col video-link" v-for="video in videos" :key="video.id" @click="video.watch">
          <div class="card">
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvX7ghSY75PvK5S-RvhkFxNz88MWEALSBDvA&s"
                 class="card-img-top" alt="...">
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
