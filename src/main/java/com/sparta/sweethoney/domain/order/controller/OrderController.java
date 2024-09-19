package com.sparta.sweethoney.domain.order.controller;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> orders() {
        List<Order> orders = orderService.orderList();
        return null;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> findOrder() {
        return null;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderDto> createOrder() {
        return null;
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateStatus() {
        return null;
    }
}
