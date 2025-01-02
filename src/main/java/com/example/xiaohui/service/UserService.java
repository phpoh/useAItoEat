package com.example.xiaohui.service;

import com.example.xiaohui.data.UserData;
import com.example.xiaohui.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserData userData;


    public List<User> getAllUsers(){
        return userData.USER_LIST;
    }
}
