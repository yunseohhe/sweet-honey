package com.sparta.sweethoney.domain.order.service;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.order.controller.OrderDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.order.repository.OrderRepository;
import com.sparta.sweethoney.exception.orderexception.NoOrdersFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> orderList() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new NoOrdersFoundException();
        }

        return orders;
    }

    public OrderDto findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("일치하는 주문이 없습니다."));

        return new OrderDto(order);
    }

    public OrderDto createOrder(Order order) {
        orderRepository.save(order);
        return new OrderDto(order);
    }

    public OrderDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("일치하는 주문이 없습니다."));

        order.setStatus(status);

        return new OrderDto(order);
    }

}
