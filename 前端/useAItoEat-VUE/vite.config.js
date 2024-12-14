import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import fs from 'fs'; // 引入文件系统模块
import path from 'path'; // 引入路径模块


// 获取本机IP地址
function getLocalIP() {
  const os = require('os');
  const networks = os.networkInterfaces();
  
  for (const name of Object.keys(networks)) {
    for (const net of networks[name]) {
      // 跳过内部IP和非IPv4地址
      if (net.family === 'IPv4' && !net.internal) {
        return net.address;
      }
    }
  }
  return 'localhost';
}

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    open: true,// 自动打开浏览器
    host: '0.0.0.0', // 允许外部访问
    port: 5173, // 指定端口，默认是5173
    https: {
      cert: fs.readFileSync(path.resolve(__dirname, './cert.crt')),
      key: fs.readFileSync(path.resolve(__dirname, './cert.key'))
    }
  }
})


