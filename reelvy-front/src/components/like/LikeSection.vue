<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import api from "@/api";

const props = defineProps<{ videoId: string; hasLiked: boolean }>();
const {videoId} = props;

const likeCount = ref(0);
const hasLiked = ref(props.hasLiked);

watch(() => props.hasLiked,
  (newValue) => {
    hasLiked.value = newValue;
  }
);

const fetchLikeCount = async () => {
  try {
    const response = await api.get(`/v1/videos/public/${videoId}/like-count`);
    likeCount.value = response.data;
  } catch (error) {
    console.log(error);
  }
}

const like = async () => {
  try {
    await api.post(`/v1/videos/${videoId}/like`);
    hasLiked.value = true;
    likeCount.value++;
  } catch (error) {
    console.log(error);
  }
}

const unlike = async () => {
  try {
    await api.delete(`/v1/videos/${videoId}/like`);
    hasLiked.value = false;
    likeCount.value--;
  } catch (error) {
    console.log(error);
  }
}

onMounted(() => {
  fetchLikeCount();
})

</script>

<template>
  <div class="d-flex align-items-center gap-2 mb-3">
    <button
      @click="hasLiked ? unlike() : like()"
      class="btn btn-outline-primary btn-sm"
    >
      {{ hasLiked ? '💔 좋아요 취소' : '👍 좋아요' }}
    </button>
    <span>{{ likeCount }}명 이 좋아요를 눌렀어요</span>
  </div>
</template>

<style scoped>

</style>
