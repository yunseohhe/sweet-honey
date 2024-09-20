package com.sparta.sweethoney.domain.menu.dto.response;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class PostMenuResponseDto {
    private Long id;
    private String name;
    private int price;
    private MenuStatus status;

    public PostMenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.status = menu.getStatus();
    }
}
