<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { AUTH_HEADER_KEY, BASE_URL } from '@/constants/server.ts'
import { useAuthStore } from '@/stores/useAuthStore.ts'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')

const loginWarning = ref(false)
const router = useRouter() // Router 인스턴스 생성

const handleLogin = async () => {
  const authStore = useAuthStore()

  if (!email.value || !password.value) {
    loginWarning.value = true
    console.error('Email and password are required.')
    return
  }

  const payload = { username: email.value, password: password.value }

  try {
    const response = await axios.post(`${BASE_URL}/v1/users/login`, payload)
    const token = response.headers[AUTH_HEADER_KEY]

    if (token) {
      authStore.setToken(token) // 쿠키에 토큰 저장
      loginWarning.value = false
      await router.push({ name: 'HOME' }) // 홈으로 이동
    } else {
      console.error('Login failed')
      loginWarning.value = true
    }
  } catch (error) {
    console.error('error!', error)
    loginWarning.value = true
  }
}
</script>

<template>
  <div class="container mt-5">
    <h3>Log In Here</h3>
    <form @submit.prevent="handleLogin">
      <span v-if="loginWarning" class="text-danger">유저정보가 일치하지 않습니다.</span>
      <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Email address</label>
        <input
          v-model="email"
          type="email"
          class="form-control"
          id="exampleInputEmail1"
          aria-describedby="emailHelp"
        />
      </div>
      <div class="mb-3">
        <label for="exampleInputPassword1" class="form-label">Password</label>
        <input v-model="password" type="password" class="form-control" id="exampleInputPassword1" />
      </div>
      <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <div class="mt-5">
      <router-link to="/signup">
        <button type="button" class="btn btn-primary btn-sm">Wanna Sign Up?</button>
      </router-link>
    </div>
  </div>
</template>

<style scoped></style>
