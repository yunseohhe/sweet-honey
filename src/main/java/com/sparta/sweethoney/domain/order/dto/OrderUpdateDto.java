package com.sparta.sweethoney.domain.order.dto;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;

public class OrderUpdateDto {

    private final Long id;
    private final OrderStatus status;

    public OrderUpdateDto(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
    }
}
