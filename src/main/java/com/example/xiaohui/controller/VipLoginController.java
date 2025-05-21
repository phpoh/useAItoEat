package com.example.xiaohui.controller;

import com.example.xiaohui.entity.NameUpdateDTO;
import com.example.xiaohui.entity.VipUserLoginDTO;
import com.example.xiaohui.service.VipService;
import com.example.xiaohui.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/viplogin")
public class VipLoginController {
    @Autowired
    private VipService vipService;

    @PostMapping("login")
    public String login(@RequestBody VipUserLoginDTO loginDTO) {
        VipUserLoginDTO loginDTO1 = vipService.getUsername(loginDTO.getName());
        if (loginDTO1 != null && loginDTO1.getName().equals(loginDTO.getName())) {
            return JwtUtil.generateToken(loginDTO1.getName());
        }else {
            return "Invalid username or password";
        }
    }

    @PostMapping("change")
    public String change(@RequestBody NameUpdateDTO dto) {
        VipUserLoginDTO existing = vipService.getUsername(dto.getOldName());
        if (existing != null && existing.getName().equals(dto.getOldName())) {
            int rows = vipService.updateName(dto.getOldName(), dto.getNewName());
            if (rows > 0) {
                return "success";
            } else {
                return "update failed";
            }
        } else {
            return "failed";
        }
    }


}
