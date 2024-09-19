package com.sparta.sweethoney.domain.menu.repository;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
