package com.hurry.eat.service;

import com.hurry.eat.model.User;
import com.hurry.eat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // 使用 BCryptPasswordEncoder 的 matches() 方法验证密码是否匹配
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
