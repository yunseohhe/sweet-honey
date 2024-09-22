package com.sparta.sweethoney.domain.user.entity;


import com.sparta.sweethoney.domain.user.dto.request.SignupRequestDto;
import com.sparta.sweethoney.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends Timestamped {

    private String email;
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private Long kakaoId;


    public User(String email, String userName, String password, UserRole userRole, UserStatus userStatus) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public User(String email, String userName, String password, UserRole userRole, UserStatus userStatus, Long kakaoId) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.kakaoId = kakaoId;
    }


    public static User saveUser(SignupRequestDto signupRequestDto, String password) {
        return new User(signupRequestDto.getEmail(), signupRequestDto.getUserName(), password, signupRequestDto.getUserRole(),  UserStatus.ACTIVE);
    }

    public void deleteUser(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}
