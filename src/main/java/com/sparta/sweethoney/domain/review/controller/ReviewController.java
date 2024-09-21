package com.sparta.sweethoney.domain.review.controller;

import com.sparta.sweethoney.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.sweethoney.domain.review.dto.response.ReviewResponseDto;
import com.sparta.sweethoney.domain.review.service.ReviewService;
import com.sparta.sweethoney.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    public final ReviewService reviewService;

    /**
     * 리뷰 생성 API
     * @param orderId 주문 ID
     * @param reviewCreateRequestDto 리뷰 생성 요청 데이터 (별점, 코멘트)
     * @return 생성된 리뷰의 정보 (리뷰 ID, 주문 ID, 별점, 코멘트, 가게 ID)
     */
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<?>> createReview(@PathVariable Long orderId, @RequestBody ReviewCreateRequestDto reviewCreateRequestDto){
        return ResponseEntity.ok(ApiResponse.success(reviewService.createReview(orderId,reviewCreateRequestDto)));
    }

    /**
     * 리뷰 조회 API
     * @param storeId 가게 ID
     * @param minRating 최소 별점
     * @param maxRating 최대 별점
     * @return 가게에 대한 리뷰 목록, 최신순으로 정렬
     */
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ApiResponse<?>> getReviews(
            @PathVariable Long storeId,
            @RequestParam Optional<Integer> minRating,
            @RequestParam Optional<Integer> maxRating
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }
}
