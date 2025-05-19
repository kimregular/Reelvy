<script setup lang="ts">
import type Video from '@/entities/video.ts'
import { defineProps } from 'vue'
import { watchOf } from '@/utils/videoUtils.ts'

defineProps<{
  videos: Video[]
  loading?: boolean
  noVideo?: boolean
  emptyMessage?: string
}>()
</script>

<template>
  <div class="container mt-5 mb-5">
    <div style="padding: 15px">
      <div v-if="loading" class="d-flex justify-content-center align-items-center flex-column">
        <img src="@/assets/NO_VIDEO_FOR_NOW.png" alt="No Video for Now" />
      </div>
      <div v-else-if="noVideo || videos.length === 0">
        {{ emptyMessage || 'No video has been found' }}
      </div>
      <div
        v-else
        class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-4"
      >
        <div
          class="col video-link"
          v-for="video in videos"
          :key="video.id"
          @click="watchOf(video.id)"
        >
          <div class="card">
            <img
              src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvX7ghSY75PvK5S-RvhkFxNz88MWEALSBDvA&s"
              class="card-img-top"
              alt="..."
            />
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

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-3px);
  cursor: pointer;
}

img {
  max-width: 40%;
  height: auto;
  background-color: white;
}
</style>
