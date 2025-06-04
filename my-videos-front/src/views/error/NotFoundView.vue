<script setup lang="ts"></script>

<template>
  <div class="container vh-100 d-flex justify-content-center align-items-center flex-column">
    <img src="../../assets/NOT_FOUND.png" alt="NOT_FOUND" />
    <router-link :to="{ name: 'HOME' }" class="btn btn-primary mt-3">메인으로</router-link>
  </div>
</template>

<style scoped>
img {
  max-width: 40%;
  height: auto;
  background-color: white;
}
</style>
