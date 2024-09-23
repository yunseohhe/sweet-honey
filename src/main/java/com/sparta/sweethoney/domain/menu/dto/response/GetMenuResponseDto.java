package com.sparta.sweethoney.domain.menu.dto.response;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class GetMenuResponseDto {
    private String name;
    private int price;
    private MenuStatus status;

    public GetMenuResponseDto(Menu menu) {
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.status = menu.getStatus();
    }
}
