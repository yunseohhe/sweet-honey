package com.sparta.sweethoney.domain.order.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartRequestDto {
    @NotNull
    Long StoreId;
    @NotNull
    Long menuId;
}
