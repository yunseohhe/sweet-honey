package com.sparta.sweethoney.domain.review.service;

import com.sparta.sweethoney.domain.common.exception.order.NotFoundOrderException;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.sweethoney.domain.review.dto.response.ReviewCreateResponseDto;
import com.sparta.sweethoney.domain.review.dto.response.ReviewResponseDto;
import com.sparta.sweethoney.domain.review.entity.Review;
import com.sparta.sweethoney.domain.common.exception.review.OrderNotCompleteException;
import com.sparta.sweethoney.domain.common.exception.review.InvalidRatingException;
import com.sparta.sweethoney.domain.review.repository.ReviewRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    // 리뷰 생성
    public ReviewCreateResponseDto createReview(Long orderId, ReviewCreateRequestDto reviewCreateRequestDto) {
        // 리뷰 생성 전에 주문이 유효한지 확인
        Order order = validateOrder(orderId);

        Store store = order.getStore();

        // 평점이 유효한지 확인 (1~5 사이인지)
        validateRating(reviewCreateRequestDto.getRating());

        Review review = new Review(order,store, reviewCreateRequestDto.getRating(), reviewCreateRequestDto.getComment());

        reviewRepository.save(review);

        return mapToResponseDto(review);
    }

    // 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews(Long storeId, Optional<Integer> minRating, Optional<Integer> maxRating) {
        // 가게 ID와 평점 범위로 리뷰를 조회 (최소, 최대 평점이 없으면 기본값 적용)
        List<Review> reviews = reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(
                storeId, minRating.orElse(MIN_RATING), maxRating.orElse(MAX_RATING));

        return convertToDtoList(reviews);
    }

    // 주문 ID 확인과 주문 상태 검증
    private Order validateOrder(Long orderId) {
        // 주문 ID 확인
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        // 주문 상태가 'COMPLETE'인지 확인
        if (order.getStatus() != OrderStatus.COMPLETE) {
            throw new OrderNotCompleteException();
        }

        return order;
    }

    // 리뷰 평점 확인(1 ~ 5 사이가 아니면 예외처리)
    private void validateRating(int rating) {
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new InvalidRatingException();
        }
    }

    // Review를 ReviewCreateResponseDto로 변환
    private ReviewCreateResponseDto mapToResponseDto(Review review) {
        return new ReviewCreateResponseDto(
                review.getId(),
                review.getOrder().getId(),
                review.getRating(),
                review.getComment(),
                review.getStore().getId()
        );
    }

    // Review를 ReviewResponseDto 리스트로 변환
    private List<ReviewResponseDto> convertToDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getComment(),
                        review.getRating(),
                        review.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
