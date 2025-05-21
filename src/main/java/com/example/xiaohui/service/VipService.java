package com.example.xiaohui.service;

import com.example.xiaohui.entity.VipUserLoginDTO;
import com.example.xiaohui.mapper.VipMapper;
import org.springframework.stereotype.Service;

@Service
public class VipService {
    private final VipMapper vipMapper;

    public VipService(VipMapper vipMapper) {
        this.vipMapper = vipMapper;
    }

    public VipUserLoginDTO getUsername(String name) {
        return vipMapper.findByVipname(name);
    }

    public int updateName(String oldName, String newName) {
        return vipMapper.updateName(oldName, newName);
    }

}
