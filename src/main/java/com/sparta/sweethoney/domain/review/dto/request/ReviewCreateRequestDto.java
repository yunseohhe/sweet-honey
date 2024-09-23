package com.sparta.sweethoney.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private Integer rating;
    private String comment;
}
