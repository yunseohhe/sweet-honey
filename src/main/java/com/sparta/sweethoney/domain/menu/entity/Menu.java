package com.sparta.sweethoney.domain.menu.entity;

import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
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
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    // store_id 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    /* 메뉴 생성 */
    public Menu(PostMenuRequestDto requestDto, Store store, String imageUrl) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.status = requestDto.getStatus();
        this.store = store;
        this.imageUrl = imageUrl;
    }

    /* 메뉴 수정 */
    public void update(PutMenuRequestDto requestDto, String imageUrl) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.imageUrl = imageUrl;
    }

    /* 메뉴 삭제 */
    public void delete(MenuStatus menuStatus) {
        this.status = menuStatus;
    }
}
