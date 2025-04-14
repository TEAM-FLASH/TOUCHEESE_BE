package com.team4.toucheese.auth.config;

//import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import com.team4.toucheese.auth.jwt.JwtFilter;
import com.team4.toucheese.auth.oauth2.service.OAuth2UserServiceImpl;
import com.team4.toucheese.auth.oauth2.utils.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserServiceImpl oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> corsConfigurationSource());
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/studio/**",
                                "/auth/**",
                                "/token/**",
                                "/user/auth/kakao/callback",
                                "/login/oauth2/code/kakao",
                                "/reservation/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/auth/my-profile",
                                "/auth/logout",
                                "/user/mypage",
                                "/user/bookmark/**",
                                "/user/mypage/**",
                                "/review/file"
                        ).authenticated()
                        .requestMatchers(
                                "/auth/register",
                                "/auth/register/check"
                        ).anonymous()
        )
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("/auth/login")
//                        .successHandler(oAuth2SuccessHandler)
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(oAuth2UserService))
//                )
                //로그인
//                .formLogin(formLogin -> formLogin.loginPage("/users/login")
//                        .defaultSuccessUrl("/users/my-profile")
//                        .failureUrl("/users/login?fail")
//                        .permitAll()
//                )
                //로그아웃
//                .logout(logout -> logout.logoutUrl("/users/logout")
//                        .logoutSuccessUrl("/users/login")
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://toucheese-flash.store", "https://www.toucheese-flash.store", "https://www.toucheeseapi.shop"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public UserDetailsManager userDetailsManager(
//            PasswordEncoder passwordEncoder
//    ) {
//        UserDetails user1 = User.withUsername("user1")
//                .password(passwordEncoder.encode("password"))
//                .build();
//        return new InMemoryUserDetailsManager(user1);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
