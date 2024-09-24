package com.sparta.sweethoney.domain.store.repository;

import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.enums.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영 중인 가게 수 조회
    int countByUserIdAndStoreStatus(Long userId, StoreStatus status);

    // 가게명으로 일괄 조회
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:name% AND s.storeStatus = :status ORDER BY CASE WHEN s.adStatus = 'ADVERTISED' THEN 1 ELSE 0 END DESC")
    List<Store> searchStores(@Param("name") String name, @Param("status") StoreStatus status);

    // 가게 단건 조회
    Optional<Store> findByIdAndStoreStatus(Long storeId, StoreStatus storeStatus);
}
