package com.sparta.sweethoney.domain.review.entity;

import com.sparta.sweethoney.domain.order.Entity.Order;
import com.sparta.sweethoney.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 200)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
