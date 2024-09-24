package com.sparta.sweethoney.domain.order.cart;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Cart {

    private Long userId;
    private Long storeId;
    private final List<Menu> menus = new ArrayList<>();
    private LocalDateTime lastUpdate;

    public void clear() {
        storeId = null;
        menus.clear();
        lastUpdate = null;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        this.lastUpdate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
