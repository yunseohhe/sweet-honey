package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
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
    }

    @Test
    void findAllOrders() {
        //given

        //when

        //then

    }

    @Test
    void updateStatus() {
        //given

        //when

        //then

    }


}