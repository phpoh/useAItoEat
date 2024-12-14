<template>
  <div class="login-container">
    <div class="notification" v-if="showNotification">
      <div class="notification-content">
        <span>{{ notificationMessage }}</span>
        <div class="loading-dots" v-if="isWaiting">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
    </div>
    
    <div class="background-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>
    
    <div class="login-wrapper">
      <!-- 登录框 -->
      <div class="login-box">
        <div class="glass-effect">
          <h1>Welcome Eat</h1>
          
          <div class="login-methods">
            <div class="tab-header">
              <button 
                :class="{ active: loginMethod === 'qrcode' }" 
                @click="loginMethod = 'qrcode'"
              >
                移动端授权登录by小辉
              </button>
              <button 
                :class="{ active: loginMethod === 'password' }" 
                @click="loginMethod = 'password'"
              >
                账号密码登录
              </button>
            </div>
            
            <div class="login-content">
              <!-- 扫码登录区域 -->
              <div v-if="loginMethod === 'qrcode'" class="qrcode-area">
                <button class="scan-button" @click="handleQRCodeLogin" :disabled="isWaiting">
                  <span class="button-content">
                    <svg class="qr-icon" viewBox="0 0 24 24" fill="none">
                      <path d="M3 3h6v6H3V3zm12 0h6v6h-6V3zm0 12h6v6h-6v-6zM3 15h6v6H3v-6z" stroke="currentColor" stroke-width="2"/>
                    </svg>
                    <span>{{ isWaiting ? '等待授权中...' : '点击向小辉手机发起申请' }}</span>
                  </span>
                </button>
                <p class="tip-text">开发者专属登录通道用户请勿点击！</p>
              </div>

              <!-- 账号密码登录区域 -->
              <div v-if="loginMethod === 'password'" class="password-area">
                <div class="input-wrapper">
                  <input 
                    type="text" 
                    v-model="username" 
                    placeholder="用户名" 
                    class="login-input"
                  />
                  <input 
                    type="password" 
                    v-model="password" 
                    placeholder="密码" 
                    class="login-input"
                  />
                </div>
                
                <div class="remember-forgot">
                  <label class="remember">
                    <input type="checkbox" v-model="remember" />
                    <span class="checkmark"></span>
                    <span>记住我</span>
                  </label>
                </div>
                
                <button @click="handlePasswordLogin" class="login-button">
                  <span class="button-content">登 录</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 账号状态窗口 -->
      <div class="status-box">
        <div class="glass-effect">
          <h2>账号状态</h2>
          <div class="account-list">
            <div 
              v-for="account in accounts" 
              :key="account.name"
              class="account-item"
            >
              <span class="account-name">{{ account.name }}</span>
              <span 
                class="status-indicator"
                :class="{ 'status-busy': account.isBusy }"
              ></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  name: 'LoginPage',
  setup() {
    const loginMethod = ref('password');
    const username = ref('');
    const password = ref('');
    const remember = ref(false);

    // 账号状态数据
    // false 表示空闲  true 表示忙碌
    const accounts = ref([
      { name: 'zhihui', isBusy: false },
      { name: 'zhihui1', isBusy: false },
      { name: 'zhihui2', isBusy: false },
      { name: 'hurry', isBusy: false },
      { name: 'hurry2', isBusy: false }
    ]);

    // 添加新的状态控制
    const showNotification = ref(false);
    const notificationMessage = ref('');
    const isWaiting = ref(false);

    // 模拟接收后端状态更新
    const updateAccountStatus = (accountName, status) => {
      const account = accounts.value.find(acc => acc.name === accountName);
      if (account) {
        account.isBusy = status;
      }
    };

    const handleQRCodeLogin = async () => {
      try {
        if (isWaiting.value) return;
        
        isWaiting.value = true;
        notificationMessage.value = '已发送申请，等待开发者授权';
        showNotification.value = true;

        // 这里可以添加实际的后端请求
        console.log('发起扫码登录请求');

        // 模拟5秒后被拒绝
        setTimeout(() => {
          showNotification.value = false;
          // 短暂延迟后显示拒绝消息
          setTimeout(() => {
            notificationMessage.value = '小辉不想理你，别乱走开发者通道，去账号密码登录吧';
            showNotification.value = true;
            isWaiting.value = false; // 重置等待状态
            
            // 3秒后隐藏拒绝消息
            setTimeout(() => {
              showNotification.value = false;
            }, 3000);
          }, 100);
        }, 5000);

      } catch (error) {
        notificationMessage.value = '申请发送失败，请重试';
        showNotification.value = true;
        setTimeout(() => {
          showNotification.value = false;
        }, 3000);
        console.error('扫码登录请求失败:', error);
      }
    };

    const handlePasswordLogin = () => {
      console.log('用户登录:', username.value, password.value);
      // 模拟登录后更新状态
      updateAccountStatus(username.value, true);
    };

    // 这里可以添加 WebSocket 连接来实时更新状态
    onMounted(() => {
      // 连接 WebSocket
      // const ws = new WebSocket('your-websocket-url');
      // ws.onmessage = (event) => {
      //   const { accountName, status } = JSON.parse(event.data);
      //   updateAccountStatus(accountName, status);
      // };
    });

    return {
      loginMethod,
      username,
      password,
      remember,
      accounts,
      handleQRCodeLogin,
      handlePasswordLogin,
      showNotification,
      notificationMessage,
      isWaiting,
    };
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #0f172a;
  position: relative;
  overflow: hidden;
}

