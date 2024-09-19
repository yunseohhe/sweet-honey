package com.sparta.sweethoney.domain.order.dto;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class OrderFindDto {

    private final Long id;
    private final int amount;

    private final String storeName;
    private final String userName;
    private final String menuName;
    private final String address;

    private final OrderStatus status;

    private final LocalDateTime orderTime;
    private final LocalDateTime orderCompleteTime;

    public OrderFindDto(Order order) {
        this.id = order.getId();
        this.amount = order.getAmount();
        this.storeName = order.getUser().getUserName();
        this.userName = order.getStore().getStoreName();
        this.menuName = order.getMenu().getName();
        this.address = order.getDeliveryAddress();
        this.status = order.getStatus();
        this.orderTime = order.getOrderTime();
        this.orderCompleteTime = order.getOrderCompleteTime();
    }
}
