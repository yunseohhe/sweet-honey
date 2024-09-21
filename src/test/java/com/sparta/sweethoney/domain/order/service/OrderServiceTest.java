package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderFindDto;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @MockBean
    UserRepository userRepository;
    @MockBean
    StoreRepository storeRepository;
    @MockBean
    MenuRepository menuRepository;

    @Autowired
    OrderService orderService;

    @Test
    void createOrder() {
        //given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(2));
        ReflectionTestUtils.setField(store, "minOrderPrice", 10000);

        Long menuId = 1L;
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "price", 15000);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "서울시 여러분 담배꽁초~ㅋㅋ");

        //when
        OrderCreateDto createOrder = orderService.createOrder(orderRequestDto);
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
    void findAllOrders() {
    }

    @Test
    void findOrder() {
    }

    @Test
    void updateStatus() {
    }
}