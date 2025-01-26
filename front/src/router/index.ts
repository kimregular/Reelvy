import {createRouter, createWebHistory} from 'vue-router'
import SignupVue from "@/components/SignupView.vue";
import HomeVue from "@/components/HomeView.vue";
import LoginVue from "@/components/LoginView.vue";
import {useAuthStore} from "@/stores/useAuthStore.ts";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeVue,
      name: "HOME"
    },
    {
      path: "/signup",
      component: SignupVue,
      name: "SIGNUP"
    },
    {
      path: "/login",
      name: "LOGIN",
      component: LoginVue,
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
