package com.team4.toucheese.auth.jwt;

import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JpaUserDetailsManager userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException{

        //요청 해더에서 JWT 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);   //"Bearer" 이휴의 토큰

            //토큰이 유효한지 확인 및 사용자 정보 추출
            String email = jwtUtil.validateToken(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                //UserDetailService를 통해 사용자 정보 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                //유효한 경우 SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}
