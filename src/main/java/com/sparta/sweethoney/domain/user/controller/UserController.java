package com.sparta.sweethoney.domain.user.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.sweethoney.domain.user.dto.request.SigninRequestDto;
import com.sparta.sweethoney.domain.user.dto.response.SigninResponseDto;
import com.sparta.sweethoney.domain.user.dto.request.SignupRequestDto;
import com.sparta.sweethoney.domain.user.dto.response.SignupResponseDto;
import com.sparta.sweethoney.domain.user.service.UserService;
import com.sparta.sweethoney.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<?>> signUp(@Validated @RequestBody SignupRequestDto signupRequestDto) {
            return ResponseEntity.ok(ApiResponse.success(userService.signUp(signupRequestDto)));
    }

    @PostMapping("/users/login")
    public ResponseEntity<ApiResponse<?>> signIn(@RequestBody SigninRequestDto signinRequestDto) {
            return ResponseEntity.ok(ApiResponse.success(userService.signIn(signinRequestDto)));
        }

    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse<?>> deleteUser(@RequestBody DeleteUserRequestDto deleteUserRequestDto, @Auth AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(userService.deleteUser(deleteUserRequestDto, authUser)));
    }
}
