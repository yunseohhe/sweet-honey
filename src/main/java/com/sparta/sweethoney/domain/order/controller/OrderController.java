package com.sparta.sweethoney.domain.order.controller;

import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.OrderUpdateDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * @param requestDto
     * @param servletRequest
     * @return ResponseEntity<OrderCreateDto>
     */
    @PostMapping
    public ResponseEntity<OrderCreateDto> createOrder(
            @RequestBody OrderRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        requestDto.setUserId(userId);

        OrderCreateDto orderCreateDto = orderService.createOrder(requestDto);

        return ResponseEntity.ok(orderCreateDto);
    }

    /**
     * 주문 전체 조회
     * @param request
     * @return ResponseEntity<List<OrderFindDto>>
     */
    @GetMapping
    public ResponseEntity<List<OrderFindDto>> orders(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        List<OrderFindDto> orders = orderService.findAllOrders(userId);

        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 단건 조회
     * @param orderId
     * @return ResponseEntity<OrderFindDto>
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderFindDto> findOrder(@PathVariable Long orderId) {
        OrderFindDto order = orderService.findOrder(orderId);

        return ResponseEntity.ok(order);
    }

    /**
     * 주문 상태 변경
     * @param orderId
     * @param orderStatusDto
     * @return
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderUpdateDto> updateStatus(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderStatus orderStatus
    ) {
        OrderUpdateDto orderUpdateDto = orderService.updateStatus(orderId, userId, orderStatus);
        return ResponseEntity.ok(orderUpdateDto);
    }
}
