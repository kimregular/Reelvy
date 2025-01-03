import { createRouter, createWebHistory } from 'vue-router'
import SignupVue from "@/components/SignupView.vue";
import HomeVue from "@/components/HomeView.vue";
import LoginVue from "@/components/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeVue,
    },
    {
      path: "/signup",
      component: SignupVue
    },
    {
      path: "/login",
      component: LoginVue
    }
  ],
})

export default router
