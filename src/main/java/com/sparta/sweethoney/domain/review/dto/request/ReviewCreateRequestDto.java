package com.sparta.sweethoney.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequestDto {
    private Integer rating;
    private String comment;
}
