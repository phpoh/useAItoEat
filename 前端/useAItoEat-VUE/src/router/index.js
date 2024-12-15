import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import Chat from '../components/Chat.vue';

const routes = [
  { path: '/', component: LoginPage }, // 登录页
  { path: '/Chat', component: Chat }, // 聊天页面
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
