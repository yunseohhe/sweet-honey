package com.sparta.sweethoney.domain.user.entity;


import com.sparta.sweethoney.domain.user.dto.SignupRequestDto;
import com.sparta.sweethoney.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;


    public User(SignupRequestDto signupRequestDto, String password) {
        this.email = signupRequestDto.getEmail();
        this.userName = signupRequestDto.getUserName();
        this.password = password;
    }
}
