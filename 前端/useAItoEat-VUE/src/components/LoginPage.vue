<template>
  <div class="login-container">
    <div class="notification" v-if="showNotification">
      <span>{{ notificationMessage }}</span>
    </div>

    <div class="login-box">
      <h1>Welcome Eat</h1>
      <div class="tab-header">
        <button :class="{ active: loginMethod === 'qrcode' }" @click="loginMethod = 'qrcode'">扫码登录</button>
        <button :class="{ active: loginMethod === 'password' }" @click="loginMethod = 'password'">账号密码登录</button>
      </div>

      <div v-if="loginMethod === 'qrcode'" class="qrcode-area">
        <button @click="handleQRCodeLogin" :disabled="isWaiting">
          {{ isWaiting ? '等待授权中...' : '点击发起扫码申请' }}
        </button>
        <p>开发者专属登录通道</p>
      </div>

      <div v-if="loginMethod === 'password'" class="password-area">
        <input type="text" v-model="username" placeholder="用户名" />
        <input type="password" v-model="password" placeholder="密码" />
        <label>
          <input type="checkbox" v-model="remember" />
          记住我
        </label>
        <button @click="handlePasswordLogin">登 录</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const loginMethod = ref('password');
const username = ref('');
const password = ref('');
const remember = ref(false);
const showNotification = ref(false);
const notificationMessage = ref('');
const isWaiting = ref(false);

const showMsg = (msg, duration = 3000) => {
  notificationMessage.value = msg;
  showNotification.value = true;
  setTimeout(() => showNotification.value = false, duration);
};

const handlePasswordLogin = async () => {
  if (!username.value || !password.value) {
    return showMsg('请输入用户名和密码');
  }

  isWaiting.value = true;
  showMsg('正在登录...');

  try {
    const { data } = await axios.post('http://127.0.0.1:8888/api/login', {
      username: username.value,
      password: password.value,
    });

    if (data && data !== 'Invalid username or password') {
      localStorage.setItem('token', data);
      showMsg('登录成功！');
      setTimeout(() => router.push('/Chat'), 1000);
    } else {
      showMsg('用户名或密码错误');
    }
  } catch (e) {
    console.error(e);
    showMsg('登录失败，请检查服务器');
  } finally {
    isWaiting.value = false;
  }
};

const handleQRCodeLogin = () => {
  isWaiting.value = true;
  showMsg('正在请求扫码登录...');
  setTimeout(() => {
    showMsg('请求被拒绝或超时');
    isWaiting.value = false;
  }, 5000);
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #1e1e1e;
  color: #fff;
  flex-direction: column;
  padding: 20px;
}

.login-box {
  background: #2c2c2c;
  padding: 32px;
  border-radius: 12px;
  width: 100%;
  max-width: 360px;
  box-shadow: 0 0 12px rgba(0,0,0,0.3);
}

h1 {
  text-align: center;
  margin-bottom: 24px;
}

.tab-header {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tab-header button {
  flex: 1;
  padding: 10px;
  border: none;
  cursor: pointer;
  background: #444;
  color: #ccc;
  border-radius: 6px;
}

.tab-header button.active {
  background: #666;
  color: #fff;
}

input[type="text"], input[type="password"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border: none;
  border-radius: 6px;
  background: #3a3a3a;
  color: #fff;
}

button {
  width: 100%;
  padding: 10px;
  background: #4f46e5;
  border: none;
  border-radius: 6px;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
}

.notification {
  background: rgba(255, 255, 255, 0.1);
  padding: 10px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  text-align: center;
}
</style>
