package com.sparta.sweethoney.domain.order.Entity;

import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.user.entitiy.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private LocalDateTime orderTime;
    private LocalDateTime orderCompleteTime;

    private String deliveryAddress;
    private Integer amount;
    private OrderStatus status;
}
