package com.sparta.sweethoney.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUserRequestDto {
    private String password;
}
