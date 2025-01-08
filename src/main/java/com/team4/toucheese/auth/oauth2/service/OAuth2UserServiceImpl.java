package com.team4.toucheese.auth.oauth2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //어떤 서비스 제공자를 사용했는지
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        //OAuth2 제공자로 부터 받은 데이터를 원하는 방식으로 다시 정리하기 위한 Map
        Map<String, Object> attributes = new HashMap<>();
        String nameAttribute = "";

        // 네이버로 부터 받은 데이터를 아래처럼 활용
        if (registrationId.equals("naver")) {
            log.info(oAuth2User.getAttributes().toString());
            //Naver에서 받아온 정보
            Map<String, Object> responseMap
                    = oAuth2User.getAttribute("response");
            attributes.put("registrationId", registrationId);
            attributes.put("id", responseMap.get("id"));
            attributes.put("email", responseMap.get("email"));
            attributes.put("phone", responseMap.get("mobile"));
            attributes.put("name", responseMap.get("name"));
            nameAttribute = "email";
        } else if (registrationId.equals("kakao")) {
            log.info(oAuth2User.getAttributes().toString());
            //Kakao에서 받아온 정보
            attributes.put("id", oAuth2User.getAttributes().get("id"));
            Map<String, Object> responseMap
                    = oAuth2User.getAttribute("kakao_account");
            attributes.put("registrationId", registrationId);
            attributes.put("email", responseMap.get("email"));
            attributes.put("phone", responseMap.get("phone_number"));
            attributes.put("name", responseMap.get("name"));
            nameAttribute = "email";
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                nameAttribute
        );
    }
}
