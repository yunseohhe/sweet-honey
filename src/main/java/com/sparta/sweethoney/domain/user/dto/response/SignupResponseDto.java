package com.sparta.sweethoney.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {

    private String Token;

    public SignupResponseDto(String token){
        this.Token = token;
    }

}
