<template>
  <div class="login-container">
    <div class="notification" v-if="showNotification">
      <span>{{ notificationMessage }}</span>
    </div>

    <div class="login-box">
      <h1>开发学习测试用</h1>
      <div class="tab-header">
        <button :class="{ active: loginMethod === 'qrcode' }" @click="loginMethod = 'qrcode'">暗号登录</button>
        <button :class="{ active: loginMethod === 'password' }" @click="loginMethod = 'password'">账号密码登录</button>
      </div>

      <div v-if="loginMethod === 'qrcode'" class="qrcode-area">
        <input type="text" v-model="specialCommand" placeholder="请输入登录暗号" />
        <button @click="handleSpecialCommandLogin" :disabled="isWaiting">
          {{ isWaiting ? '等待响应...' : '确认登录' }}
        </button>
        <p>修改暗号命令 例： change:oldName,newName 或直接输入暗号进行登录</p>
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

  <!-- 备案号信息 -->
  <div class="footer">
    <a href="https://beian.miit.gov.cn/" target="_blank">蜀ICP备2023011467号-1</a>
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
const specialCommand = ref('');
const showNotification = ref(false);
const notificationMessage = ref('');
const isWaiting = ref(false);

const showMsg = (msg, duration = 3000) => {
  notificationMessage.value = msg;
  showNotification.value = true;
  setTimeout(() => (showNotification.value = false), duration);
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

const handleSpecialCommandLogin = async () => {
  if (!specialCommand.value) {
    return showMsg('请输入登录暗号');
  }

  isWaiting.value = true;
  showMsg('处理中...');

  try {
    const cmd = specialCommand.value.trim();

    if (cmd.startsWith('change:')) {
      const params = cmd.slice(7).split(',');
      if (params.length !== 2) {
        showMsg('修改命令格式错误，应为 change:oldName,newName');
        isWaiting.value = false;
        return;
      }
      const [oldName, newName] = params;
      const changeResp = await axios.post('http://127.0.0.1:8888/viplogin/change', {
        oldName: oldName.trim(),
        newName: newName.trim(),
      });
      if (changeResp.data === 'success') {
        showMsg(`用户名已修改：${oldName} → ${newName}`);
      } else {
        showMsg('修改失败：' + changeResp.data);
      }
    } else {
      const username = cmd;
      const loginResp = await axios.post('http://127.0.0.1:8888/viplogin/login', {
        name: username,
      });
      if (loginResp.data && loginResp.data !== 'Invalid username or password') {
        localStorage.setItem('token', loginResp.data);
        showMsg('登录成功！');
        setTimeout(() => router.push('/Chat'), 1000);
      } else {
        showMsg('登录失败：用户名或密码错误');
      }
    }
  } catch (error) {
    console.error(error);
    showMsg('服务器错误，操作失败');
  } finally {
    isWaiting.value = false;
  }
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
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.3);
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

input[type='text'],
input[type='password'] {
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

.footer {
  position: fixed;
  bottom: 10px;
  width: 100%;
  text-align: center;
  color: #aaa;
  font-size: 12px;
}

.footer a {
  color: #aaa;
  text-decoration: none;
}

.footer a:hover {
  text-decoration: underline;
}
</style>
