package com.sparta.sweethoney.domain.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequest {
    private String name;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int minOrderPrice;
}
