package com.sparta.sweethoney.domain.order.Entity;

import com.sparta.sweethoney.domain.common.entity.Timestamped;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.order.enums.OrderStatus;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sparta.sweethoney.domain.order.enums.OrderStatus.COMPLETE;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
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

    public Order(User user, Store store, Menu menu,
                 LocalDateTime orderTime, String deliveryAddress, Integer amount, OrderStatus status) {
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.orderTime = orderTime;
        this.deliveryAddress = deliveryAddress;
        this.amount = amount;
        this.status = status;
    }

    /**
     * 주문 상태 변경할 때, 완료면 시간도 넣어준다.
     * @param status
     */
    public void updateStatus(OrderStatus status) {
        if (status.equals(COMPLETE)) {
            this.orderCompleteTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }

        this.status = status;
    }
}
