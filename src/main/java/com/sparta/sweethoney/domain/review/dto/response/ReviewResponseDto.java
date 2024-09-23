package com.sparta.sweethoney.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private final Long id;
    private final String comment;
    private final int rating;
    private final LocalDateTime createdAt;
}
