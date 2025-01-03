package com.team4.toucheese.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private static final long EXPIRATION_TIME = 3600000;    //1시간

    public JwtUtil(
            @Value("${jwt.secret}")
            String jwtSecret
    ){
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //JWT 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //JWT 검증 및 이메일 추출
    public String validateToken(String token) {
        try{
            Claims claims = Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(token).getBody();
            return claims.getSubject(); // 토큰에서 email 반환
        }catch (JwtException | IllegalArgumentException e) {
            return null;    //유효하지 않은 토큰
        }
    }
}
