import { createRouter, createWebHistory } from 'vue-router'

import { isPageOwner, requireAnonymous, requireAuth } from '@/utils/routerUtil.ts'
import HomeView from '@/views/video/HomeView.vue'
import WatchView from '@/views/video/WatchView.vue'
import Upload from '@/components/video/Upload.vue'
import SignupView from '@/views/auth/SignupView.vue'
import LoginView from '@/views/auth/LoginView.vue'
import ProfileView from '@/views/profile/ProfileView.vue'
import NotFoundView from '@/views/error/NotFoundView.vue'
import VideoEdit from '@/components/profile/VideoEdit.vue'
import VideoEditDetail from '@/components/profile/VideoEditDetail.vue'
import ProfileEditView from '@/views/profile/ProfileEditView.vue'
import { useAuthStore } from '@/stores/useAuthStore.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeView,
      name: 'HOME',
    },
    {
      path: '/video/watch',
      component: WatchView,
      name: 'WATCH',
    },
    {
      path: '/upload',
      component: Upload,
      name: 'UPLOAD',
      beforeEnter: requireAuth,
    },
    {
      path: '/signup',
      component: SignupView,
      name: 'SIGNUP',
      beforeEnter: requireAnonymous,
    },
    {
      path: '/login',
      component: LoginView,
      name: 'LOGIN',
      beforeEnter: requireAnonymous,
    },
    {
      path: '/logout',
      name: 'LOGOUT',
      beforeEnter: () => {
        const authStore = useAuthStore()
        authStore.clearToken()
      },
      component: HomeView,
    },
    {
      path: '/profile/:username',
      component: ProfileView,
      name: 'PROFILE',
    },
    {
      path: '/notFound',
      component: NotFoundView,
      name: 'NOT_FOUND',
    },
    {
      path: '/profile/:username/edit',
      component: ProfileEditView,
      name: 'PROFILE_EDIT',
      beforeEnter: isPageOwner,
    },
    {
      path: '/profile/:username/video/edit',
      component: VideoEdit,
      name: 'VIDEO_EDIT',
      beforeEnter: isPageOwner,
    },
    {
      path: '/profile/:username/video/edit/:videoId',
      component: VideoEditDetail,
      name: 'VIDEO_EDIT_DETAIL',
      beforeEnter: isPageOwner,
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: { name: 'NOT_FOUND' },
    },
  ],
})

export default router
