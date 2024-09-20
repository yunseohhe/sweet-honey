package com.sparta.sweethoney.domain.order.log;

import com.sparta.sweethoney.domain.order.dto.OrderCreateDto;
import com.sparta.sweethoney.domain.order.dto.OrderUpdateDto;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Aspect
@Slf4j
@Component
public class OrderLoggingAspect {
    @Pointcut("execution(* com.sparta.sweethoney.domain.order.service.OrderService.*(..))")
    public void orderMethods() {
    }

    @AfterReturning(pointcut = "orderMethods()", returning = "result")
    public void CreateAndUpdateStatusLog(Object result) {
        //주문 생성 시 LOGGING -> 가게 ID 대신 가게 이름으로 대체
        if (result instanceof OrderCreateDto) {
            OrderCreateDto orderCreateDto = (OrderCreateDto) result;
            Long orderId = orderCreateDto.getId();
            String storeName = orderCreateDto.getStoreName();
            OrderStatus status = orderCreateDto.getStatus();

            log.info("주문 생성 : 요청 시각={}, 주문 ID={}, 가게 이름={}, 주문 상태={}",
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), orderId, storeName, status);
        }

        //주문 상태 변경 시 LOGGING -> 가게 ID 대신 가게 이름으로 대체
        if (result instanceof OrderUpdateDto) {
            OrderUpdateDto orderUpdateDto = (OrderUpdateDto) result;

            Long orderId = orderUpdateDto.getId();
            String storeName = orderUpdateDto.getStoreName();
            OrderStatus status = orderUpdateDto.getStatus();

            log.info("주문 상태 변경 : 요청 시각={}, 주문 ID={}, 가게 이름={}, 주문 상태={}",
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), orderId, storeName, status);
        }
    }
}