.background-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.shape {
  position: absolute;
  filter: blur(100px);
  opacity: 0.5;
  animation: float 10s infinite ease-in-out;
}

.shape-1 {
  background: #4f46e5;
  width: 300px;
  height: 300px;
  top: -150px;
  left: -150px;
}

.shape-2 {
  background: #7c3aed;
  width: 400px;
  height: 400px;
  bottom: -200px;
  right: -200px;
}

.shape-3 {
  background: #2563eb;
  width: 250px;
  height: 250px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-wrapper {
  display: flex;
  gap: 24px;
  align-items: flex-start;
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.login-box {
  flex: 1;
  min-width: 420px;
}

.status-box {
  width: 300px;
}

.status-box .glass-effect {
  padding: 24px;
}

.status-box h2 {
  color: #fff;
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 24px;
  text-align: center;
}

.account-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.account-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.account-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.account-name {
  color: #fff;
  font-size: 16px;
}

.status-indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #10b981; /* 绿色 - 空闲 */
  transition: all 0.3s ease;
  box-shadow: 0 0 8px rgba(16, 185, 129, 0.5);
}

.status-indicator.status-busy {
  background-color: #ef4444; /* 红色 - 忙碌 */
  box-shadow: 0 0 8px rgba(239, 68, 68, 0.5);
}

.glass-effect {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #fff;
  font-size: 32px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 40px;
  letter-spacing: -0.5px;
}

.tab-header {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
}

.tab-header button {
  flex: 1;
  padding: 12px;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-header button.active {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-color: rgba(255, 255, 255, 0.2);
}

.login-input {
  width: 100%;
  height: 48px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 0 16px;
  color: #fff;
  font-size: 16px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.login-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.login-input:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 24px 0;
}

.remember {
  display: flex;
  align-items: center;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  cursor: pointer;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  margin-right: 8px;
  position: relative;
  transition: all 0.2s ease;
}

.remember input {
  display: none;
}

.remember input:checked + .checkmark {
  background: #4f46e5;
  border-color: #4f46e5;
}

.remember input:checked + .checkmark::after {
  content: '';
  position: absolute;
  left: 5px;
  top: 2px;
  width: 4px;
  height: 8px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.login-button, .scan-button {
  width: 100%;
  height: 48px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button {
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  color: #fff;
}

.scan-button {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
}

.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.qr-icon {
  width: 20px;
  height: 20px;
}

.login-button:hover, .scan-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.login-button:active, .scan-button:active {
  transform: translateY(0);
}

.tip-text {
  text-align: center;
  color: rgba(255, 255, 255, 0.4);
  font-size: 14px;
  margin-top: 16px;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@media (max-width: 900px) {
  .login-wrapper {
    flex-direction: column;
    align-items: center;
    padding: 16px;
  }

  .login-box {
    min-width: unset;
    width: 100%;
  }

  .glass-effect {
    padding: 24px;
  }

  .status-box {
    width: 100%;
    max-width: 420px;
    margin-top: 20px;
  }

  .tab-header button {
    font-size: 14px;
    padding: 10px;
  }

  .notification {
    width: 90%;
    max-width: 400px;
    white-space: normal;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .login-wrapper {
    padding: 12px;
  }

  .glass-effect {
    padding: 20px;
  }

  h1 {
    font-size: 24px;
    margin-bottom: 24px;
  }

  .tab-header {
    gap: 8px;
    margin-bottom: 24px;
  }

  .login-input {
    height: 44px;
    font-size: 14px;
  }

  .login-button, .scan-button {
    height: 44px;
    font-size: 14px;
  }
}

.notification {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 12px;
  color: white;
  z-index: 1000;
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: slideDown 0.3s ease;
  white-space: nowrap; /* 防止文字换行 */
  font-size: 15px; /* 调整字体大小 */
}

.notification-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.loading-dots {
  display: flex;
  gap: 4px;
}

.loading-dots span {
  width: 4px;
  height: 4px;
  background-color: white;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.loading-dots span:nth-child(1) { animation-delay: 0s; }
.loading-dots span:nth-child(2) { animation-delay: 0.2s; }
.loading-dots span:nth-child(3) { animation-delay: 0.4s; }

@keyframes slideDown {
  from {
    transform: translate(-50%, -20px);
    opacity: 0;
  }
  to {
    transform: translate(-50%, 0);
    opacity: 1;
  }
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.scan-button:disabled {
  opacity: 0.7;
  cursor: wait;
}

.scan-button {
  transition: all 0.3s ease, opacity 0.3s ease;
}

.scan-button:disabled:hover {
  transform: none;
  box-shadow: none;
}
</style> 