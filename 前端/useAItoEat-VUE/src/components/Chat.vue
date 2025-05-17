<template>
  <div>
    <div id="message-display">
      <div v-for="msg in messages" :key="msg.id">
        <strong>{{ msg.sender }}:</strong> {{ msg.text }}
      </div>
    </div>

    <div v-if="isWaitingForReply">
      <p>等待回复中...</p>
    </div>
    <div>
      <input
        type="text"
        v-model="userInput"
        placeholder="输入消息..."
        @keyup.enter="sendMessage"
      />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UltraSimpleChat',
  data() {
    return {
      userInput: '',
      messages: [
        { id: Date.now(), text: '欢迎!', sender: '系统' }
      ],
      nextMessageId: 1,
      isWaitingForReply: false
    };
  },
  methods: {
    // 模拟从本地获取 JWT Token
    getJwtToken() {
      // 示例：从 localStorage 获取 token，视你的项目而定
      return localStorage.getItem('token');
    },

    async sendMessage() {
      const text = this.userInput.trim();
      if (!text) return;

      // 显示用户消息
      this.messages.push({
        id: 'user-' + this.nextMessageId++,
        text: text,
        sender: '我'
      });

      this.userInput = '';
      this.isWaitingForReply = true;

      try {
        const token = this.getJwtToken();
        if (!token) {
          throw new Error("JWT Token 未找到！");
        }

        const response = await fetch('http://127.0.0.1:8888/api/chatAi', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
          },
          body: JSON.stringify({ msg: text })
        });

        if (!response.ok) {
          throw new Error('请求失败，状态码：' + response.status);
        }

        const data = await response.json();

        // 添加服务器返回的消息
        this.messages.push({
          id: 'system-' + this.nextMessageId++,
          text: data.msg,
          sender: '机器人'
        });

      } catch (error) {
        console.error('请求出错：', error);
        this.messages.push({
          id: 'error-' + this.nextMessageId++,
          text: '发生错误：' + error.message,
          sender: '系统'
        });
      } finally {
        this.isWaitingForReply = false;
      }
    }
  },
  mounted() {
    if (this.messages.length > 0) {
      this.nextMessageId = this.messages.length + 1;
    }
  }
};
</script>
