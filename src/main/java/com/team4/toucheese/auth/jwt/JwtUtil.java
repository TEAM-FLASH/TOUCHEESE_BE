package com.team4.toucheese.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private static final long EXPIRATION_TIME = 3600000;    //1시간
    private final JwtParser jwtParser;

    public JwtUtil(
            @Value("${jwt.secret}")
            String jwtSecret
    ){
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
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

    // JWT를 인자로 받고, 그 JWT를 해석해서
    // 사용자 정보를 회수하는 메소드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
