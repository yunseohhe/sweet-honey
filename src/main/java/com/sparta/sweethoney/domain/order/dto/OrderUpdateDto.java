package com.sparta.sweethoney.domain.order.dto;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderUpdateDto {

    private final Long id;
    private final String storeName;
    private final OrderStatus status;

    public OrderUpdateDto(Order order) {
        this.id = order.getId();
        this.storeName = order.getStore().getName();
        this.status = order.getStatus();
    }
}
