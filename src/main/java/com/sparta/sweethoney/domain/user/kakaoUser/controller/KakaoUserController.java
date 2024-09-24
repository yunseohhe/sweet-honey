package com.sparta.sweethoney.domain.user.kakaoUser.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.user.kakaoUser.dto.KakaoRegisterRequestDto;
import com.sparta.sweethoney.domain.user.kakaoUser.service.KakaoUserService;
import com.sparta.sweethoney.util.ApiResponse;
import com.sparta.sweethoney.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KakaoUserController {
    private final KakaoUserService kakaoUserService;



    @GetMapping("/login-kakao")
    public ResponseEntity<ApiResponse<?>> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String createToken = kakaoUserService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("https://kapi.kakao.com/v2/user/me");
        response.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.success(createToken));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody KakaoRegisterRequestDto kakaoRegisterRequestDto, @Auth AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(kakaoUserService.register(kakaoRegisterRequestDto, authUser.getId())));
    }

}
