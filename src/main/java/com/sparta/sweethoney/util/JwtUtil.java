package com.sparta.sweethoney.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    //JWT 데이터-Secret Key
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    //encoding decoding algorithm 선언
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
     @Value("${jwt.secret.key}")
    // Base64 Encode 한 SecretKey
    //application.properties 에서 선언한 jwt.secret.key 를 String으로 저장
    private String secretKey;
    //키를 저장할 키클래스 필드선언
    private Key key;
    @PostConstruct
    public void init() {
        //byte array 에 secretkey 를 base 64로 디코딩 한값을 저장
        System.out.println(secretKey);
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        //디코딩한 값을 가진 byte array 를 key 필드에 다시 저장
        key = Keys.hmacShaKeyFor(bytes);
    }
    //JWT 생성
    // 토큰 생성
    public String createToken(Long userId) {
        Date date = new Date();
        return BEARER_PREFIX +
                //Jwts.builder 를 사용하여 토큰을 생성
                Jwts.builder()
                        .setSubject(Long.toString(userId)) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 = 현제시간 date.getTIME() + 만료시간 TOKEN_TIME
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘 (키 , 선택한 알고리즘)
                        .compact();
    }



    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new NullPointerException("Not Found Token");
    }

    public boolean validateToken(String token) {
        try {//암호화할떄 사용한 키를 .setSigningkey , parseClaimsJws(token)은 받아와서 검증할 토큰
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}