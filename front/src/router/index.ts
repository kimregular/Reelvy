import {createRouter, createWebHistory} from 'vue-router'
import SignupView from "@/components/SignupView.vue";
import HomeView from "@/components/HomeView.vue";
import LoginView from "@/components/LoginView.vue";
import WatchView from "@/components/WatchView.vue";
import {useAuthStore} from "@/stores/useAuthStore.ts";

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
      name:"WATCH"
    },
    {
      path: "/signup",
      component: SignupView,
      name: "SIGNUP"
    },
    {
      path: "/login",
      name: "LOGIN",
      component: LoginView,
      beforeEnter: (to, from, next) => {
        const authStore = useAuthStore();
        if (authStore.token) {
          // 로그인한 상태면 홈 페이지로 리다이렉트
          next({path: "/"});
        } else {
          next(); // 로그인하지 않았으면 /login 접근 허용
        }
      },
    },
  ],
})

export default router
