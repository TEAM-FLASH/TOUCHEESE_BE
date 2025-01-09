package com.team4.toucheese.auth.oauth2.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OAuth2RedirectController {
    @PostMapping("/user/auth/kakao/callback")
    public ResponseEntity<String> redirectKakao(
            HttpServletResponse response,
            @RequestParam("code") String code
    ){
        System.out.println("response = " + response.toString());
        return ResponseEntity.ok(code);
    }
}
