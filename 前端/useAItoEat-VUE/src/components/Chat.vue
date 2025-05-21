<template>
  <div class="app-container">
    <!-- AI 聊天窗口 -->
    <div class="chat-box">
      <div class="chat-title">🤖 AI 聊天</div>
      <div class="chat-messages">
        <div v-for="msg in messages" :key="msg.id" class="chat-message">
          <strong>{{ msg.sender }}:</strong> {{ msg.text }}
        </div>
        <!-- 打字输出动画 -->
        <div v-if="typingText" class="chat-message">
          <strong>机器人:</strong> {{ typingText }}
        </div>
        <!-- 等待中动画 -->
        <div v-else-if="isWaitingForReply" class="chat-message typing-indicator">
          <strong>机器人:</strong>
          正在输入<span class="dot">.</span><span class="dot">.</span><span class="dot">.</span>
        </div>
      </div>
      <div class="chat-input">
        <input
          v-model="userInput"
          @keyup.enter="sendMessage"
          placeholder="输入消息..."
        />
        <button @click="sendMessage">发送</button>
      </div>
    </div>

    <!-- 热搜展示窗口 -->
    <div class="hot-box">
      <div class="hot-title">
        🔥 抖音热搜（{{ currentTime }}）
      </div>
      <div class="hot-list">
        <div v-for="item in hotList" :key="item.index" class="hot-item">
          <a :href="item.url" target="_blank">{{ item.index }}. {{ item.title }}</a>
          <span class="hot-value">🔥 {{ item.hot }}</span>
        </div>
      </div>
      <div class="hot-footer">
        <button @click="summarizeHot">AI 总结热搜</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatAndHotPage',
  data() {
    return {
      userInput: '',
      messages: [{ id: 0, text: '欢迎！', sender: '系统' }],
      nextMessageId: 1,
      isWaitingForReply: false,
      typingText: '',
      hotList: [],
      currentTime: '',
      hotTimer: null,
      checkTokenInterval: null, // 用于定时检查token
    };
  },
  methods: {
    getJwtToken() {
      return localStorage.getItem('token');
    },

    checkToken() {
      const token = this.getJwtToken();
      if (!token) {
        alert('未登录，请先登录！');
      } else {
        // 有token，停止继续弹窗，开始正常功能
        clearInterval(this.checkTokenInterval);
        this.checkTokenInterval = null;

        // 开始拉取热搜数据定时器
        this.fetchHotList();
        this.hotTimer = setInterval(this.fetchHotList, 10000);
      }
    },

    async sendMessage() {
      const text = this.userInput.trim();
      if (!text) return;

      this.messages.push({
        id: 'user-' + this.nextMessageId++,
        text: text,
        sender: '我'
      });

      this.userInput = '';
      this.typingText = '';
      this.isWaitingForReply = true;

      try {
        const token = this.getJwtToken();
        const response = await fetch('http://127.0.0.1:8888/api/chatAi', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + token
          },
          body: JSON.stringify({msg: text})
        });

        const data = await response.json();
        await this.simulateTyping(data.msg || '（无返回内容）');

        this.messages.push({
          id: 'ai-' + this.nextMessageId++,
          text: this.typingText,
          sender: '机器人'
        });

      } catch (err) {
        this.messages.push({
          id: 'error-' + this.nextMessageId++,
          text: '发生错误：' + err.message,
          sender: '系统'
        });
      } finally {
        this.typingText = '';
        this.isWaitingForReply = false;
      }
    },

    async simulateTyping(text) {
      this.typingText = '';
      for (let i = 0; i < text.length; i++) {
        this.typingText += text[i];
        await new Promise(resolve => setTimeout(resolve, 30));
      }
    },

    async fetchHotList() {
      try {
        const token = this.getJwtToken();
        const res = await fetch('http://127.0.0.1:8888/hot/douyin', {
          headers: {
            Authorization: 'Bearer ' + token
          }
        });
        const data = await res.json();
        this.hotList = data;
        this.updateTime();
      } catch (error) {
        console.error('获取热搜失败', error);
      }
    },

    updateTime() {
      const now = new Date();
      this.currentTime = now.toLocaleTimeString();
    },

    summarizeHot() {
      const allTitles = this.hotList.map(item => item.title).join('；');
      this.sendMessage(`请帮我总结以下抖音热搜内容：${allTitles}`);
    }
  },
  mounted() {
    // 页面加载后，每3秒检查一次token
    this.checkTokenInterval = setInterval(this.checkToken, 3000);
  },
  beforeDestroy() {
    if (this.checkTokenInterval) {
      clearInterval(this.checkTokenInterval);
    }
    if (this.hotTimer) {
      clearInterval(this.hotTimer);
    }
  }
};
</script>

<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  padding: 10px;
  background-color: #f5f5f5;
  gap: 10px;
}

.chat-box, .hot-box {
  flex: 1;
  background: #fff;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.chat-title, .hot-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 10px;
}

.chat-message {
  margin: 5px 0;
}

.chat-input {
  display: flex;
  gap: 10px;
}

.chat-input input {
  flex: 1;
  padding: 8px;
}

.hot-list {
  flex: 1;
  overflow-y: auto;
}

.hot-item {
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hot-item a {
  color: #333;
  text-decoration: none;
}

.hot-value {
  font-size: 14px;
  color: #f40;
}

.hot-footer {
  margin-top: 10px;
  text-align: center;
}

button {
  padding: 6px 12px;
  cursor: pointer;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 5px;
}

button:hover {
  background-color: #66b1ff;
}

/* 等待动画点点效果 */
.typing-indicator .dot {
  animation: blink 1s infinite;
  display: inline-block;
  width: 4px;
  margin-left: 2px;
}

.typing-indicator .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%, 20% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
</style>
