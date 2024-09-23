package com.sparta.sweethoney.domain.store.repository;

import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.enums.StoreStatus;
import com.sparta.sweethoney.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영 중인 가게 수 조회
    int countByUserIdAndStoreStatus(Long userId, StoreStatus status);

    // 가게 일괄 조회
    List<Store> findAllByStoreStatusOrderByAdStatusDesc(StoreStatus storeStatus);

    // 가게 단건 조회
    Optional<Store> findByIdAndStoreStatus(Long storeId, StoreStatus storeStatus);
}
