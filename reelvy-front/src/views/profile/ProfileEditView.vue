<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { BASE_URL } from '@/constants/server.ts'
import router from '@/router'
import { useUserStore } from '@/stores/useUserStore.ts'
import api from "@/api";

interface UserData {
  nickname: string
  desc: string
  profileImageUrl: string
  backgroundImageUrl: string
}

const userData = ref<UserData>({
  nickname: '',
  desc: '',
  profileImageUrl: '',
  backgroundImageUrl: '',
})

const profileImageInput = ref<HTMLInputElement | null>(null)
const backgroundImageInput = ref<HTMLInputElement | null>(null)
const profileImageFile = ref<File | null>(null)
const backgroundImageFile = ref<File | null>(null)
const profilePreview = ref<string>('')
const backgroundPreview = ref<string>('')

const handleProfileImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    profileImageFile.value = file
    profilePreview.value = URL.createObjectURL(file)
  }
}

const handleBackgroundImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    backgroundImageFile.value = file
    backgroundPreview.value = URL.createObjectURL(file)
  }
}

const fetchUserData = async (): Promise<void> => {
  try {
    const userStore = useUserStore()
    const username = userStore.username
    const response = await api.get(`/v1/users/public/${username}/info`)
    const data: UserData = response.data
    userData.value = {
      nickname: data.nickname || '',
      desc: data.desc || '',
      profileImageUrl: data.profileImageUrl || '',
      backgroundImageUrl: data.backgroundImageUrl || '',
    }
  } catch (error) {
    console.error('Error fetching user data:', error)
  }
}

interface UserUpdateRequest {
  nickname: string
  desc: string
}

const handleSubmit = async (): Promise<void> => {
  try {
    const formData = new FormData()

    const userUpdateRequest: UserUpdateRequest = {
      nickname: userData.value.nickname,
      desc: userData.value.desc,
    }
    formData.append(
      'user',
      new Blob([JSON.stringify(userUpdateRequest)], { type: 'application/json' }),
    )

    if (profileImageFile.value) {
      formData.append('profileImage', profileImageFile.value)
    }
    if (backgroundImageFile.value) {
      formData.append('backgroundImage', backgroundImageFile.value)
    }
    const response = await api.patch(`/v1/users/update`, formData)

    if (response.status === 200) {
      alert('Profile updated successfully!')
      if (profileImageInput.value) profileImageInput.value.value = ''
      if (backgroundImageInput.value) backgroundImageInput.value.value = ''
      profileImageFile.value = null
      backgroundImageFile.value = null
      profilePreview.value = ''
      backgroundPreview.value = ''
      await fetchUserData()
      await router.push({ name: 'PROFILE' })
      window.location.reload()
    }
  } catch (error) {
    console.error('Error updating profile:', error)
    alert('Failed to update profile. Please try again.')
  }
}

onMounted(() => {
  fetchUserData()
})
</script>

<template>
  <div class="user-profile-form">
    <h2>User Profile</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="nickname">Nickname</label>
        <input
          type="text"
          id="nickname"
          v-model="userData.nickname"
          placeholder="Enter your nickname"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label for="desc">Description</label>
        <textarea
          id="desc"
          v-model="userData.desc"
          placeholder="Tell us about yourself"
          class="form-input"
          rows="4"
        ></textarea>
      </div>

      <div class="form-group">
        <div v-if="userData.profileImageUrl" class="image-preview">
          <label for="curProfile">Current Profile Image</label>
          <img
            :src="`${userData.profileImageUrl}`"
            id="curProfile"
            alt="Current profile"
          />
        </div>
        <label for="profileImage">New Profile Image</label>
        <input
          type="file"
          id="profileImage"
          ref="profileImageInput"
          @change="handleProfileImageChange"
          accept="image/*"
          class="form-input"
        />
        <div v-if="profilePreview" class="image-preview">
          <img :src="profilePreview" alt="Profile preview" />
        </div>
      </div>

      <div class="form-group">
        <div v-if="userData.backgroundImageUrl" class="image-preview">
          <label for="curBackground">Current Background Image</label>
          <img
            :src="`${userData.backgroundImageUrl}`"
            id="curBackground"
            alt="Current background"
          />
        </div>
        <label for="backgroundImage">New Background Image</label>
        <input
          type="file"
          id="backgroundImage"
          ref="backgroundImageInput"
          @change="handleBackgroundImageChange"
          accept="image/*"
          class="form-input"
        />
        <div v-if="backgroundPreview" class="image-preview">
          <img :src="backgroundPreview" alt="Background preview" />
        </div>
      </div>

      <button type="submit" class="submit-btn">Update Profile</button>
    </form>
  </div>
</template>

<style scoped>
.user-profile-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

textarea.form-input {
  resize: vertical;
}

.image-preview {
  margin-top: 10px;
  margin-bottom: 15px;
}

.image-preview img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.submit-btn {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-btn:hover {
  background-color: #45a049;
}
</style>
