package com.sparta.sweethoney.domain.store.repository;

import com.sparta.sweethoney.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
