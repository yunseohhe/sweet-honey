package com.sparta.sweethoney.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateResponseDto {
    private final Long reviewId;
    private final Long orderId;
    private final int rating;
    private final String comment;
    private final Long storeId;
}
