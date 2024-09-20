package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.OrderUpdateDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @MockBean
    StoreRepository storeRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    MenuRepository menuRepository;

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("정상 주문 생성 테스트")
    void createOrder() {
        //given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "storeId", storeId);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(2));
        ReflectionTestUtils.setField(store, "minOrderPrice", 10000);

        Long menuId = 1L;
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "price", 15000);

        when(userRepository.save(user)).thenReturn(user);
        when(storeRepository.save(store)).thenReturn(store);
        when(menuRepository.save(menu)).thenReturn(menu);

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "서울시 여러분 담배꽁초~ㅋㅋ");

        //when
        OrderCreateDto createOrder = orderService.createOrder(orderRequestDto);
        OrderFindDto findOrder = orderService.findOrder(createOrder.getId());

        //then
        assertThat(findOrder.getId()).isEqualTo(createOrder.getId());
        assertThat(findOrder.getStoreName()).isEqualTo(createOrder.getStoreName());
        assertThat(findOrder.getUserEmail()).isEqualTo(createOrder.getUserEmail());
        assertThat(findOrder.getAddress()).isEqualTo(createOrder.getAddress());
        assertThat(findOrder.getStatus().name()).isEqualTo("PENDING");
        assertThat(findOrder.getOrderTime()).isEqualTo(createOrder.getOrderTime());
        assertThat(findOrder.getOrderCompleteTime()).isNull();
    }

    @Test
    @DisplayName("정상 주문 전체 조회 테스트")
    void findAllOrders() {
        //given
        Long userAId = 1L;
        User userA = new User();
        ReflectionTestUtils.setField(userA, "id", userAId);

        Long userBId = 2L;
        User userB = new User();
        ReflectionTestUtils.setField(userB, "id", userBId);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "storeId", storeId);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(2));
        ReflectionTestUtils.setField(store, "minOrderPrice", 10000);

        Long menuId = 1L;
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "price", 15000);

        when(userRepository.save(userA)).thenReturn(userA);
        when(userRepository.save(userB)).thenReturn(userB);
        when(storeRepository.save(store)).thenReturn(store);
        when(menuRepository.save(menu)).thenReturn(menu);

        OrderRequestDto orderARequestDto = new OrderRequestDto(userAId, storeId, menuId, "서울시 여러분 담배꽁초~ㅋㅋ");
        OrderRequestDto orderBRequestDto = new OrderRequestDto(userBId, storeId, menuId, "안녕하세욤ㅎㅎ");

        orderService.createOrder(orderARequestDto);
        orderService.createOrder(orderBRequestDto);

        //when
        // findOrderA <-> userAOrders contains() 가능한가?
        OrderFindDto findOrderA = orderService.findOrder(1L);
        List<OrderFindDto> userAOrders = orderService.findAllOrders(userAId);

        //then
        assertThat(userAOrders).isNotNull();
        assertThat(userAOrders.size()).isEqualTo(1);
        assertThat(userAOrders.contains(findOrderA)).isTrue();
    }

    @Test
    void updateStatus() {
        //given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        Long storeUserId = 2L;
        User storeUser = new User();
        ReflectionTestUtils.setField(user, "id", storeUser);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "storeId", storeId);
        ReflectionTestUtils.setField(store, "user", storeUser);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(2));
        ReflectionTestUtils.setField(store, "minOrderPrice", 10000);

        Long menuId = 1L;
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "price", 15000);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(storeUser)).thenReturn(storeUser);
        when(storeRepository.save(store)).thenReturn(store);
        when(menuRepository.save(menu)).thenReturn(menu);

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "주소지가 바로 여기입니당!!!");

        OrderCreateDto createOrder = orderService.createOrder(orderRequestDto);
        Long orderId = createOrder.getId();

        //when
        OrderUpdateDto orderUpdateDto = orderService.updateStatus(orderId, storeId, ACCEPTED);
        OrderFindDto findOrder = orderService.findOrder(orderId);

        //then
        assertThat(findOrder.getStatus()).isEqualTo(ACCEPTED);
    }


}