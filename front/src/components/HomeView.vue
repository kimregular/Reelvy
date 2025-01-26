<script setup lang="ts">
import {onBeforeMount, ref} from "vue";
import axios from "axios";
import {BASE_URL} from "@/constants/server.ts";

const welcome = ref("");

onBeforeMount( async () => {
  try {
    const token = document.cookie.split("=")[1];
    console.log(token);
    const response = await axios.get(`${BASE_URL}/v1/user/getInfo`, {
      headers : {
        Authorization: token,
      }
    });
    console.log(response);
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
