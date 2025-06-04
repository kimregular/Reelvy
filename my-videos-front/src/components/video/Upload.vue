<script setup lang="ts">
import { computed, ref } from 'vue'
import router from '@/router'
import { api } from '@/api'

const videoUploadRequirements = ref({
  isVideoFileUploaded: false,
  isVideoTitleContains: false,
  isVideoDescContains: false,
})

const invalidUploadRequest = computed(() => {
  return (
    !videoUploadRequirements.value.isVideoFileUploaded ||
    !videoUploadRequirements.value.isVideoTitleContains ||
    !videoUploadRequirements.value.isVideoDescContains
  )
})

const videoFile = ref<File | null>(null)
const videoTitle = ref('')
const videoDesc = ref('')
const videoUrl = ref<string | null>(null) // 비디오 미리보기를 위한 URL

const handleVideoFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    videoFile.value = target.files[0]
    videoUrl.value = URL.createObjectURL(videoFile.value) // 파일을 URL로 변환
    videoUploadRequirements.value.isVideoFileUploaded = true
  } else {
    videoFile.value = null
    videoUrl.value = null
    videoUploadRequirements.value.isVideoFileUploaded = false
  }
}

const handleVideoTitleChange = (event: Event) => {
  const { value: titleInfo } = event.target as HTMLInputElement
  if (titleInfo === '') {
    alert('제목은 필수값입니다.')
    return
  }
  videoTitle.value = titleInfo
  videoUploadRequirements.value.isVideoTitleContains = true
}

const handleVideoDescChange = (event: Event) => {
  const { value: descInfo } = event.target as HTMLInputElement
  if (descInfo === '') {
    alert('설명은 필수값입니다.')
    return
  }
  videoDesc.value = descInfo
  videoUploadRequirements.value.isVideoDescContains = true
}
const fetchUpload = async () => {
  if (invalidUploadRequest.value) {
    alert('유효하지 않은 요청입니다.')
    return
  }

  const formData = new FormData()
  formData.append('videoFile', videoFile.value as Blob)
  formData.append('title', videoTitle.value)
  formData.append('desc', videoDesc.value)

  try {
    await api.post(`/v1/videos/upload`, formData)
    await router.replace({ name: 'HOME' })
  } catch (error) {
    console.log('error!', error)
    alert('error!')
    return
  }
}
</script>

<template>
  <div class="container mt-5">
    <h3>Upload a Video Here</h3>
    <form @submit.prevent="fetchUpload">
      <div class="mb-3">
        <label for="video-file" class="form-label me-3">Video File</label>
        <div class="d-flex align-items-center">
          <input
            id="video-file"
            class="form-control me2"
            @change="handleVideoFileChange"
            type="file"
            accept="video/*"
            name="video"
            required
          />
        </div>
      </div>

      <!-- 비디오 미리보기 -->
      <div v-if="videoUrl" class="mb-3">
        <label for="video-preview" class="form-label me-3">Video Preview</label>
        <div class="ratio ratio-16x9">
          <video id="video-preview" controls>
            <source :src="videoUrl" type="video/mp4" />
            Your browser does not support the video tag.
          </video>
        </div>
      </div>

      <div class="mb-3">
        <label for="video-title" class="form-label me-3">Video Title</label>
        <input
          id="video-title"
          class="form-control me2"
          @change="handleVideoTitleChange"
          type="text"
          placeholder="Video Title here"
          required
        />
      </div>

      <div class="mb-3">
        <label for="video-desc" class="form-label me-3">Description</label>
        <textarea
          id="video-desc"
          class="form-control me2"
          rows="15"
          @change="handleVideoDescChange"
          placeholder="Video Description here"
        ></textarea>
      </div>

      <button class="btn btn-primary mb-5" type="submit" :disabled="invalidUploadRequest">
        Upload!
      </button>
    </form>
  </div>
</template>

<style scoped>
textarea {
  resize: none;
}
</style>
