package com.team4.toucheese.auth.oauth2.controller;

import com.team4.toucheese.auth.dto.KakaoCode;
import com.team4.toucheese.auth.dto.LoginSuccessDto;
import com.team4.toucheese.auth.dto.RegisterDto;
import com.team4.toucheese.auth.jwt.JwtUtil;
import com.team4.toucheese.auth.oauth2.service.KakaoApi;
import com.team4.toucheese.auth.service.JpaUserDetailsManager;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OAuth2RedirectController {

    private final KakaoApi kakaoApi;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JpaUserDetailsManager userDetailsManager;

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoCallback(@RequestParam("code") String code, HttpServletRequest request) throws IOException {
        return code;
    }

    @PostMapping("/user/auth/kakao/callback")
    public ResponseEntity<?> redirectKakao(
            @RequestBody KakaoCode kakaoCode
    ){
        try {
            String code = kakaoCode.getCode();
            //받은 코드로 토큰을 받는다
            String accessToken = kakaoApi.getAccessToken(code);
            System.out.println("accessToken = " + accessToken);
            //사용자 정보를 받는다
            Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

            //회원가입이 되어있지 않으면 회원가입
            if (!userRepository.existsByEmail(userInfo.get("email").toString())) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setStatus("가입되지 않은 이메일 입니다");
                registerDto.setR_email(userInfo.get("email").toString());
//                registerDto.setR_username(userInfo.get("name").toString());
                registerDto.setR_registration("kakao");
                registerDto.setR_password(kakaoApi.makePassword());
                return ResponseEntity.status(404).body(registerDto);
            } else {
                //가입이 되어 있다면 회원 정보 불러오기
                Optional<UserEntity> user = userRepository.findByEmail(userInfo.get("email").toString());

                //회원가입이 <카카오>로되어있으면 토큰 발행 후 로그인
                if (userRepository.existsByEmail(user.get().getEmail()) && user.get().getRegistration().equals("kakao")) {
                    String token = jwtUtil.generateToken(user.get().getEmail());
                    LoginSuccessDto dto = new LoginSuccessDto();
                    dto.setAccessToken(token);
                    dto.setPhone(user.get().getPhone());
                    dto.setEmail(user.get().getEmail());
                    dto.setUsername(user.get().getUsername());
                    dto.setRegistration(user.get().getRegistration());
                    dto.setUser_id(user.get().getId());
                    return ResponseEntity.ok(dto);
                } else if (userRepository.existsByEmail(user.get().getEmail()) && !user.get().getRegistration().equals("kakao")) {
                    return ResponseEntity.status(401).body(String.format("%s로 가입된 이메일 입니다", user.get().getRegistration()));
                }
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.status(500).body("로그인이 완료되지 못했습니다 다시 시도해주세요");
    }

}
