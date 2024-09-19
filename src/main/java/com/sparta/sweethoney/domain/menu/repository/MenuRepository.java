package com.sparta.sweethoney.domain.menu.repository;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndStoreId(Long menuId, Long id);
}
