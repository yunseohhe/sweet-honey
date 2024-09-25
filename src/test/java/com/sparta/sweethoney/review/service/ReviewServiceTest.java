package com.sparta.sweethoney.review.service;

import com.sparta.sweethoney.domain.common.exception.order.NotFoundOrderException;
import com.sparta.sweethoney.domain.common.exception.review.InvalidRatingException;
import com.sparta.sweethoney.domain.common.exception.review.OrderNotCompleteException;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.sweethoney.domain.review.dto.response.ReviewCreateResponseDto;
import com.sparta.sweethoney.domain.review.entity.Review;
import com.sparta.sweethoney.domain.review.repository.ReviewRepository;
import com.sparta.sweethoney.domain.review.service.ReviewService;
import com.sparta.sweethoney.domain.store.entity.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private Order order;

    @Test
    public void 리뷰_생성_성공() {
        Long orderId = 1L;
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "맛있어요!");

        Store store = mock(Store.class);

        when(order.getStatus()).thenReturn(OrderStatus.COMPLETE);
        when(order.getStore()).thenReturn(store);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Review review = new Review(order, store, 5, "맛있어요!");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewCreateResponseDto result = reviewService.createReview(orderId, requestDto);

        assertThat(result.getRating()).isEqualTo(5);
        assertThat(result.getComment()).isEqualTo("맛있어요!");
        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void 리뷰_생성_실패_주문_찾을_수_없음() {
        Long orderId = 1L;
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "좋아요");

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(orderId, requestDto))
                .isInstanceOf(NotFoundOrderException.class);

        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void 리뷰_생성_실패_주문_완료되지_않음() {
        Long orderId = 1L;
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(4, "괜찮아요");

        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> reviewService.createReview(orderId, requestDto))
                .isInstanceOf(OrderNotCompleteException.class);

        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void 리뷰_생성_실패_잘못된_평점() {
        Long orderId = 1L;
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(6, "맛없어요");

        when(order.getStatus()).thenReturn(OrderStatus.COMPLETE);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> reviewService.createReview(orderId, requestDto))
                .isInstanceOf(InvalidRatingException.class);

        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, never()).save(any(Review.class));
    }
}

