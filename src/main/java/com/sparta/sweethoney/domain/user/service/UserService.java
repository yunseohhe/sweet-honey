package com.sparta.sweethoney.domain.user.service;

import com.sparta.sweethoney.config.PasswordEncoder;
import com.sparta.sweethoney.domain.user.dto.SigninRequestDto;
import com.sparta.sweethoney.domain.user.dto.SigninResponseDto;
import com.sparta.sweethoney.domain.user.dto.SignupRequestDto;
import com.sparta.sweethoney.domain.user.dto.SignupResponseDto;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import com.sparta.sweethoney.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponseDto signup(@Validated SignupRequestDto signupRequestDto, HttpServletResponse res) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String email = signupRequestDto.getEmail();

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
        // RequestDto -> Entity
        User user = new User(signupRequestDto,password);
        //DB 저장
        User saveUser = userRepository.save(user);
        //token생성
        String token = jwtUtil.createToken(saveUser.getId());

        return new SignupResponseDto(token);

    }

    @Transactional
    public SigninResponseDto signin(SigninRequestDto signinRequestDto, HttpServletResponse res) {
        // 등록되지 않은 Email 확인
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("이메일을 확인해주세요."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("패스워드를 확인해주세요.");
        }

        //token생성
        String token = jwtUtil.createToken(user.getId());

        return new SigninResponseDto(token);
    }
}
