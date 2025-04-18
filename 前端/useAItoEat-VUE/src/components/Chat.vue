<template>
  <div class="chat-container">
    <!-- Chat messages -->
    <div ref="messageContainer" class="chat-messages" @scroll="checkIfScrolledToBottom">
      <div v-for="msg in messages" :key="msg.id" :class="['message', msg.sender === 'user' ? 'user' : 'system']">
        <div class="message-content">{{ msg.text }}</div>
      </div>

      <!-- 等待动画 -->
      <div v-if="isWaitingForReply" class="waiting-animation">
        <span class="dot">.</span>
        <span class="dot">.</span>
        <span class="dot">.</span>
      </div>
    </div>

    <!-- Scroll Down Arrow -->
    <div v-if="!isScrolledToBottom" class="scroll-down-arrow" @click="scrollToBottom">
      <font-awesome-icon icon="arrow-down" />
    </div>

    <!-- Input area -->
    <div class="chat-input-area">
      <div v-if="!quickActionSelected" class="quick-actions">
        <button v-for="action in quickActions" :key="action.id" @click="handleQuickAction(action)" class="quick-action-button">
          <span class="icon">{{ action.icon }}</span>
          <span>{{ action.text }}</span>
        </button>
      </div>

      <div v-else class="input-section">
        <textarea
          v-model="message"
          placeholder="请输入您的消息..."
          class="message-input"
          rows="1"
        />
        <button @click="handleSend()" class="send-button">
          <font-awesome-icon icon="paper-plane" />
        </button>
      </div>
    </div>

    <!-- Snowflakes -->
    <div class="snowflakes">
      <div v-for="n in 30" :key="n" class="snowflake">❄️</div>
    </div>
  </div>
</template>

<script>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faPaperPlane, faArrowDown } from '@fortawesome/free-solid-svg-icons';

import OpenAI from "openai";


library.add(faPaperPlane, faArrowDown);

export default {
  name: 'ChristmasChatInterface',
  components: {
    FontAwesomeIcon
  },


  data() {
    return {
      message: '',
      messages: [
        { id: 1, text: "🎄 圣诞快乐! 欢迎来到节日聊天室 ⛄", sender: 'system' },
      ],
      quickActions: [
        { id: 1, text: "想吃火锅", icon: "🍲" },
        { id: 2, text: "想吃羊肉", icon: "🥩" },
        { id: 3, text: "想吃饺子", icon: "🥟" },
        { id: 4, text: "想吃烧烤", icon: "🍖" }
      ],
      quickActionSelected: false,  // 是否选择了快捷消息
      isScrolledToBottom: true,  // 用来判断是否已经滚动到底部
      isWaitingForReply: false, // 是否等待系统回复
    };
  },

  methods: {
    handleQuickAction(action) {
      // 选择快捷消息并发送
      this.message = action.text;  // 设置消息为选择的快捷消息
      this.quickActionSelected = true;  // 标记快捷消息已被选中
      this.handleSend(action.text);  // 发送消息
    },

    handleSend(text = this.message) {
      if (!text.trim()) return;

      // 用户发送的消息
      this.messages.push({
        id: this.messages.length + 1,
        text: text,
        sender: 'user'
      });

      // 清空消息框
      this.message = '';

      // 显示等待动画
      this.isWaitingForReply = true;

      // 模拟系统回复
      setTimeout(() => {
        // 隐藏等待动画
        this.isWaitingForReply = false;

        // 系统回复消息
        this.messages.push({
          id: this.messages.length + 1,
          text: '收到你的消息了！🎅',
          sender: 'system'
        });

        // 滚动到最新的消息
        this.scrollToBottom();
      }, 2000); // 模拟2秒后系统回复
    },

    // 滚动到消息的底部
    scrollToBottom() {
      const container = this.$refs.messageContainer;
      this.$nextTick(() => {
        container.scrollTop = container.scrollHeight;
        this.isScrolledToBottom = true; // 滚动到最底部时，更新状态
      });
    },

    // 检查是否已经滚动到底部
    checkIfScrolledToBottom() {
      const container = this.$refs.messageContainer;
      this.isScrolledToBottom = container.scrollHeight - container.scrollTop === container.clientHeight;
    }
  },

  // 在组件挂载时也确保滚动到底部
  mounted() {
    this.scrollToBottom();
  },

  // 在消息数据变化时，自动滚动到最新的消息
  watch: {
    messages: function () {
      this.scrollToBottom();
    }
  }
};
</script>

<style scoped>
/* 基本布局设置 */
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(45deg, red, yellow, green, blue, purple);
  background-size: 400% 400%;
  animation: gradientBackground 10s ease infinite; /* 背景渐变动画 */
  font-family: 'Helvetica Neue', Arial, sans-serif;
  overflow: hidden;
  position: relative;
}

@keyframes gradientBackground {
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

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background: rgba(255, 255, 255, 0.7); /* 半透明背景 */
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
}

.message {
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message.system {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 12px 18px;
  border-radius: 12px;
  font-size: 1rem;
  background-color: #f2f2f7;
  color: #333;
}

.message.user .message-content {
  background-color: #007aff;
  color: white;
}

.message.system .message-content {
  background-color: #f1f1f6;
}

.chat-input-area {
  padding: 10px 20px;
  background-color: #ffffff;
  border-top: 1px solid #e5e5e5;
  box-shadow: inset 0 -2px 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.quick-actions {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}

.quick-action-button {
  background-color: #f0f0f5;
  color: #007aff;
  padding: 8px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  border: none;
}

.quick-action-button:hover {
  background-color: #007aff;
  color: white;
}

.input-section {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.message-input {
  flex: 1;
  padding: 10px;
  border-radius: 18px;
  border: 1px solid #007aff;
  background-color: #f8f8f9;
  color: #333;
  font-size: 1rem;
  resize: none;
  transition: border-color 0.3s ease;
  min-height: 40px; /* 控制输入框高度 */
}

.message-input:focus {
  border-color: #007aff;
  outline: none;
}

.send-button {
  background-color: #007aff;
  color: white;
  font-size: 1.4rem;
  padding: 10px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  transition: transform 0.2s ease;
  width: 44px;  /* 控制按钮的大小 */
  height: 44px;
}

.send-button:hover {
  transform: scale(1.1);
}

.scroll-down-arrow {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 50%;
  padding: 8px;
  cursor: pointer;
}

.snowflakes {
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
}

.snowflake {
  position: absolute;
  top: -10px;
  font-size: 24px;
  animation: snowflake 10s linear infinite;
}

.snowflake:nth-child(odd) {
  animation-duration: 12s;
}

@keyframes snowflake {
  0% {
    transform: translateX(0) translateY(0);
  }
  100% {
    transform: translateX(30px) translateY(100vh);
  }
}

.waiting-animation {
  display: flex;
  justify-content: center;
  gap: 5px;
  font-size: 24px;
  color: #007aff;
}

.waiting-animation .dot {
  animation: waitingDot 1s infinite step-start;
}

.waiting-animation .dot:nth-child(1) {
  animation-delay: 0s;
}

.waiting-animation .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.waiting-animation .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes waitingDot {
  0% {
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
