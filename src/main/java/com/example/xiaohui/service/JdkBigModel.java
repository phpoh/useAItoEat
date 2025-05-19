package com.example.xiaohui.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public  class JdkBigModel {

    private final OpenAIClient client;

    public JdkBigModel() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey("5a32f0f9db044ea583a93c8ef157e451.CpCr7x5UeCRJGP6l")
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .build();
    }

    public String chat(String userMessage) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(userMessage)
                .model("glm-4")  // 替换成智谱的模型名
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        // content() 返回 Optional<String>，取出值或返回空字符串
        return completion.choices().get(0).message().content().orElse("");
    }
    // main 函数测试
    public static void main(String[] args) {
        JdkBigModel model = new JdkBigModel();
        String response = model.chat("你好，帮我写一句Java代码");
        System.out.println("模型回复：" + response);
    }
}
