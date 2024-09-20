package com.sparta.sweethoney.domain.user.dto.request;

import com.sparta.sweethoney.domain.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "GUEST 와 OWNER 중 선택해주세요.")
    private UserRole userRole;
}
