package com.sparta.sweethoney.domain.order.dto.response;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderUpdateStatusResponse {
    private final Long orderId;
    private final String storeName;
    private final OrderStatus orderStatus;
    private final LocalDateTime modificationTime;

    public OrderUpdateStatusResponse(Order order, LocalDateTime modificationTime) {
        this.orderId = order.getId();
        this.storeName = order.getStore().getName();
        this.orderStatus = order.getStatus();
        this.modificationTime = modificationTime;
    }
}
