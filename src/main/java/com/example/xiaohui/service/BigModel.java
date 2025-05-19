package com.example.xiaohui.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class BigModel {

    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final String API_KEY = "5a32f0f9db044ea583a93c8ef157e451.CpCr7x5UeCRJGP6l";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String chatWithBigModel(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "glm-4");
        requestBody.put("messages", List.of(message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            // 取出 choices[0].message.content
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "解析响应失败";
        }
    }
}
