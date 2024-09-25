package com.sparta.sweethoney.domain.review.entity;

import com.sparta.sweethoney.domain.common.entity.Timestamped;
import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends Timestamped {

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 500)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Review(Order order, Store store, int rating, String comment) {
        this.order = order;
        this.store = store;
        this.rating = rating;
        this.comment = comment;
    }
}
