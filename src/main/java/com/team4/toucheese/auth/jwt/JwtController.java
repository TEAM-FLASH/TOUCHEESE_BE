package com.team4.toucheese.auth.jwt;

import com.team4.toucheese.auth.dto.LoginSuccessDto;
import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtController {

    private final JpaUserDetailsManager userDetailsManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // 사용자 정보 가져오기
        try {
            var userDetails = userDetailsManager.loadUserByUsername(email);

            // 패스워드 검증
            if (passwordEncoder.matches(password, userDetails.getPassword()) && userDetails.getRegistration().equals("EMAIL")) {
                // JWT 토큰 발급
                String token = jwtUtil.generateToken(userDetails.getEmail());
                LoginSuccessDto dto = new LoginSuccessDto();
                dto.setAccessToken(token);
                dto.setPhone(userDetails.getPhone());
                dto.setEmail(userDetails.getEmail());
                dto.setUsername(userDetails.getUsername());
                dto.setRegistration(userDetails.getRegistration());
                dto.setUser_id(userDetails.getId());
                return ResponseEntity.ok(dto);
            } else if ( passwordEncoder.matches(password, userDetails.getPassword()) && !userDetails.getRegistration().equals("EMAIL")) {
                return ResponseEntity.status(401).body(Map.of("error", userDetails.getRegistration() + "로 가입된 아이디 입니다"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }

    @GetMapping("/val")
    public ResponseEntity<?> validateToken(
            HttpSession session,
            @RequestParam(value = "error", required = false) String error // 쿼리 파라미터 받기
            ){
        //세선에서 JWT 가져오기
        if ("failregistration".equals(error)){
            return ResponseEntity.status(401).body(Map.of("이미 가입된 이메일 입니다 : ", session.getAttribute("error")));
        }
        String token = (String) session.getAttribute("token");
        if (token != null){
            LoginSuccessDto dto = new LoginSuccessDto();
            Optional<UserEntity> user = userRepository.findByEmail(session.getAttribute("email").toString());
            dto.setAccessToken(token);
            dto.setPhone(user.get().getPhone());
            dto.setEmail(user.get().getEmail());
            dto.setUsername(user.get().getUsername());
            dto.setRegistration(user.get().getRegistration());
            dto.setUser_id(user.get().getId());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(401).body("No token found in session!");
    }

}
