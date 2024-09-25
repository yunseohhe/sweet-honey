package com.sparta.sweethoney.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninRequestDto {
    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;


    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
