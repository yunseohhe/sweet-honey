package com.sparta.sweethoney.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequestDto {
    private Long userId;
    private Long storeId;
    private Long menuId;
    private Integer count;
    private String address;
}
