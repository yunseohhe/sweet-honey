package com.sparta.sweethoney.order.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.common.exception.order.MinimumOrderAmountException;
import com.sparta.sweethoney.domain.common.exception.order.NotFoundOrderException;
import com.sparta.sweethoney.domain.common.exception.order.UnauthorizedAccessException;
import com.sparta.sweethoney.domain.common.exception.store.NotFoundStoreException;
import com.sparta.sweethoney.domain.common.exception.user.NotFoundUserException;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderUpdateStatusResponse;
import com.sparta.sweethoney.domain.order.logging.aop.OrderLoggingAspect;
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
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.COMPLETE;
import static com.sparta.sweethoney.domain.order.enums.OrderStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @InjectMocks
    OrderLoggingAspect orderLoggingAspect;

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
                2,
                PENDING);
        ReflectionTestUtils.setField(order, "id", orderId);
    }

    @Test
    @DisplayName("정상 주문 생성과 단건 주문 조회")
    void createAndFindOrderTest() {
        //given
        Long orderId = 1L;
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, 2, "주소");

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
        assertThat(findOrder.getCount()).isEqualTo(createOrder.getCount());
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
        assertThat(findOrders.get(0).getCount()).isEqualTo(2);
        assertThat(findOrders.get(0).getOrderAmount()).isEqualTo(30000);
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

        orderService.updateStatus(orderId, userId, COMPLETE);

        //then
        assertThat(order.getStatus()).isEqualTo(COMPLETE);
    }

    @Test
    @DisplayName("예외 주문 생성 - 사용자 찾을 수 없음")
    void createOrderUserException() {
        //given
        Long userId = 2L;
        Long storeId = 1L;
        Long menuId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, 2, "주소입니당!!!");

        //when & then
        when(userRepository.findById(userId)).thenThrow(new NotFoundUserException());

        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    @DisplayName("예외 주문 생성 - 가게 찾을 수 없음")
    void createOrderStoreException() {
        //given
        Long userId = 1L;
        Long storeId = 2L;
        Long menuId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, 2, "주소입니당!!!");

        //when & then
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenThrow(new NotFoundStoreException());

        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(NotFoundStoreException.class);
    }

    @Test
    @DisplayName("예외 주문 생성 - 메뉴 찾을 수 없음")
    void createOrderMenuException() {
        //given
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 2L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, 2, "주소입니당!!!");

        //when & then
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(menuRepository.findById(menuId)).thenThrow(new NotFoundMenuException());

        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(NotFoundMenuException.class);
    }

/*
    @Test
    @DisplayName("예외 주문 생성 - 마감 시간 자정 전, 가게 오픈 시간 이전 주문")
    void createOrderExceptionBeforeOpenTime() {
        //given
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;

        ReflectionTestUtils.setField(store, "openTime", LocalTime.of(7, 0, 0));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.of(23, 59, 59));

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "주소입니당!!!");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(userId)).willReturn(Optional.of(store));
        given(menuRepository.findById(userId)).willReturn(Optional.of(menu));

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(StoreClosedException.class);
    }

    @Test
    @DisplayName("예외 주문 생성 - 마감 시간 자정 전, 가게 마감 시간 이후 주문")
    void createOrderExceptionBeforeOpenTime() {
        //given
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;

        ReflectionTestUtils.setField(store, "openTime", LocalTime.of(7, 0, 0));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.of(23, 59, 59));

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, "주소입니당!!!");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(userId)).willReturn(Optional.of(store));
        given(menuRepository.findById(userId)).willReturn(Optional.of(menu));

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(StoreClosedException.class);
    }
*/

    @Test
    @DisplayName("예외 주문 생성 - 최소 금액 이하 주문")
    void createOrder_MinimumPriceException() {
        //given
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;
        int menuPrice = 2000;

        ReflectionTestUtils.setField(menu, "price", menuPrice);

        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, storeId, menuId, 2, "주소입니당!!!");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(orderRequestDto))
                .isInstanceOf(MinimumOrderAmountException.class);
    }

    @Test
    @DisplayName("예외 주문 전체 조회 - 특정 유저 아이디 - 주문 내역 찾을 수 없음")
    void findAll_NotFoundException() {
        //given
        Long userId = 2L;
        ArrayList<Order> orders = new ArrayList<>();

        //when & then
        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);

        assertThatThrownBy(() -> orderService.findAllOrders(userId))
                .isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("예외 주문 상태 변경 - 가게 관리자가 아닌 유저")
    void updateStatus_UnauthorizedAccessException() {
        //given
        Long orderId = 1L;
        Long userId = 2L;

        //when & then
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateStatus(orderId, userId, COMPLETE))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("주문 생성 시, 로그 출력")
    void orderLoggingAopByOrderCreate() {
        //given
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(OrderLoggingAspect.class);

        listAppender.setContext(loggerContext);
        logger.addAppender(listAppender);
        listAppender.start();

        OrderCreateDto result = new OrderCreateDto(order);

        //when
        orderLoggingAspect.orderLogAccess(result);

        List<String> collect = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        //then
        assertThat(collect).contains(
                String.format(
                        "주문 생성 : 요청 시각=%s, 주문 ID=%s, 가게 이름=%s, 주문 상태=%s",
                        result.getOrderTime(),
                        result.getOrderId(),
                        result.getStoreName(),
                        result.getOrderStatus())
        );
    }

    @Test
    @DisplayName("주문 상태 변경 시, 로그 출력")
    void orderLoggingAopByUpdateStatus() {
        //given
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(OrderLoggingAspect.class);

        listAppender.setContext(loggerContext);
        logger.addAppender(listAppender);
        listAppender.start();

        OrderUpdateStatusResponse result =
                new OrderUpdateStatusResponse(order, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        //when
        orderLoggingAspect.orderLogAccess(result);

        List<String> collect = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        //then
        assertThat(collect).contains(
                String.format(
                        "주문 상태 변경 : 요청 시각=%s, 주문 ID=%s, 가게 이름=%s, 주문 상태=%s",
                        result.getModificationTime(),
                        result.getOrderId(),
                        result.getStoreName(),
                        result.getOrderStatus()
                )
        );
    }
}