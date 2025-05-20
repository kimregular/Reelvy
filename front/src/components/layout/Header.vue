<script setup lang="ts">
import { getUsername, isLoggedIn } from '@/utils/userUtils.ts'
import { computed } from 'vue'

const loggedIn = computed(() => isLoggedIn())
const username = computed(() => getUsername())
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
          <template v-if="loggedIn">
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'UPLOAD' }">Upload </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'LOGOUT' }">Logout </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" :to="{ name: 'PROFILE', params: { username } }">
                profile
              </router-link>
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

<style scoped></style>
