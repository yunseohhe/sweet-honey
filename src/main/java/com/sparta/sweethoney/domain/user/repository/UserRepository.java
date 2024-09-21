package com.sparta.sweethoney.domain.user.repository;

import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);


}
