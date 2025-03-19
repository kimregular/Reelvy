<script setup lang="ts">
import { onBeforeMount, ref } from 'vue'
import axios from 'axios'
import { BASE_URL } from '@/constants/server.ts'
import { useRoute } from 'vue-router'

const route = useRoute()
const userInfos = ref()

const fetchUserInfo = async () => {
  const username = route.params.username
  console.log(username)
  const response = await axios.get(`${BASE_URL}/v1/users/${username}/info`)
  userInfos.value = response.data
  console.log(userInfos.value.nickname)
}

onBeforeMount(() => {
  fetchUserInfo()
})
</script>

<template>
  <div v-if="userInfos">{{ userInfos.value.nickname }}</div>
</template>

<style scoped></style>
