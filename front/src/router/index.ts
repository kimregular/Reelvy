import { createRouter, createWebHistory } from 'vue-router'

import { isPageOwner, requireAnonymous, requireAuth } from '@/utils/routerUtil.ts'
import HomeView from '@/components/HomeView.vue'
import WatchView from '@/components/video/WatchView.vue'
import UploadView from '@/components/video/UploadView.vue'
import SignupView from '@/components/auth/SignupView.vue'
import LoginView from '@/components/auth/LoginView.vue'
import ProfileView from '@/components/profile/ProfileView.vue'
import NotFoundView from '@/components/errorPages/NotFoundView.vue'
import ProfileEditView from '@/components/profile/ProfileEditView.vue'
import VideoEditView from '@/components/profile/VideoEditView.vue'
import VideoEditDetailView from '@/components/profile/VideoEditDetailView.vue'

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
      component: UploadView,
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
      component: VideoEditView,
      name: 'VIDEO_EDIT',
      beforeEnter: isPageOwner,
    },
    {
      path: '/profile/:username/video/edit/:videoId',
      component: VideoEditDetailView,
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
