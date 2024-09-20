package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.common.exception.order.*;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.OrderUpdateDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.COMPLETE;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /**
     * 주문을 생성한다.
     *
     * @param requestDto
     * @return OrderCreateDto
     */
    public OrderCreateDto createOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(StoreNotFoundException::new);
        Menu menu = menuRepository.findById(requestDto.getMenuId()).orElseThrow(MenuNotFoundException::new);

        //영업시간, 최소금액 검증
        LocalTime orderTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        validateTimeAndPrice(orderTime, store, menu);

        Order order = new Order(
                user,
                store,
                menu,
                LocalDateTime.of(LocalDate.now(), orderTime),
                requestDto.getAddress(),
                menu.getPrice(),
                OrderStatus.PENDING
        );

        orderRepository.save(order);

        return new OrderCreateDto(order);
    }

    /**
     * 전체 주문 조회 -> 유저가 OWNER || USER 따라서 다름. (미정) -> 가게 사장님도 주문 가
     *
     * @param userId
     * @return
     */
    public List<OrderFindDto> findAllOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        if (orders.isEmpty()) {
            throw new OrderNotFoundException();
        }

        return orders.stream()
                .map(OrderFindDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId
     * @return OrderFindDto
     */
    public OrderFindDto findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return new OrderFindDto(order);
    }

    /**
     * 주문 상태 변경
     *
     * @param orderId
     * @param status
     * @return OrderUpdateDto
     */
    public OrderUpdateDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (status.equals(COMPLETE)) {
            order.setOrderCompleteTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }

        order.setStatus(status);

        return new OrderUpdateDto(order);
    }

    /**
     * 영엽시간, 최소금액 검증
     * @param orderTime
     * @param store
     * @param menu
     */
    private static void validateTimeAndPrice(LocalTime orderTime, Store store, Menu menu) {
        //마감시간이 넘으면 주문 할 수 없다.
        if (orderTime.isAfter(store.getCloseTime()) || orderTime.isBefore(store.getOpenTime())) {
            throw new StoreClosedException();
        }

        //가게가 정한 최소 주문 금액 이상이어야 주문할 수 있다.
        if (menu.getPrice() < store.getMinOrderPrice()) {
            throw new MinimumOrderAmountException();
        }
    }
}
