package com.sparta.sweethoney.domain.menu.dto.response;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class PutMenuResponseDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public PutMenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.imageUrl = menu.getImageUrl();
    }
}
