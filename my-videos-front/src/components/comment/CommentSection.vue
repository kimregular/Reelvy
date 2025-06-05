<script setup lang="ts">
import { onMounted, ref } from "vue";
import { api } from "@/api";
import type Comment from "@/entities/comment.ts";
import dayjs from "dayjs";
import {useUserStore} from "@/stores/useUserStore.ts";

const { videoId } = defineProps<{ videoId: string }>();
const userStore = useUserStore();
const comments = ref<Comment[]>([]);
const commentCount = ref(0);
const newComment = ref('');
const editingId = ref<number | null>(null);
const editContent = ref('');

const fetchComments = async () => {
  const response = await api.get(`/v1/comments`, {
    params: { videoId },
  });
  comments.value = response.data.comments;
  commentCount.value = response.data.commentCount;
};

const postComment = async () => {
  if (!newComment.value.trim()) return;
  try {
    await api.post(`/v1/comments`, {
      content: newComment.value,
    }, {
      params: { videoId }
    });
    newComment.value = '';
    await fetchComments();
  } catch (error) {
    alert("댓글 작성에 실패했습니다." + error);
  }
};

const startEditing = (id: number, content: string) => {
  editingId.value = id;
  editContent.value = content;
};

const cancelEditing = () => {
  editingId.value = null;
  editContent.value = '';
};

const updateComment = async (id: number) => {
  if (!editContent.value.trim()) return;
  try {
    await api.patch(`/v1/comments/${id}`, {
      content: editContent.value,
    });
    editingId.value = null;
    await fetchComments();
  } catch (error) {
    alert("댓글 수정에 실패했습니다." + error);
  }
};

const deleteComment = async (id: number) => {
  if (!confirm("댓글을 삭제하시겠습니까?")) return;
  try {
    await api.delete(`/v1/comments/${id}`);
    await fetchComments();
  } catch (error) {
    alert("댓글 삭제에 실패했습니다." + error);
  }
};

onMounted(fetchComments);
</script>

<template>
  <div class="mt-4">
    <h5>댓글 {{ commentCount }}개</h5>

    <!-- 작성 -->
    <div class="mb-3" v-if="userStore.isLoggedIn">
      <textarea v-model="newComment" rows="2" class="form-control" placeholder="댓글을 입력하세요" />
      <button @click="postComment" class="btn btn-primary btn-sm mt-2">댓글 작성</button>
    </div>

    <!-- 목록 -->
    <div v-for="comment in comments" :key="comment.id" class="mb-2 p-2 border rounded">
      <strong>{{ comment.author }}</strong>
      <small class="text-muted float-end">{{ dayjs(comment.createdAt).format('YYYY.MM.DD HH:mm') }}</small>

      <div v-if="editingId === comment.id">
        <textarea v-model="editContent" class="form-control mt-2" rows="2" />
        <button @click="updateComment(comment.id)" class="btn btn-success btn-sm mt-1">수정 완료</button>
        <button @click="cancelEditing" class="btn btn-secondary btn-sm mt-1 ms-2">취소</button>
      </div>
      <div v-else>
        <p class="mb-0">{{ comment.content }}</p>
        <div v-if="comment.isAuthor || comment.isVideoOwner" class="mt-1">
          <button @click="startEditing(comment.id, comment.content)" class="btn btn-outline-primary btn-sm me-2">수정</button>
          <button @click="deleteComment(comment.id)" class="btn btn-outline-danger btn-sm">삭제</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
textarea {
  resize: vertical;
}
</style>
