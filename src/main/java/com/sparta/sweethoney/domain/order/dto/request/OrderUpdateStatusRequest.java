package com.sparta.sweethoney.domain.order.dto.request;

import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderUpdateStatusRequest {
    @NotNull
    private OrderStatus status;
}
