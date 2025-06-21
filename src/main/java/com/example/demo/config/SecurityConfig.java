package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	}
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
        	.cors(cors -> {}) //允許跨域，可加 CORS config bean
        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 禁用 Session
        	.authorizeHttpRequests(auth -> auth
				.requestMatchers("/System/login").permitAll() // 允許登入和 OPTIONS 請求
				.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll() // 允許登入和 OPTIONS 請求
				.requestMatchers("/leave/create","/leave/my-forms").hasAnyRole("staff", "manager", "HR", "admin") // 允許請假
				.requestMatchers("/leave/review/**").hasAnyRole("manager", "HR", "admin") // 允許審核請求	
				.anyRequest().authenticated() // 其他請求需要認證
			);
        
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加 JWT 認證過濾器
            
		
        return http.build();
    }
}
