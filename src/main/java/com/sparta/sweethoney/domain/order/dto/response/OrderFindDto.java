package com.sparta.sweethoney.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderFindDto {
    private final Long orderId;
    private final int orderAmount;

    private final String storeName;
    private final String userEmail;
    private final String menuName;
    private final String deliveryAddress;

    private final OrderStatus orderStatus;

    private final LocalDateTime orderTime;
    private final LocalDateTime orderCompleteTime;

    public OrderFindDto(Order order) {
        this.orderId = order.getId();
        this.orderAmount = order.getAmount();
        this.storeName = order.getStore().getName();
        this.userEmail = order.getUser().getEmail();
        this.menuName = order.getMenu().getName();
        this.deliveryAddress = order.getDeliveryAddress();
        this.orderStatus = order.getStatus();
        this.orderTime = order.getOrderTime();
        this.orderCompleteTime = order.getOrderCompleteTime();
    }
}
