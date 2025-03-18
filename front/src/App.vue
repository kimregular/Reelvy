<script setup lang="ts">
import { useAuthStore } from '@/stores/useAuthStore'
import router from '@/router'

const authStore = useAuthStore()

const handleUpload = () => {
  router.push({ name: 'UPLOAD' })
}

const handleLogout = () => {
  authStore.clearToken()
  router.replace({ name: 'HOME' })
}

const handleProfile = () => {
  router.push({
    name: 'PROFILE',
    params: {
      username: localStorage.getItem('username'),
    },
  })
}
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">
        <img src="./assets/icons/favicon-32x32.png" alt="MainIcon" />
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <!-- 로그인 상태에 따라 버튼 표시 -->
          <template v-if="authStore.token">
            <li class="nav-item">
              <button class="nav-link btn btn-link" @click="handleUpload">Upload</button>
            </li>
            <li class="nav-item">
              <button class="nav-link btn btn-link" @click="handleLogout">Logout</button>
            </li>
            <li class="nav-item">
              <button class="nav-link btn btn-link" @click="handleProfile">profile</button>
            </li>
          </template>
          <template v-else>
            <li class="nav-item">
              <router-link class="nav-link" to="/login">Login</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/signup">Sign up</router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>

  <router-view></router-view>
</template>

<style scoped>
.nav-link.btn {
  cursor: pointer;
  text-decoration: none;
}
</style>
