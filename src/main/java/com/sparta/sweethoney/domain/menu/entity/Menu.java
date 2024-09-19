package com.sparta.sweethoney.domain.menu.entity;

import com.sparta.sweethoney.util.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu extends Timestamped {
    private String name;
    private int price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    // order_id 연결
}
