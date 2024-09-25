package com.sparta.sweethoney.domain.order.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderUpdateStatusRequest;
import com.sparta.sweethoney.domain.order.dto.response.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.response.OrderUpdateStatusResponse;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.service.OrderService;
import com.sparta.sweethoney.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문을 생성한다.
     * 1. requestDto 필드 -> 메뉴, 가게 ID
     * 2. servletRequest 내부 저장된 회원 ID를 꺼낸다.
     * 3. UserId를 requestDto에 담고, 넘겨주면서 `OrderService`호출
     * @param requestDto 가게 ID, 메뉴 ID, 수량, 배달 주소
     * @param authUser   유저 ID
     * @return 주문 ID, 수량, 주문 금액, 가게 이름, 회원 이메일, 메뉴 이름, 배달 주소, 주문 상태, 주문 시간
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createOrder(
            @Validated @RequestBody OrderRequestDto requestDto,
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.getId();

        OrderCreateDto orderCreateDto = orderService.createOrder(userId, requestDto);

        return ResponseEntity.ok(ApiResponse.success(orderCreateDto));
    }


    /**
     * 주문 전체 조회
     *
     * @param authUser
     * @return ResponseEntity<List < OrderFindDto>>
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> orders(@Auth AuthUser authUser) {
        Long userId = authUser.getId();

        List<OrderFindDto> orders = orderService.findAllOrders(userId);

        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId
     * @return ResponseEntity<OrderFindDto>
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> findOrder(@PathVariable("orderId") Long orderId) {
        OrderFindDto order = orderService.findOrder(orderId);

        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * 주문 상태 변경
     *
     * @param orderId
     * @param orderUpdateStatusRequest
     * @param authUser
     * @return ResponseEntity<OrderUpdateDto>
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> updateStatus(
            @PathVariable("orderId") Long orderId,
            @Validated @RequestBody OrderUpdateStatusRequest orderUpdateStatusRequest,
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.getId();
        OrderStatus updateStatus = orderUpdateStatusRequest.getStatus();

        OrderUpdateStatusResponse orderUpdateStatusResponse = orderService.updateStatus(orderId, userId, updateStatus);

        return ResponseEntity.ok(ApiResponse.success(orderUpdateStatusResponse));
    }
}
