package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.exception.UserException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class LoginUserUtil {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String account = authentication.getName();
        return userRepository.findByAccount(account)
            .orElseThrow(() -> new UserException("找不到目前登入的使用者"));
    }
}
