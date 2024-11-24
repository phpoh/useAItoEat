async function sendMessage() {
    const input = document.getElementById('chat-input');
    const message = input.value.trim();
    
    if (message) {
        // 显示用户消息
        addMessageToChat(message, 'user');
        input.value = '';

        // 显示加载动画
        showLoadingMessage();

        try {
            console.log('Sending message:', message);

            const response = await fetch('http://127.0.0.1:8888/api/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    message: message
                })
            });

            // 移除加载动画
            removeLoadingMessage();

            console.log('Response status:', response.status);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            console.log('API Response:', data);

            // 显示AI回复（使用打字机效果）
            if (data && data.response) {
                await typeMessage(data.response);
            } else {
                addMessageToChat('服务器响应格式不正确', 'ai');
            }

        } catch (error) {
            // 确保移除加载动画
            removeLoadingMessage();
            
            console.error('Error details:', error);
            addMessageToChat(`发生错误: ${error.message}`, 'ai');
        }
    }
}

// 打字机效果函数
async function typeMessage(message) {
    const chatMessages = document.getElementById('chat-messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message ai-message';
    chatMessages.appendChild(messageDiv);

    // 每个字符的延迟时间（毫秒）
    const CHAR_DELAY = 10;
    
    // 逐字显示文本
    for (let i = 0; i < message.length; i++) {
        messageDiv.textContent += message[i];
        chatMessages.scrollTop = chatMessages.scrollHeight;
        await new Promise(resolve => setTimeout(resolve, CHAR_DELAY));
    }
}

function addMessageToChat(message, type) {
    const chatMessages = document.getElementById('chat-messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}-message`;
    messageDiv.textContent = message;
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 添加回车键支持
document.getElementById('chat-input').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        sendMessage();
    }
});

// 显示加载动画
function showLoadingMessage() {
    const chatMessages = document.getElementById('chat-messages');
    const loadingDiv = document.createElement('div');
    loadingDiv.className = 'loading-message';
    loadingDiv.id = 'loading-message';
    loadingDiv.innerHTML = `
        正在思考中
        <div class="loading-dots">
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
        </div>
    `;
    chatMessages.appendChild(loadingDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 移除加载动画
function removeLoadingMessage() {
    const loadingMessage = document.getElementById('loading-message');
    if (loadingMessage) {
        loadingMessage.remove();
    }
} 