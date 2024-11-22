package com.example.xiaohui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DouyinHotlistService {

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000)  // 每分钟执行一次
    public void fetchDouyinHotlist() {
        String url = "https://api.vvhan.com/api/hotlist/douyinHot";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);  // 使用 getForObject 直接获取响应数据

        if (response != null && (Boolean) response.get("success")) {
            List<Map> hotlist = (List<Map>) response.get("data");
            for (Map hot : hotlist) {
                String title = (String) hot.get("title");
                String hotValue = (String) hot.get("hot");
                Integer index = (Integer) hot.get("index");
                // 这里只是输出数据，你可以选择做其他处理
                System.out.println("热搜标题: " + title + ", 热搜内容: " + hotValue + ", 热度排名: " + index);
            }
        }
    }



}

