<template>
  <div class="app-container">
    <!-- AI 聊天窗口 -->
    <div class="chat-box">
      <div class="chat-title">🤖 AI 数字分身</div>
      <div class="chat-messages">
        <div v-for="msg in messages" :key="msg.id" class="chat-message">
          <strong>{{ msg.sender }}:</strong> {{ msg.text }}
        </div>
        <div v-if="typingTextChars.length" class="chat-message">
          <strong>数字分身:</strong>
          <span
            v-for="(char, index) in typingTextChars"
            :key="index"
            class="typing-char"
            :style="{ animationDelay: (index * 30) + 'ms' }"
          >
            {{ char }}
          </span>
        </div>
        <div v-else-if="isWaitingForReply" class="chat-message typing-indicator">
          <strong>数字分身:</strong>
          正在输入<span class="dot">.</span><span class="dot">.</span><span class="dot">.</span>
        </div>
      </div>
      <div class="chat-input">
        <input
          v-model="userInput"
          @keyup.enter="sendMessage"
          placeholder="输入消息..."
          autocomplete="off"
        />
        <button @click="sendMessage">发送</button>
      </div>
    </div>

    <!-- 热搜展示窗口 -->
    <div class="hot-box">
      <div class="hot-title">🔥 抖音热搜（{{ currentTime }}）</div>
      <div class="hot-list">
        <div v-for="item in hotList" :key="item.index" class="hot-item">
          <a :href="item.url" target="_blank">{{ item.index }}. {{ item.title }}</a>
          <span class="hot-value">🔥 {{ item.hot }}</span>
        </div>
      </div>
      <div class="hot-footer">
        <input
          v-model="hotInput"
          placeholder="输入秘密词..."
          autocomplete="off"
          @keyup.enter="triggerSurprise"
        />
        <button @click="triggerSurprise">小惊喜</button>
      </div>
      <!-- 爱心和烟花效果 -->
      <div v-if="showEffect" class="effect-container">
        <div class="heart">❤️</div>
        <div class="fireworks"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChatAndHotPage",
  data() {
    return {
      userInput: "",
      hotInput: "",
      messages: [{id: 0, text: "欢迎！", sender: "系统"}],
      nextMessageId: 1,
      isWaitingForReply: false,
      typingTextChars: [],
      hotList: [],
      currentTime: "",
      hotTimer: null,
      checkTokenInterval: null,
      showEffect: false,
    };
  },
  methods: {
    getJwtToken() {
      return localStorage.getItem("token");
    },
    checkToken() {
      const token = this.getJwtToken();
      if (!token) {
        alert("未登录，请先登录！");
      } else {
        clearInterval(this.checkTokenInterval);
        this.checkTokenInterval = null;
        this.fetchHotList();
        this.hotTimer = setInterval(this.fetchHotList, 10000);
      }
    },
    async sendMessage() {
      const text = this.userInput.trim();
      if (!text) return;
      this.messages.push({id: "user-" + this.nextMessageId++, text, sender: "我"});
      this.userInput = "";
      this.typingTextChars = [];
      this.isWaitingForReply = true;
      try {
        const token = this.getJwtToken();
        const response = await fetch("http://127.0.0.1:8888/api/chatAi", {
          method: "POST",
          headers: {"Content-Type": "application/json", Authorization: "Bearer " + token},
          body: JSON.stringify({msg: text}),
        });
        const data = await response.json();
        await this.simulateTyping(data.msg || "（无返回内容）");
        this.messages.push({
          id: "ai-" + this.nextMessageId++,
          text: this.typingTextChars.join(""),
          sender: "机器人",
        });
      } catch (err) {
        this.messages.push({id: "error-" + this.nextMessageId++, text: "发生错误：" + err.message, sender: "系统"});
      } finally {
        this.typingTextChars = [];
        this.isWaitingForReply = false;
      }
    },
    async simulateTyping(text) {
      this.typingTextChars = [];
      const totalDuration = 3000;
      const len = text.length;
      const delay = Math.max(totalDuration / len, 20);

      for (let i = 0; i < len; i++) {
        this.typingTextChars.push(text[i]);
        await new Promise((resolve) => setTimeout(resolve, delay));
      }
    },
    async fetchHotList() {
      try {
        const token = this.getJwtToken();
        const res = await fetch("http://127.0.0.1:8888/hot/douyin", {
          headers: {Authorization: "Bearer " + token},
        });
        const data = await res.json();
        this.hotList = data;
        this.updateTime();
      } catch (error) {
        console.error("获取热搜失败", error);
      }
    },
    updateTime() {
      const now = new Date();
      this.currentTime = now.toLocaleTimeString();
    },
    triggerSurprise() {
      if (this.hotInput.trim() === "凉面") {
        this.showEffect = true;
        this.playFireworkSound();
        setTimeout(() => {
          this.showEffect = false;
          this.hotInput = "";
        }, 3000);
      } else {
        const allTitles = this.hotList.map((item) => item.title).join("；");
        this.sendMessage(`请帮我总结以下抖音热搜内容：${allTitles}`);
      }
    },
    playFireworkSound() {
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
    },
  },
  mounted() {
    this.checkTokenInterval = setInterval(this.checkToken, 3000);
  },
  beforeDestroy() {
    if (this.checkTokenInterval) clearInterval(this.checkTokenInterval);
    if (this.hotTimer) clearInterval(this.hotTimer);
  },
};
</script>

