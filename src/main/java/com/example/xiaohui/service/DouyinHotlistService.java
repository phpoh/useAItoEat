package com.example.xiaohui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DouyinHotlistService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取抖音热榜数据
     * @return 热榜条目列表，每个条目是一个 Map，包含 title、hot、index 等字段
     */
    public List<Map<String, Object>> getDouyinHotlist() {
        String url = "https://api.vvhan.com/api/hotlist/douyinHot";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (List<Map<String, Object>>) response.get("data");
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            // 打印错误日志或抛出业务异常
            System.err.println("获取抖音热榜失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
