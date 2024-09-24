package com.sparta.sweethoney.domain.order.Entity;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.COMPLETE;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @NotNull
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @NotNull
    private Menu menu;

    @NotNull
    private LocalDateTime orderTime;
    private LocalDateTime orderCompleteTime;

    @NotBlank
    private String deliveryAddress;

    private Integer count;

    @NotNull
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    public Order(User user, Store store, Menu menu,
                 LocalDateTime orderTime, String deliveryAddress, Integer count, OrderStatus status) {
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.orderTime = orderTime;
        this.deliveryAddress = deliveryAddress;
        this.count = count;
        this.amount = menu.getPrice() * count;
        this.status = status;
    }

    /**
     * 주문 상태 변경할 때, 완료면 시간도 넣어준다.
     *
     * @param status
     */
    public void updateStatus(OrderStatus status) {
        if (status.equals(COMPLETE)) {
            this.orderCompleteTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }

        this.status = status;
    }
}




