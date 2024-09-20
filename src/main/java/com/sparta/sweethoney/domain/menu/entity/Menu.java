package com.sparta.sweethoney.domain.menu.entity;

import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.common.entity.Timestamped;
import jakarta.persistence.*;
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

    // store_id 연결
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    /* 메뉴 생성 */
    public Menu(String name, int price, MenuStatus status, Store store) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.store = store;
    }

    /* 메뉴 수정 */
    public void update(PutMenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
    }

    /* 메뉴 삭제 */
    public void delete(MenuStatus menuStatus) {
        this.status = menuStatus;
    }
}
