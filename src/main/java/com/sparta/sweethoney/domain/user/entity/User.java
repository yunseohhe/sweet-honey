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


    public User(String email, String userName, String password, UserRole userRole, UserStatus userStatus) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public static User saveUser(SignupRequestDto signupRequestDto, String password) {
        return new User(signupRequestDto.getEmail(), signupRequestDto.getUserName(), password, signupRequestDto.getUserRole(),  UserStatus.ACTIVE);
    }

//    public static User deleteUser(User user, UserStatus userStatus) {
//        return new User(user.getEmail(), user.getUserName(), user.password, user.getUserRole(), userStatus);
//    }
}
