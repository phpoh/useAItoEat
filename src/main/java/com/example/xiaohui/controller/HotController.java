package com.example.xiaohui.controller;

import com.example.xiaohui.service.DouyinHotlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hot")
public class HotController {
    @Autowired
    private DouyinHotlistService douyinHotlistService;

    @GetMapping("/douyin")
    public List<Map<String, Object>> getDouyinHotlist() {
        return douyinHotlistService.getDouyinHotlist();
    }

}
