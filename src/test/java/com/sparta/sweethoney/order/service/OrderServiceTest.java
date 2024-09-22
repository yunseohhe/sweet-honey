package com.sparta.sweethoney.order.service;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderUpdateStatusResponse;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.domain.order.service.OrderService;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.COMPLETE;
import static com.sparta.sweethoney.domain.order.enums.OrderStatus.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    StoreRepository storeRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    Order order;
    User user;
    Store store;
    Menu menu;

    @BeforeEach
    void beforeGiven() {
        Long userId = 1L;
        user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "email", "thisisemail@gmail.com");

        Long storeId = 1L;
        store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(store, "name", "제육킹밥집");
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(2));
        ReflectionTestUtils.setField(store, "minOrderPrice", 10000);

        Long menuId = 1L;
        menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "name", "제육쌈밥");
        ReflectionTestUtils.setField(menu, "price", 15000);

        Long orderId = 1L;
        order = new Order(
                user,
                store,
                menu,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                "주소",
                PENDING);
        ReflectionTestUtils.setField(order, "id", orderId);
    }

    @Test
    @DisplayName("정상 주문 생성과 주문 조회")
    void createAndFindOrderTest() {
        //given
        Long orderId = 1L;
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "주소");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        //when
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrderCreateDto createOrder = orderService.createOrder(orderRequestDto);
        ReflectionTestUtils.setField(createOrder, "orderId", orderId);

        OrderFindDto findOrder = orderService.findOrder(createOrder.getOrderId());

        //then
        assertThat(findOrder.getOrderId()).isEqualTo(createOrder.getOrderId());
        assertThat(findOrder.getStoreName()).isEqualTo(createOrder.getStoreName());
        assertThat(findOrder.getUserEmail()).isEqualTo(createOrder.getUserEmail());
        assertThat(findOrder.getDeliveryAddress()).isEqualTo(createOrder.getDeliveryAddress());
        assertThat(findOrder.getOrderStatus().name()).isEqualTo("PENDING");
        assertThat(findOrder.getOrderTime()).isEqualTo(createOrder.getOrderTime());
        assertThat(findOrder.getOrderCompleteTime()).isNull();
    }

    @Test
    @DisplayName("정상 주문 전체 조회 - 특정 유저 아이디")
    void findAllOrdersByUserId() {
        //given
        Long userId = 1L;
        List<Order> orders = List.of(order);

        //when
        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);
        List<OrderFindDto> findOrders = orderService.findAllOrders(userId);

        //then
        assertThat(findOrders.size()).isEqualTo(1);
        assertThat(findOrders.get(0).getOrderId()).isEqualTo(1L);
        assertThat(findOrders.get(0).getUserEmail()).isEqualTo("thisisemail@gmail.com");
        assertThat(findOrders.get(0).getOrderAmount()).isEqualTo(15000);
        assertThat(findOrders.get(0).getOrderStatus()).isEqualTo(PENDING);
        assertThat(findOrders.get(0).getDeliveryAddress()).isEqualTo("주소");
        assertThat(findOrders.get(0).getStoreName()).isEqualTo("제육킹밥집");
        assertThat(findOrders.get(0).getMenuName()).isEqualTo("제육쌈밥");
    }

    @Test
    @DisplayName("정상 주문 상태 변경")
    void updateStatus() {
        //given
        Long orderId = 1L;
        Long userId = 1L;

        //when
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderUpdateStatusResponse updateResponse = orderService.updateStatus(orderId, userId, COMPLETE);

        //then
        assertThat(order.getStatus()).isEqualTo(COMPLETE);
    }
}