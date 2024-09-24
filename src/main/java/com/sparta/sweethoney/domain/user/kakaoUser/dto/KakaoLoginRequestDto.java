package com.sparta.sweethoney.domain.user.kakaoUser.dto;

import com.sparta.sweethoney.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {
    private UserRole userRole;

}
