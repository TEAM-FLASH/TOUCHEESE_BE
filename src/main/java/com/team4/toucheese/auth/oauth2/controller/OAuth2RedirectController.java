package com.team4.toucheese.auth.oauth2.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OAuth2RedirectController {
    @GetMapping("/user/auth/kakao/callback")
    public void redirectKakao(
            HttpServletResponse response,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    )throws IOException {
        String redirectUrl = String.format(
                "https://www.toucheeseapi.shop/login/oauth2/code/kakao?code=%s&state=%s", code, state
        );
        response.sendRedirect(redirectUrl);
    }
}
