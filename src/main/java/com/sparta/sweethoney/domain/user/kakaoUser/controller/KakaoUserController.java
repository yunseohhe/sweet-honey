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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class KakaoUserController {
    private final KakaoUserService kakaoUserService;


    @GetMapping("/sweethoney/home")
    public String loginPage() {
        return "home";
    }


    @GetMapping("/login-kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response, Model model) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String createToken = kakaoUserService.kakaoLogin(code, response);

        return "main";
    }

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody KakaoRegisterRequestDto kakaoRegisterRequestDto, @Auth AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(kakaoUserService.register(kakaoRegisterRequestDto, authUser.getId())));
    }

}
