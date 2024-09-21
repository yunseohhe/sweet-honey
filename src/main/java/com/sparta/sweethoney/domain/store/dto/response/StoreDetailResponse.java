package com.sparta.sweethoney.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.sweethoney.domain.menu.dto.response.GetMenuResponseDto;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreDetailResponse {
    private final Long id;
    private final String name;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime closeTime;

    private final int minOrderPrice;
    private final List<GetMenuResponseDto> menuList;

    public StoreDetailResponse(Long id, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice, List<GetMenuResponseDto> menuList) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.menuList = menuList;
    }
}
