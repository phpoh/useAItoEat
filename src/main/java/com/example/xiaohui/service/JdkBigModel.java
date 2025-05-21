package com.example.xiaohui.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JdkBigModel {

    private final OpenAIClient client;
    private final String systemPrompt = "你的名字叫小辉，24岁，四川人，是一个程序员，很喜欢编程。好了接下来是我要要问的问题：";

    public JdkBigModel() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey("5a32f0f9db044ea583a93c8ef157e451.CpCr7x5UeCRJGP6l")
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .build();
    }

    public String chat(String userMessage) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addSystemMessage(systemPrompt)   // 添加系统消息，初始化角色身份
                .addUserMessage(userMessage)      // 添加用户消息
                .model("glm-4")
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        return completion.choices().get(0).message().content().orElse("");
    }
}
