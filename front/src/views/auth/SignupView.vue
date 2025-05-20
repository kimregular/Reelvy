<script setup lang="ts">
import { ref, watch } from 'vue'
import { BASE_URL } from '@/constants/server.ts'
import axios from 'axios'
import router from '@/router'

const isInvalidInfo = ref(true) // 회원가입 버튼 비활성화 여부

const isValidEmailStructure = ref(true) // 이메일 형식 검증
const isDuplicateEmail = ref(true) // 이메일 중복 여부

const email = ref('') // 이메일 입력값
const username = ref('') // 사용자 이름 입력값
const password = ref('') // 비밀번호 입력값
const passwordCheck = ref('') // 비밀번호 확인 입력값

const isPasswordMatch = ref(false) // 비밀번호 일치 여부
const isPasswordValidLength = ref(true) // 비밀번호 길이 유효 여부
const isUsernameValid = ref(true) // 사용자 이름 유효 여부

const showEmailWarning = ref(false) // 이메일 경고 표시 여부
const showPasswordWarning = ref(false) // 비밀번호 경고 표시 여부
const showUsernameWarning = ref(false) // 사용자 이름 경고 표시 여부

const hasDuplicateEmail = async (email: string) => {
  if (isInvalidEmail(email)) {
    isValidEmailStructure.value = false
    showEmailWarning.value = true
    return
  }
  isValidEmailStructure.value = true
  showEmailWarning.value = true
  try {
    const response = await axios.post(BASE_URL + '/v1/users/check-email', { email })
    isDuplicateEmail.value = response.data.isDuplicateEmail
  } catch (error) {
    console.log(error)
    isDuplicateEmail.value = true
  }
}

const isInvalidEmail = (email_address: string) => {
  const email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i
  return !email_regex.test(email_address)
}

watch([password, passwordCheck], ([newPassword, newPasswordCheck]) => {
  isPasswordMatch.value = newPassword === newPasswordCheck
  isPasswordValidLength.value = newPassword.length >= 8
  if (newPassword || newPasswordCheck) {
    showPasswordWarning.value = true
  }
})

watch(username, (newUsername) => {
  isUsernameValid.value = newUsername.trim() !== ''
  showUsernameWarning.value = true
})

// 전체 폼 검증
watch(
  [
    isValidEmailStructure,
    isDuplicateEmail,
    isPasswordMatch,
    isPasswordValidLength,
    isUsernameValid,
    email,
    password,
  ],
  () => {
    isInvalidInfo.value =
      !isValidEmailStructure.value || // 이메일 형식 유효 여부
      isDuplicateEmail.value || // 이메일 중복 여부
      !isPasswordMatch.value || // 비밀번호 일치 여부
      !isPasswordValidLength.value || // 비밀번호 길이 조건
      !isUsernameValid.value || // 사용자 이름 유효 여부
      email.value === '' || // 이메일 입력값 유무
      password.value === '' // 비밀번호 입력값 유무
  },
)

const handleSignUp = async () => {
  if (isInvalidInfo.value) {
    alert('회원가입 폼을 작성해주세요')
    return
  }

  const payload = {
    username: email.value,
    nickname: username.value,
    password: password.value,
  }

  try {
    await axios.post(BASE_URL + '/v1/users/signup', payload)
    await router.push('/login')
  } catch (error) {
    console.log('error!', error)
    alert('error!')
    return
  }
}
</script>

<template>
  <div class="container mt-5">
    <h3>Sign Up Here</h3>
    <form @submit.prevent="handleSignUp">
      <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label me-3">Email address</label>
        <span v-if="showEmailWarning && !isValidEmailStructure" class="text-danger me-3"
          >이메일 형식에 맞지 않습니다.</span
        >
        <span v-if="showEmailWarning && !isDuplicateEmail" class="text-primary me-3"
          >사용할 수 있는 이메일입니다.</span
        >
        <span v-if="showEmailWarning && isDuplicateEmail" class="text-danger"
          >사용할 수 없는 이메일입니다.</span
        >
        <div class="d-flex align-items-center">
          <input
            v-model="email"
            type="email"
            class="form-control me-2"
            id="exampleInputEmail1"
            aria-describedby="emailHelp"
            style="flex: 4"
            required
          />
          <button
            type="button"
            class="btn btn-primary"
            style="flex: 0.5"
            @click="hasDuplicateEmail(email)"
          >
            Check
          </button>
        </div>
      </div>

      <!-- 사용자 이름 입력 -->
      <div class="mb-3">
        <label for="username" class="form-label me-3">User name</label>
        <span v-if="showUsernameWarning && !isUsernameValid" class="text-danger"
          >사용자 이름은 필수입니다.</span
        >
        <input
          v-model="username"
          type="text"
          class="form-control"
          id="username"
          aria-describedby="emailHelp"
          required
        />
      </div>

      <!-- 비밀번호 입력 -->
      <div class="mb-3">
        <label for="password-input" class="form-label me-3">비밀번호</label>
        <span v-if="showPasswordWarning && !isPasswordValidLength" class="text-danger"
          >비밀번호는 8자 이상이어야 합니다.</span
        >
        <input
          v-model="password"
          type="password"
          class="form-control"
          id="password-input"
          required
        />

        <label for="password-check" class="form-label me-3">비밀번호 확인</label>
        <span v-if="showPasswordWarning && !isPasswordMatch" class="text-danger"
          >비밀번호가 일치하지 않습니다.</span
        >
        <input
          v-model="passwordCheck"
          type="password"
          class="form-control"
          id="password-check"
          required
        />
      </div>
      <button type="submit" class="btn btn-primary" v-bind:disabled="isInvalidInfo">SignUp</button>
    </form>
    <div class="mt-5">
      <router-link :to="{ name: 'LOGIN' }">
        <button type="button" class="btn btn-primary btn-sm">Already have an Account?</button>
      </router-link>
    </div>
  </div>
</template>

<style scoped></style>
