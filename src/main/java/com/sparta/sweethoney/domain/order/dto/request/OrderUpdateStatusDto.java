package com.sparta.sweethoney.domain.order.dto.request;

import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderUpdateStatusDto {

    private OrderStatus status;
}
