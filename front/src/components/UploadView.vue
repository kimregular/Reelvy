<script setup lang="ts">

import {computed, ref} from "vue";

const videoUploadRequirements = ref({
  isVideoFileUploaded: false,
  isVideoTitleContains: false,
  isVideoDescContains: false,
});

const invalidUploadRequest = computed(() => {
  return !videoUploadRequirements.value.isVideoFileUploaded ||
    !videoUploadRequirements.value.isVideoTitleContains ||
    !videoUploadRequirements.value.isVideoDescContains;
});

const videoFile = ref<File | null>(null);
const videoTitle = ref("");
const videoDesc = ref("");
const videoUrl = ref<string | null>(null); // 비디오 미리보기를 위한 URL

const handleVideoFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    videoFile.value = target.files[0];
    videoUrl.value = URL.createObjectURL(videoFile.value); // 파일을 URL로 변환
    videoUploadRequirements.value.isVideoFileUploaded = true;
  } else {
    videoFile.value = null;
    videoUrl.value = null;
    videoUploadRequirements.value.isVideoFileUploaded = false;
  }
};
const fetchUpload = () => {
  if (invalidUploadRequest) {
    alert("유효하지 않은 요청입니다.");
    return;
  }
  alert("업로드 처리 중!");
}
</script>

<template>
  <section class="container mt-5">
    <h3>Upload a Video Here</h3>
    <form @submit.prevent="fetchUpload">
      <div class="mb-3">
        <label for="video-file" class="form-label me-3">Video File</label>
        <div class="d-flex align-items-center">
          <input
            id="video-file"
            @change="handleVideoFileChange"
            type="file"
            accept="video/*"
            name="video"
            required
          />
        </div>
      </div>

      <div class="mb-3">
        <label for="video-title" class="form-label me-3">Video Title</label>
        <input id="video-title" type="text" placeholder="Video Title here" required/>
      </div>

      <div class="mb-3">
        <label for="video-desc" class="form-label me-3">Description</label>
        <textarea id="video-desc" placeholder="Video Description here"></textarea>
      </div>

      <!-- 비디오 미리보기 -->
      <div v-if="videoUrl" class="mb-3">
        <label for="video-preview" class="form-label me-3">Video Preview</label>
        <video id="video-preview" width="320" height="240" controls>
          <source :src="videoUrl" type="video/mp4" />
          Your browser does not support the video tag.
        </video>
      </div>

      <button class="btn btn-primary" type="submit" :disabled="invalidUploadRequest">Upload!</button>
    </form>
  </section>
</template>

<style scoped>

</style>
