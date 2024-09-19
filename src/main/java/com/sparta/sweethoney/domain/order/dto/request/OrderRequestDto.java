package com.sparta.sweethoney.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    private Long userId;
    private Long storeId;
    private Long menuId;
    private String address;
}
