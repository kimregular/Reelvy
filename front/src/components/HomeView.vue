<script setup lang="ts">
import {onBeforeMount, ref} from "vue";
import axios from "axios";

const welcome = ref("");

onBeforeMount( async () => {
  try {
    const response = await axios.get("http://localhost:8080");
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
