package com.sparta.sweethoney.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StorePutResponse {
    private final Long id;
    private final String name;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime closeTime;

    private final int minOrderPrice;
    private final String notice;
}
