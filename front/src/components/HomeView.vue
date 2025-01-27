<script setup lang="ts">
import axios from "axios";
import {onBeforeMount, ref} from "vue";
import {BASE_URL} from "@/constants/server.ts";
import {useAuthStore} from "@/stores/useAuthStore.ts";

const welcome = ref("");

onBeforeMount( async () => {
  const authStore = useAuthStore();
  try {
    const response = await axios.get(`${BASE_URL}/v1/user/getInfo`, {
      headers : {
        Authorization: authStore.token,
      }
    });
    welcome.value = response.data;
  } catch (error){
    welcome.value = "error has occured";
  }
})
</script>

<template>
  <div class="container mt-5">
    <h1>HOME PAGE</h1>
    <span>{{welcome}}</span>
  </div>
</template>

<style scoped>

</style>
