package com.sparta.sweethoney.domain.user.controller;

import com.sparta.sweethoney.domain.user.dto.SigninRequestDto;
import com.sparta.sweethoney.domain.user.dto.SigninResponseDto;
import com.sparta.sweethoney.domain.user.dto.SignupRequestDto;
import com.sparta.sweethoney.domain.user.dto.SignupResponseDto;
import com.sparta.sweethoney.domain.user.service.UserService;
import com.sparta.sweethoney.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto, HttpServletResponse res, HttpServletRequest request) {
        return ResponseEntity.ok(userService.signup(signupRequestDto, res));
    }

    @PostMapping("/users/login")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody SigninRequestDto signinRequestDto, HttpServletResponse res) {
        return ResponseEntity.ok(userService.signin(signinRequestDto, res));
    }

}
