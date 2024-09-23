package com.sparta.sweethoney.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.sweethoney.domain.menu.dto.response.GetMenuResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {
    private final Long id;
    private final String name;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime closeTime;

    private final int minOrderPrice;
    private final String notice;
    private final List<GetMenuResponseDto> menuList;
}
