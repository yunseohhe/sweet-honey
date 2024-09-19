package com.sparta.sweethoney.domain.order.controller;

import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderFindDto;
import com.sparta.sweethoney.domain.order.dto.OrderUpdateDto;
import com.sparta.sweethoney.domain.order.dto.request.OrderRequestDto;
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
     * 1. requestDto
     * @param requestDto
     * @param servletRequest
     * @return
     */
    @PostMapping("")
    public ResponseEntity<OrderCreateDto> createOrder(
            @RequestBody OrderRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        requestDto.setUserId(userId);

        orderService.createOrder(requestDto);

        return null;
    }

    @GetMapping
    public ResponseEntity<List<OrderFindDto>> orders() {
        return ResponseEntity.ok(orderService.orderList());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderFindDto> findOrder(@PathVariable Long orderId) {
        return null;
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderUpdateDto> updateStatus(@PathVariable Long orderId) {
        return null;
    }
}
