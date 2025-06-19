package com.example.demo.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.detail.UserAuthDetails;
import com.example.demo.service.UserAuthDetailsService;
import com.example.demo.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UserAuthDetailsService userAuthDetailsService;
	
	@Value("${jwt.secret}")
    private String secret;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String path = request.getServletPath();
		
		//放行登入、註冊、忘記密碼等不需要驗證的路徑
		if(path.equals("/System/login")) {
			filterChain.doFilter(request, response);
			return;
		}		
		// 從 Header 中擷取 Authorization 欄位
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String token = authHeader.substring(7);

	        try {
	            // 驗證 JWT 並取得帳號
	            String username = jwtUtil.validateTokenAndRetrieveSubject(token);
	           
	            //用userauthdetailsservice載入完整使用者
	            UserAuthDetails userDetails = (UserAuthDetails) userAuthDetailsService.loadUserByUsername(username);
	            System.out.println("Token 驗證成功，帳號：" + username); //測試用
	            System.out.println("Authorities：" + userDetails.getAuthorities()); //測試用
	            // 建立認證物件，設定至 Spring Security 上下文中
	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 這邊可加入權限
	            //設定到spring security的上下文忠
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            

	        } catch (Exception e) {
	            // token 驗證失敗時，回傳 401
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json");
	            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
	            return;
	        }
	    } else {
	        // 沒有 token 的情況
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"error\": \"Missing Authorization header\"}");
	        return;
	    }

	    // 驗證成功，繼續處理請求
	    filterChain.doFilter(request, response);
	}
}
