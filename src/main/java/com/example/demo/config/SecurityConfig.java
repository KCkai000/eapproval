package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors() // ➜ 配合 CorsConfig 使用
            .and()
            .csrf().disable() // ➜ 禁用 CSRF，否則 POST 會被擋
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/System/login").permitAll() // 允許這條路徑匿名訪問
                .requestMatchers("/System/leave").permitAll() // 允許這條路徑匿名訪問
                .requestMatchers("/System/*").permitAll() // 允許這條路徑匿名訪問
                .anyRequest().authenticated() // 其他的都要登入（之後你要設定 jwt 或 session）
            );
        return http.build();
    }
}
