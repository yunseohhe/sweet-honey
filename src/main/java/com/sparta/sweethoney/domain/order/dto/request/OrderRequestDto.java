package com.sparta.sweethoney.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotNull
    private Long storeId;
    @NotNull
    private Long menuId;
    @NotNull
    private Integer count;
    @NotBlank
    private String address;
}
