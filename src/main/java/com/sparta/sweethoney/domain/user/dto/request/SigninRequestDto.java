package com.sparta.sweethoney.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SigninRequestDto {
    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "비밀번호는 최소 8자리 이상, 영문, 특수문자를 1자 이상 포함되어야 합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
