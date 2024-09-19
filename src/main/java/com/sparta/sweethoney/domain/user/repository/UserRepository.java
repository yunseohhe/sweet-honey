package com.sparta.sweethoney.domain.user.repository;

import com.sparta.sweethoney.domain.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
