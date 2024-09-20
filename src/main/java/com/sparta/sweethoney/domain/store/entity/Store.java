package com.sparta.sweethoney.domain.store.entity;

import com.sparta.sweethoney.domain.store.enums.StoreStatus;
import com.sparta.sweethoney.domain.user.entity.User;
import jakarta.persistence.*;
import com.sparta.sweethoney.domain.common.entity.Timestamped;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(length = 20, nullable = false)
    private String name;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Column(nullable = false)
    private int minOrderPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus storeStatus = StoreStatus.OPERATING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Store(String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice, User user) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.user = user;
    }

    /* 가게 최소 가격 */
    public void setMinOrderPrice(int minOrderPrice) {
        if (minOrderPrice < 0) {
            throw new IllegalArgumentException("최소 주문 금액은 0 이상이어야 합니다.");
        }

        this.minOrderPrice = minOrderPrice;
    }

    /* 가게 수정 */
    public void update(String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
    }

    /* 가게 폐업 */
    public void terminated() {
        this.storeStatus = storeStatus.TERMINATED;
    }
}
