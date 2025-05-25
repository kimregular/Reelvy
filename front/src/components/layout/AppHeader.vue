<script setup lang="ts">
import { computed } from 'vue'
import { useUserStore } from '@/stores/useUserStore.ts'
import { api } from '@/api'
import router from '@/router'

const userStore = useUserStore()
const loggedIn = computed(() => userStore.isLoggedIn)
const username = computed(() => userStore.username)

const handleLogout = () => {
  api
    .post('/v1/users/logout')
    .then(() => {
      userStore.clearUser()
      router.push({ name: 'HOME' })
    })
    .catch((error) => {
      console.error('로그아웃 실패!', error)
    })
}
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
      <router-link class="navbar-brand" :to="{ name: 'HOME' }">
        <img src="@/assets/icons/favicon-32x32.png" alt="MainIcon" />
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
          <template v-if="loggedIn && username">
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'UPLOAD' }">Upload</router-link>
            </li>
            <li class="nav-item">
              <router-link
                class="nav-link"
                :to="{ name: 'PROFILE', params: { username: username } }"
              >
                profile
              </router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="handleLogout">Logout</a>
            </li>
          </template>
          <template v-else>
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'LOGIN' }">Login</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'SIGNUP' }">Sign up</router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>
