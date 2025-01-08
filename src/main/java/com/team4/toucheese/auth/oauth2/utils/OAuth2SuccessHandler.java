package com.team4.toucheese.auth.oauth2.utils;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.auth.jwt.JwtUtil;
import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil tokenUtils;
    private final UserRepository userRepository;
    private final JpaUserDetailsManager jpaUserDetailsManager;

    public OAuth2SuccessHandler(JwtUtil tokenUtils, UserRepository userRepository, JpaUserDetailsManager jpaUserDetailsManager) {
        this.tokenUtils = tokenUtils;
        this.userRepository = userRepository;
        this.jpaUserDetailsManager = jpaUserDetailsManager;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User
                = (OAuth2User) authentication.getPrincipal();

        //유저 정보
        String username = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String phone = oAuth2User.getAttribute("phone");
        String registrationId = oAuth2User.getAttribute("registrationId");

        //유저가 가입한적이 없다면 가입
        if (!userRepository.existsByEmail(email)){

            jpaUserDetailsManager.createUser(
                    CustomUserDetails.builder()
                            .username(username)
                            .email(email)
                            .phone(phone)
                            .password("")
                            .registration(registrationId)
                            .build()
            );
        }
        if (userRepository.existsByEmail(email)
                && userRepository.findByEmail(email).get().getRegistration().equals(registrationId)){

            String jwt
                    = tokenUtils
                    .generateToken(String.valueOf(User.withUsername(oAuth2User.getName())
                            .password(oAuth2User.getAttribute("id").toString())
                            .build()));

            //세션에 JWT 저장
            request.getSession().setAttribute("token", jwt);
            //세션에 Email 저장
            request.getSession().setAttribute("email", email);

            String targetUrl =
                    "http://localhost:8080/auth/val";
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        }else{
            String targetUrl =
                    "http://localhost:8080/auth/val?error=failregistration";
            request.getSession().setAttribute("error", userRepository.findByEmail(email).get().getRegistration());
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        };


    }
}