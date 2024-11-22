package com.example.xiaohui.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageRequest {

    @Schema(description = "用户发送的消息", example = "你好，AI")
    private String message;
}
