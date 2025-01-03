package com.team4.toucheese.auth.jwt;

import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtController {

    private final JpaUserDetailsManager userDetailsManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        // 사용자 정보 가져오기
        try {
            var userDetails = userDetailsManager.loadUserByUsername(email);

            // 패스워드 검증
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                // JWT 토큰 발급
                String token = jwtUtil.generateToken(userDetails.getEmail());
                return ResponseEntity.ok(Map.of("token", token));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }
}
