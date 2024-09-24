package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.common.exception.order.*;
import com.sparta.sweethoney.domain.common.exception.store.NotFoundStoreException;
import com.sparta.sweethoney.domain.common.exception.user.NotFoundUserException;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderUpdateStatusResponse;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /* 주문 생성 */
    public OrderCreateDto createOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(NotFoundUserException::new);
        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(NotFoundStoreException::new);
        Menu menu = menuRepository.findById(requestDto.getMenuId()).orElseThrow(NotFoundMenuException::new);

        //영업시간, 최소금액 검증
        LocalTime orderTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        validateTimeAndPrice(orderTime, store, menu);

        Order order = new Order(
                user,
                store,
                menu,
                LocalDateTime.of(LocalDate.now(), orderTime),
                requestDto.getAddress(),
                OrderStatus.PENDING
        );

        orderRepository.save(order);

        return new OrderCreateDto(order);
    }

    /* 주문 전체 조회 */
    public List<OrderFindDto> findAllOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        if (orders.isEmpty()) {
            throw new NotFoundOrderException();
        }

        return orders.stream()
                .map(OrderFindDto::new)
                .collect(Collectors.toList());
    }

    /* 주문 단건 조회 */
    public OrderFindDto findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        return new OrderFindDto(order);
    }

    /* 주문 상태 변경 */
    public OrderUpdateStatusResponse updateStatus(Long orderId, Long userId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        //가게 관리자만 주문 상태를 변경할 수 있다.
        if (checkIsNotOwner(userId, order)) {
            log.info("userId={}", userId);
            log.info("storeId={}", order.getStore().getUser().getId());
            throw new UnauthorizedAccessException();
        }

        //주문 상태 수정 -> 상태 : COMPLETE 이면 주문 완료 시간 같이 담아준다.
        order.updateStatus(status);

        return new OrderUpdateStatusResponse(order, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    /* 영업 시간, 가격 최소 금액 검증 */
    private static void validateTimeAndPrice(LocalTime orderTime, Store store, Menu menu) {
        LocalTime openTime = store.getOpenTime();
        LocalTime closeTime = store.getCloseTime();

        //마감 시간이 자정을 넘었을 때
        if (closeTime.isBefore(openTime)) {
            if (orderTime.isBefore(openTime) && orderTime.isAfter(closeTime)) {
                throw new StoreClosedException();
            }
        }

        //마감 시간이 자정을 넘지 않을 때
        if (closeTime.isAfter(openTime)) {
            if (orderTime.isBefore(openTime) || orderTime.isAfter(closeTime)) {
                throw new StoreClosedException();
            }
        }

        //가게가 정한 최소 주문 금액 이상이어야 주문할 수 있다.
        if (menu.getPrice() < store.getMinOrderPrice()) {
            throw new MinimumOrderAmountException();
        }
    }

    /* 가격 관리자 검증 */
    private static boolean checkIsNotOwner(Long userId, Order order) {
        return !(order.getStore().getUser().getId().equals(userId));
    }
}