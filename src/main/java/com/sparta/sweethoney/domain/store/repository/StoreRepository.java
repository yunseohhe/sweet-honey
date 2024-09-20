package com.sparta.sweethoney.domain.store.repository;

import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영 중인 가게 수 조회
    int countByOwner(User owner);
}
