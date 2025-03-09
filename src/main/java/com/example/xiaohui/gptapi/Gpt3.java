package com.example.xiaohui.gptapi;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Gpt3 {
    private static final Logger logger = LoggerFactory.getLogger(Gpt3.class);
    private static final Random random = new Random();
    private static final Generation gen = new Generation();
    private static final List<String> conversationHistory = new ArrayList<>();

    private static final List<String> EMOTIONS = Arrays.asList("兴奋", "期待", "紧张", "意外", "沉稳");
    private static final String BOY_PERSONALITY = "激进派未来主义者，崇拜马斯克，喜欢大胆畅想，情绪化，倾向乐观预测";
    private static final String GIRL_PERSONALITY = "理性派科学家，注重技术细节和可行性，冷静，倾向审慎分析";
    private static final String BOY_API_KEY = "sk-b852cc941f504c83965ddadeb6f70e1f";
    private static final String GIRL_API_KEY = "sk-cbda6012669a412283a6746a9266e74d";

    private static final String RESET = "\u001B[0m";
    private static final String BOY_COLOR = "\u001B[31m"; // 红色
    private static final String GIRL_COLOR = "\u001B[34m"; // 蓝色
    private static final String BOY_CONTENT_COLOR = "\u001B[33m"; // 黄色
    private static final String GIRL_CONTENT_COLOR = "\u001B[36m"; // 青色

    private static final String STARSHIP_CONTEXT = "以下是马斯克星舰（Starship）与火星移民的关键背景：星舰是SpaceX研发的可重复使用航天器，" +
            "旨在将人类送往火星，实现殖民化目标。截至2025年3月9日，星舰已多次试飞，计划2030年代大规模移民火星。讨论基于技术进展与愿景。";

    private static void generateWithEmotionStream(String prompt, String speaker) {
        String emotion = EMOTIONS.get(random.nextInt(EMOTIONS.size()));
        String history = String.join("\n", conversationHistory);
        String fullPrompt = "历史对话：\n" + (history.isEmpty() ? "无" : history) + "\n\n" + prompt + " 情感：" + emotion +
                " 注意：回答需避免不当或敏感内容，保持客观和专业。";

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(fullPrompt)
                .build();

        String apiKey = speaker.equals("男未来主义者") ? BOY_API_KEY : GIRL_API_KEY;
        GenerationParam param = buildGenerationParam(userMsg, apiKey);
        String speakerColor = speaker.equals("男未来主义者") ? BOY_COLOR : GIRL_COLOR;
        String contentColor = speaker.equals("男未来主义者") ? BOY_CONTENT_COLOR : GIRL_CONTENT_COLOR;

        try {
            Flowable<GenerationResult> result = gen.streamCall(param);
            System.out.print(speakerColor + speaker + "：" + RESET);
            StringBuilder response = new StringBuilder();
            result.blockingForEach(message -> handleGenerationResult(message, contentColor, response));
            System.out.println(RESET);
            conversationHistory.add(speaker + "：" + response.toString());
        } catch (ApiException e) {
            logger.error("生成失败 for {}: {}", speaker, e.getMessage());
            System.out.println(speakerColor + speaker + "：" + RESET + contentColor + "抱歉，此轮生成失败，可能涉及敏感内容。" + RESET);
        } catch (NoApiKeyException | InputRequiredException e) {
            logger.error("API配置错误 for {}: {}", speaker, e.getMessage());
            System.out.println(speakerColor + speaker + "：" + RESET + contentColor + "配置错误，无法生成。" + RESET);
        }
    }

    private static void handleGenerationResult(GenerationResult message, String contentColor, StringBuilder response) {
        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
        if (!content.isEmpty()) {
            StringBuilder lineBuilder = new StringBuilder();
            int charCount = 0;

            for (char c : content.toCharArray()) {
                lineBuilder.append(c);
                charCount++;

                if (charCount >= 50 && (c == ' ' || c == '，' || c == '。' || c == '！' || c == '？')) {
                    System.out.print(contentColor + lineBuilder.toString() + RESET + "\n" + contentColor);
                    response.append(lineBuilder.toString());
                    lineBuilder.setLength(0);
                    charCount = 0;
                }
            }

            if (lineBuilder.length() > 0) {
                System.out.print(contentColor + lineBuilder.toString());
                response.append(lineBuilder.toString());
            }
            System.out.flush();
        }
    }

    private static GenerationParam buildGenerationParam(Message userMsg, String apiKey) {
        return GenerationParam.builder()
                .apiKey(apiKey)
                .model("deepseek-v3")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
    }

    public static void main(String[] args) {
        try {
            System.out.println("\n=== 讨论主题：马斯克星舰与火星移民 ===\n" + STARSHIP_CONTEXT + "\n");

            String boyPromptBase = "你是男生，未来主义者，个性：" + BOY_PERSONALITY + "，根据星舰与火星移民背景：" + STARSHIP_CONTEXT +
                    "，结合当前讨论轮次，畅想火星移民前景或星舰突破，观点独特乐观。回答控制在50字左右，像人与人聊天，避免重复。";
            String girlPromptBase = "你是女生，科学家，个性：" + GIRL_PERSONALITY + "，根据星舰与火星移民背景：" + STARSHIP_CONTEXT +
                    "，回应男未来主义者，分析技术可行性或挑战，理性客观。回答控制在50字左右，像人与人聊天，避免重复。";

            int round = 1;
            while (true) {
                String boyPrompt = boyPromptBase + " 当前是第 " + round + " 轮讨论。";
                String girlPrompt = girlPromptBase + " 当前是第 " + round + " 轮讨论。";

                generateWithEmotionStream(boyPrompt, "男未来主义者");
                generateWithEmotionStream(girlPrompt, "女科学家");

                System.out.println();
                Thread.sleep(50);
                round++;
            }

        } catch (InterruptedException e) {
            logger.error("线程睡眠中断: {}", e.getMessage());
        }
    }
}