<script setup lang="ts">

import axios from "axios";
import {onBeforeMount, ref} from "vue";

const welcome = ref("");

onBeforeMount(async () => {
  await axios.get("http://localhost:8080").then((res) => {
    welcome.value = res.data;
    console.log(welcome);
  });
})
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">
        <img src="./assets/icons/favicon-32x32.png">
      </a>
      <button class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" href="#">Features</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Pricing</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <h4>{{ welcome }}</h4>
</template>

<style scoped>
</style>
