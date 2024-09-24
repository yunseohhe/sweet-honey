package com.sparta.sweethoney.domain.store.entity;


import com.sparta.sweethoney.domain.common.entity.Timestamped;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.enums.AdStatus;
import com.sparta.sweethoney.domain.store.enums.StoreStatus;
import com.sparta.sweethoney.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Getter
@Entity
@NoArgsConstructor
public class Store extends Timestamped {
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

    /*광고 상태 설정*/
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus adStatus = AdStatus.NONE;

    /*공지 필드*/
    @Column(columnDefinition = "TEXT")
    private String notice = "";

    public Store(StoreRequest storeRequest, User user) {
        this.name = storeRequest.getName();
        this.openTime = storeRequest.getOpenTime();
        this.closeTime = storeRequest.getCloseTime();
        this.minOrderPrice = storeRequest.getMinOrderPrice();
        this.user = user;
        this.notice = storeRequest.getNotice();
    }

    /* 가게 최소 가격 */
    public void setMinOrderPrice(int minOrderPrice) {
        if (minOrderPrice < 0) {
            throw new IllegalArgumentException("최소 주문 금액은 0 이상이어야 합니다.");
        }

        this.minOrderPrice = minOrderPrice;
    }

    /* 가게 수정 */
    public void update(StoreRequest storeRequest) {
        this.name = storeRequest.getName();
        this.openTime = storeRequest.getOpenTime();
        this.closeTime = storeRequest.getCloseTime();
        this.minOrderPrice = storeRequest.getMinOrderPrice();
        // 공지가 없으면 빈 문자열로 처리
        this.notice = storeRequest.getNotice() != null ? storeRequest.getNotice() : "";
    }

    /* 가게 폐업 */
    public void terminated() {
        this.storeStatus = storeStatus.TERMINATED;
    }
}
