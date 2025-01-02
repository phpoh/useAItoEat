package com.example.xiaohui.data;


import com.example.xiaohui.entity.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserData {

    //0为离线1为在线
    public static List<User> USER_LIST = Arrays.asList(
            new User("1", "zhihui", "xiaohui123", "0"),
            new User("2", "zhihui1", "xiaohui123", "0"),
            new User("3", "zhihui2", "xiaohui123", "0")
    );
}
