package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import com.sparta.sweethoney.exception.order.MenuNotFoundException;
import com.sparta.sweethoney.exception.order.OrderNotFoundException;
import com.sparta.sweethoney.exception.order.StoreNotFoundException;
import com.sparta.sweethoney.exception.order.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /**
     * 주문을 생성한다.
     * @param  requestDto
     * @return OrderCreateDto
     */
    public OrderCreateDto createOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(StoreNotFoundException::new);
        Menu menu = menuRepository.findById(requestDto.getMenuId()).orElseThrow(MenuNotFoundException::new);

        Order order = new Order(
                user,
                store,
                menu,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                requestDto.getAddress(),
                menu.getPrice(),
                OrderStatus.PENDING
        );

        orderRepository.save(order);

        return new OrderCreateDto(order);
    }

    /**
     * 전체 주문 조회 -> 유저가 OWNER || USER 따라서 다름.
     * @return List<OrderFindDto>
     */
    public List<OrderFindDto> orderList() {



        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new OrderNotFoundException();
        }

        return orders.stream()
                .map(OrderFindDto::new)
                .collect(Collectors.toList());
    }

    public OrderDto findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return new OrderDto(order);
    }

    public OrderDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        order.setStatus(status);

        return new OrderDto(order);
    }

}
