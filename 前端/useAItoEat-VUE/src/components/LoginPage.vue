<template>
  <div class="login-container">
    <div class="notification" v-if="showNotification">
      <span>{{ notificationMessage }}</span>
    </div>

    <div class="login-box">
      <h1>开发学习测试用(ByHurryWang)</h1>
      <div class="tab-header">
        <button :class="{ active: loginMethod === 'qrcode' }" @click="loginMethod = 'qrcode'">暗号登录</button>
        <button :class="{ active: loginMethod === 'password' }" @click="loginMethod = 'password'">账号密码登录</button>
      </div>

      <div v-if="loginMethod === 'qrcode'" class="qrcode-area">
        <input type="text" v-model="specialCommand" placeholder="请输入登录暗号" />
        <button @click="handleSpecialCommandLogin" :disabled="isWaiting">
          {{ isWaiting ? '等待响应...' : '确认登录' }}
        </button>
        <p class="hint-text">修改暗号命令 例： change:oldName,newName 或直接输入暗号进行登录</p>
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

      <!-- 爱心和烟花效果 -->
      <div v-if="showEffect" class="effect-container">
        <div class="heart">❤️</div>
        <div class="fireworks"></div>
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
const showEffect = ref(false);

const showMsg = (msg, duration = 3000) => {
  notificationMessage.value = msg;
  showNotification.value = true;
  setTimeout(() => (showNotification.value = false), duration);
};

const playFireworkSound = () => {
  const audioContext = new (window.AudioContext || window.webkitAudioContext)();
  const oscillator = audioContext.createOscillator();
  const gainNode = audioContext.createGain();

  oscillator.type = "sine";
  oscillator.frequency.setValueAtTime(800, audioContext.currentTime);
  oscillator.frequency.exponentialRampToValueAtTime(200, audioContext.currentTime + 0.5);
  gainNode.gain.setValueAtTime(0.5, audioContext.currentTime);
  gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);

  oscillator.connect(gainNode);
  gainNode.connect(audioContext.destination);

  oscillator.start();
  oscillator.stop(audioContext.currentTime + 0.5);
};

const handlePasswordLogin = async () => {
  if (!username.value || !password.value) {
    return showMsg('请输入用户名和密码');
  }

  isWaiting.value = true;
  showMsg('正在登录...');

  try {
    const {data} = await axios.post('http://127.0.0.1:8888/api/login', {
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

    if (cmd === '凉面') {
      showEffect.value = true;
      playFireworkSound();
      setTimeout(() => {
        showEffect.value = false;
      }, 3000);
    }

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
.hint-text {
  font-size: 13px;
  color: #aaa;
  margin-top: 10px;
  line-height: 1.5;
}

.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(-45deg, #0f2027, #203a43, #2c5364, #1c1c1c);
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
  color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", sans-serif;
  padding: 20px;
  box-sizing: border-box;
  flex-direction: column;
}

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.login-box {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  padding: 40px 32px;
  border-radius: 20px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
  transition: all 0.3s ease;
  position: relative;
}

h1 {
  text-align: center;
  margin-bottom: 28px;
  font-size: 26px;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: #ffffffee;
}

.tab-header {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.tab-header button {
  flex: 1;
  padding: 10px 14px;
  border: none;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.08);
  color: #bbb;
  border-radius: 10px;
  transition: all 0.2s ease;
  font-weight: 500;
}

.tab-header button.active {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.tab-header button:hover {
  background: rgba(255, 255, 255, 0.12);
}

input[type='text'],
input[type='password'] {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: none;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
  font-size: 15px;
  box-sizing: border-box;
  transition: background 0.2s ease;
}

input::placeholder {
  color: #bbb;
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.12);
}

label {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #ccc;
  margin-bottom: 8px;
}

button {
  width: 100%;
  padding: 12px;
  background: #0a84ff;
  border: none;
  border-radius: 10px;
  color: #fff;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s ease;
  font-size: 15px;
  margin-top: 10px;
}

button:disabled {
  background: #3b3b3b;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background: #0066cc;
}

.notification {
  background: rgba(255, 255, 255, 0.08);
  padding: 12px 20px;
  border-radius: 12px;
  margin-bottom: 24px;
  text-align: center;
  font-size: 14px;
  color: #eee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.footer {
  position: fixed;
  bottom: 12px;
  width: 100%;
  text-align: center;
  color: #888;
  font-size: 12px;
  padding: 0 10px;
  box-sizing: border-box;
}

.footer a {
  color: #aaa;
  text-decoration: none;
}

.footer a:hover {
  text-decoration: underline;
}

/* 爱心和烟花效果 */
.effect-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  pointer-events: none;
}

.heart {
  font-size: 100px;
  animation: heartAnimation 3s ease forwards;
}

@keyframes heartAnimation {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

.fireworks {
  position: absolute;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.8) 10%, transparent 70%);
  animation: fireworkAnimation 3s ease forwards;
}

@keyframes fireworkAnimation {
  0% {
    transform: scale(0);
    opacity: 1;
  }
  50% {
    transform: scale(1);
    opacity: 0.5;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

/* Mobile responsiveness */
@media (max-width: 480px) {
  .login-box {
    padding: 24px 20px;
    border-radius: 16px;
  }

  h1 {
    font-size: 22px;
  }

  .tab-header button {
    padding: 8px 10px;
    font-size: 14px;
  }

  input[type='text'],
  input[type='password'],
  button {
    font-size: 14px;
    padding: 10px;
  }

  .footer {
    font-size: 10px;
    line-height: 1.5;
  }
}
</style>
