package com.sparta.sweethoney.domain.user.service;

import com.sparta.sweethoney.config.PasswordEncoder;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.sweethoney.domain.user.dto.request.SigninRequestDto;
import com.sparta.sweethoney.domain.user.dto.response.SigninResponseDto;
import com.sparta.sweethoney.domain.user.dto.request.SignupRequestDto;
import com.sparta.sweethoney.domain.user.dto.response.SignupResponseDto;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
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
    public SignupResponseDto signUp(@Validated SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String email = signupRequestDto.getEmail();

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
        // RequestDto -> Entity
        User user = User.saveUser(signupRequestDto, password);
        //DB 저장
        User saveUser = userRepository.save(user);
        //token생성
        String token = jwtUtil.createToken(saveUser.getId());

        return new SignupResponseDto(token);

    }

    @Transactional
    public SigninResponseDto signIn(SigninRequestDto signinRequestDto) {
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

    public String deleteUser(DeleteUserRequestDto deleteUserRequestDto, AuthUser authUser) {
        // 사용자 확인
        User user = userRepository.findById(authUser.getId()).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(deleteUserRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("패스워드를 확인해주세요.");
        }
        // 유저 상태 DELETED 로 변경
        user.deleteUser(UserStatus.DELETED);

        userRepository.save(user);
        return "회원탈퇴가 정상적으로 되었습니다.";
    }

}
