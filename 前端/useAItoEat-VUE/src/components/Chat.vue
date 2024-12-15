<template>
  <div class="chat-container">
    <h1>Chat Page</h1>
    <div class="chat-box">
      <input 
        v-model="message" 
        type="text" 
        placeholder="输入消息..."
        @keyup.enter="sendMessage"
      />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'Chat',
  setup() {
    const router = useRouter();
    const message = ref('');

    const sendMessage = async () => {
      if (!message.value.trim()) return;

      try {
        const token = localStorage.getItem('token');
        if (!token) {
          console.error('Token not found');
          router.push('/');
          return;
        }

        const response = await axios.post('/chat', 
          { message: message.value },
          {
            headers: {
              'Authorization': token,
              'Content-Type': 'application/json'
            }
          }
        );

        console.log('聊天响应:', response.data);
        // 处理响应...
        message.value = ''; // 清空输入框

      } catch (error) {
        console.error('发送消息失败:', error);
        if (error.response && error.response.status === 401) {
          // token 无效，跳转回登录页
          router.push('/');
        }
      }
    };

    return {
      message,
      sendMessage
    };
  }
}
</script>

<style scoped>
.chat-container {
  text-align: center;
  margin-top: 50px;
  padding: 20px;
}

.chat-box {
  max-width: 600px;
  margin: 20px auto;
  display: flex;
  gap: 10px;
}

input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 10px 20px;
  background: #4f46e5;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background: #4338ca;
}
</style>
  