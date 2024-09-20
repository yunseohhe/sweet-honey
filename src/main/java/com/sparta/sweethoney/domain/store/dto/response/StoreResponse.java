package com.sparta.sweethoney.domain.store.dto.response;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreResponse {
    private final Long id;
    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final int minOrderPrice;

    public StoreResponse(Long id, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
    }
}
