package com.sparta.sweethoney.domain.user.service;

import com.sparta.sweethoney.config.PasswordEncoder;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.sweethoney.domain.user.dto.request.SigninRequestDto;
import com.sparta.sweethoney.domain.user.dto.request.SignupRequestDto;
import com.sparta.sweethoney.domain.user.dto.response.SigninResponseDto;
import com.sparta.sweethoney.domain.user.dto.response.SignupResponseDto;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserRole;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import com.sparta.sweethoney.domain.user.util.UserMockDataUtil;
import com.sparta.sweethoney.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;


    @Test
    void user_회원가입_성공() {
        // given
        String token = "token";
        SignupRequestDto signupRequestDto = new SignupRequestDto("email@naver.com", "name", "password", UserRole.GUEST);
        String encodedPassword = "encodedPassword";

        User user = User.saveUser(signupRequestDto, encodedPassword);
        given(userRepository.save(any())).willReturn(user);
        given(jwtUtil.createToken(user.getId())).willReturn(token);

        //when
        SignupResponseDto result = userService.signUp(signupRequestDto);

        //then
        assertNotNull(result);

    }

    @Test
    void user_로그인_성공() {
        //given
        String email = "email";
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        String token = "token";
        SigninRequestDto signinRequestDto = new SigninRequestDto(email, rawPassword);
        User user = new User(email, "name", encodedPassword, UserRole.GUEST, UserStatus.ACTIVE);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(jwtUtil.createToken(user.getId())).willReturn(token);

        //when
        SigninResponseDto signinResponseDto = userService.signIn(signinRequestDto);
        //then
        assertNotNull(signinResponseDto);
    }

    @Test
    void user_회원_삭제() {
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto(rawPassword);
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "email", UserRole.GUEST);
        User user = new User(authUser.getEmail(), authUser.getUserName(), encodedPassword, UserRole.GUEST, UserStatus.ACTIVE);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        user.deleteUser(UserStatus.DELETED);

        String result = userService.deleteUser(deleteUserRequestDto, authUser);

        assertNotNull(result);
        assertEquals(UserStatus.DELETED, user.getUserStatus());
    }
}