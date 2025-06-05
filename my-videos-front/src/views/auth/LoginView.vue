<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import api from "@/api";

const email = ref('')
const password = ref('')

const loginWarning = ref(false)
const router = useRouter() // Router 인스턴스 생성

const handleLogin = async () => {
  if (!email.value || !password.value) {
    loginWarning.value = true
    console.error('Email and password are required.')
    return
  }
  await api.delete('/v1/users/logout').catch(() => {}) // 실패 무시
  try {
    await api.post(`/v1/users/login`, {
      username: email.value,
      password: password.value,
    })

    await router.push({ name: 'HOME' }) // 홈으로 이동
  } catch (error) {
    console.error('error!', error)
    loginWarning.value = true
  }
}

const googleLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
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
      <router-link :to="{ name: 'SIGNUP' }">
        <button type="button" class="btn btn-primary btn-sm">Wanna Sign Up?</button>
      </router-link>
      <button class="btn btn-primary btn-sm" @click="googleLogin()">Sign with Google</button>
    </div>
  </div>
</template>
