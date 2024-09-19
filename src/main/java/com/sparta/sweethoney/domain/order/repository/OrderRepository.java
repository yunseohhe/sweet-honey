package com.sparta.sweethoney.domain.order.repository;

import com.sparta.sweethoney.domain.order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}