<style scoped>
/* 保持原有样式不变 */
.app-container {
  display: flex;
  height: 100vh;
  padding: 10px;
  gap: 10px;
  background: linear-gradient(
    -45deg,
    #0f1117,
    #1a1c23,
    #12141b,
    #181a22
  );
  background-size: 400% 400%;
  animation: gradientBG 25s ease infinite;
  color: #e6e8ea;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue",
  sans-serif;
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

.chat-box,
.hot-box {
  flex: 1;
  background: rgba(30, 32, 40, 0.85);
  backdrop-filter: blur(15px);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.8);
  display: flex;
  flex-direction: column;
}

.chat-title,
.hot-title {
  font-size: 20px;
  font-weight: 600;
  color: #c1c3c6;
  margin-bottom: 16px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 10px;
  color: #e6e8ea;
  font-size: 16px;
}

.chat-message {
  margin: 6px 0;
  line-height: 1.4;
  user-select: text;
}

.chat-message strong {
  color: #8ab4f8;
}

.chat-input {
  display: flex;
  gap: 12px;
}

.chat-input input {
  flex: 1;
  padding: 10px 14px;
  font-size: 16px;
  border-radius: 12px;
  border: none;
  background: rgba(255 255 255 / 0.1);
  color: #e6e8ea;
  outline: none;
  transition: background 0.3s ease;
}

.chat-input input::placeholder {
  color: #a1a3a6;
}

.chat-input input:focus {
  background: rgba(255 255 255 / 0.15);
}

button {
  padding: 10px 20px;
  background: linear-gradient(145deg, #5a78ff, #2a3eff);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 4px 14px rgba(42, 62, 255, 0.6);
  transition: background 0.3s ease;
}

button:hover {
  background: linear-gradient(145deg, #7b94ff, #405cff);
}

.typing-indicator .dot {
  animation: blink 1.2s infinite;
  display: inline-block;
  width: 6px;
  height: 6px;
  margin-left: 4px;
  background-color: #5a78ff;
  border-radius: 50%;
}

.typing-indicator .dot:nth-child(2) {
  animation-delay: 0.3s;
}

.typing-indicator .dot:nth-child(3) {
  animation-delay: 0.6s;
}

.typing-char {
  background: linear-gradient(90deg, #6a85ff, #4361ee);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  opacity: 0;
  display: inline-block;
  animation: fadeInGradient 0.3s ease forwards;
}

@keyframes fadeInGradient {
  to {
    opacity: 1;
  }
}

.hot-list {
  flex: 1;
  overflow-y: auto;
  font-size: 15px;
  color: #d0d3d7;
}

.hot-item {
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hot-item a {
  color: #aab0b8;
  text-decoration: none;
  transition: color 0.25s ease;
}

.hot-item a:hover {
  color: #6a85ff;
}

.hot-value {
  font-size: 14px;
  color: #ff6f61;
}

.hot-footer {
  margin-top: 14px;
  text-align: center;
  display: flex;
  gap: 12px;
}

.hot-footer input {
  flex: 1;
  padding: 10px 14px;
  font-size: 16px;
  border-radius: 12px;
  border: none;
  background: rgba(255 255 255 / 0.1);
  color: #e6e8ea;
  outline: none;
  transition: background 0.3s ease;
}

.hot-footer input::placeholder {
  color: #a1a3a6;
}

.hot-footer input:focus {
  background: rgba(255 255 255 / 0.15);
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

/* 响应式 - 手机端适配 */
@media screen and (max-width: 768px) {
  .app-container {
    flex-direction: column;
    height: auto;
  }

  .chat-box,
  .hot-box {
    width: 100%;
    margin-bottom: 12px;
  }

  .chat-input {
    flex-direction: column;
  }

  .chat-input input,
  .chat-input button {
    width: 100%;
  }

  .hot-footer {
    flex-direction: column;
  }

  .hot-footer input,
  .hot-footer button {
    width: 100%;
  }
}

@keyframes blink {
  0%,
  20% {
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
