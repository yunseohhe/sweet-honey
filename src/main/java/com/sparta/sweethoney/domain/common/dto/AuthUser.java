package com.sparta.sweethoney.domain.common.dto;

import com.sparta.sweethoney.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {
    private final Long id;
    private final String userName;
    private final String email;
    private final UserRole userRole;

    public AuthUser(Long id, String userName, String email, UserRole userRole) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;
    }
}
