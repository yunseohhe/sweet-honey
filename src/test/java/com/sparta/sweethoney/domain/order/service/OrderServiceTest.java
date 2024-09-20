package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    void before() {

    }

    @Test
    void createOrder() {
        //given
        OrderRequestDto request = new OrderRequestDto();
        request.setUserId(1L);
        request.setMenuId(1L);
        request.setStoreId(1L);
        request.setAddress("주소입니다.");
        //when
/*
        OrderCreateDto createOrder = orderService.createOrder(requestDto);
        OrderFindDto findOrder = orderService.findOrder(createOrder.getId());

        //then
        assertThat(findOrder.getId()).isEqualTo(createOrder.getId());
*/
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