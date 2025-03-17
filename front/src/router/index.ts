import {
  createRouter,
  createWebHistory
} from 'vue-router'

import SignupView from "@/components/SignupView.vue";
import HomeView from "@/components/HomeView.vue";
import LoginView from "@/components/LoginView.vue";
import WatchView from "@/components/WatchView.vue";
import UploadView from "@/components/UploadView.vue";
import {requireAnonymous, requireAuth} from "@/utils/routerUtil.ts";
import ProfileView from "@/components/ProfileView.vue";
import NotFoundView from "@/components/errorPages/NotFoundView.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeView,
      name: "HOME"
    },
    {
      path: "/video/watch",
      component: WatchView,
      name: "WATCH"
    },
    {
      path: "/upload",
      component: UploadView,
      name: "UPLOAD",
      beforeEnter: requireAuth
    },
    {
      path: "/signup",
      component: SignupView,
      name: "SIGNUP",
      beforeEnter: requireAnonymous
    },
    {
      path: "/login",
      component: LoginView,
      name: "LOGIN",
      beforeEnter: requireAnonymous
    },
    {
      path: "/profile/:username",
      component: ProfileView,
      name: "PROFILE",
    },
    {
      path: "/notFound",
      component: NotFoundView,
      name: "NOT_FOUND"
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: {name: "NOT_FOUND"}
    }
  ],
});

export default router
