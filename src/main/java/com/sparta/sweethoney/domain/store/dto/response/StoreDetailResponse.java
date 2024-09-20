package com.sparta.sweethoney.domain.store.dto.response;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreDetailResponse {
    private final Long id;
    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final int minOrderPrice;
    private final List<PostMenuResponseDto> menuList;

    public StoreDetailResponse(Long id, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice, List<PostMenuResponseDto> menuList) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.menuList = menuList;
    }
}
