import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'

const app = createApp(App)

// 配置 axios 默认值
axios.defaults.baseURL = import.meta.env.PROD ? '/' : '/'
axios.defaults.timeout = 5000
axios.defaults.withCredentials = true

// 将 axios 添加到全局属性中
app.config.globalProperties.$axios = axios

app.mount('#app')
