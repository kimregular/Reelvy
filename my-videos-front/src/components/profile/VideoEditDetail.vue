<template>
  <div class="container">
    <div class="video-edit-detail">
      <h2>비디오 정보 수정</h2>
      <form @submit.prevent="submitUpdate">
        <div class="form-group">
          <label for="title">제목</label>
          <input id="title" v-model="title" class="form-control" />
        </div>

        <div class="form-group">
          <label for="desc">설명</label>
          <textarea id="desc" v-model="desc" class="form-control"></textarea>
        </div>

        <div class="form-group">
          <label for="status">상태</label>
          <select v-model="status" id="status" class="form-control">
            <option value="AVAILABLE">시청</option>
            <option value="ARCHIVED">보관</option>
            <option value="DELETED">삭제</option>
          </select>
        </div>

        <button type="submit" class="btn btn-primary">저장</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from "@/api";

const route = useRoute()
const router = useRouter()
const videoId = route.params.videoId as string

const title = ref('')
const desc = ref('')
const status = ref('AVAILABLE')

onMounted(async () => {
  const response = await api.get(`/v1/videos/${videoId}/info`)
  const video = response.data
  title.value = video.title
  desc.value = video.desc
  status.value = video.videoStatus
})

const submitUpdate = async () => {
  await api.patch(`v1/videos/${videoId}/info`, {
    title: title.value,
    desc: desc.value,
    videoStatus: status.value,
  })
  alert('비디오 정보가 수정되었습니다.')
  router.back()
}
</script>

<style scoped>
.form-group {
  margin-bottom: 15px;
}
</style>
