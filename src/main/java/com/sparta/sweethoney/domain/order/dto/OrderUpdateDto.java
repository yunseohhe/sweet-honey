package com.sparta.sweethoney.domain.order.dto;

import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderUpdateDto {

    private final Long id;
    private final OrderStatus status;
}
