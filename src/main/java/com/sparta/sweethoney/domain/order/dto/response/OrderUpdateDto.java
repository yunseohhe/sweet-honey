package com.sparta.sweethoney.domain.order.dto.response;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderUpdateDto {

    private final Long orderId;
    private final String storeName;
    private final OrderStatus orderStatus;

    public OrderUpdateDto(Order order) {
        this.orderId = order.getId();
        this.storeName = order.getStore().getName();
        this.orderStatus = order.getStatus();
    }
}
